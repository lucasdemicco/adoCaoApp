package View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lucas.adocaoapp.R;
import com.santalu.maskara.widget.MaskEditText;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import Controller.ConfigurarFirebase;
import Controller.Permissoes;
import Model.Pets;
import dmax.dialog.SpotsDialog;

public class CadastrarPetActivity extends AppCompatActivity
        implements View.OnClickListener {

    private String[] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
    };

    private List<String> listaFotos = new ArrayList<>();
    private List<String> listaUrlFotos = new ArrayList<>();

    private EditText txtNome, txtRaca, txtDescricao;
    private MaskEditText txtTelefone;
    private ImageView img1, img2, img3;
    private Spinner spinnerEstados, spinnerRacas;

    private Pets pets;
    private StorageReference storage;

    private android.app.AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_pet);
        getSupportActionBar().setTitle("Cadastrar novo Pet");

        iniciarComponentes();
        carregarDadosSpinner();

        //Configs iniciais
        storage = ConfigurarFirebase.getReferenciaStorage();

        //Validar permissões
        Permissoes.validarPermissoes(permissoes, this, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissaoResultado : grantResults) {
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                alertaValidacaoPermissao();
            }
        }
    }

    private void alertaValidacaoPermissao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões negadas");
        builder.setMessage("Para utilizar a câmera ou galeria é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.img1:
                escolherImagem(1);
                break;
            case R.id.img2:
                escolherImagem(2);
                break;
            case R.id.img3:
                escolherImagem(3);
                break;
        }
    }

    public void escolherImagem(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            //Recuperar imagem
            Uri imagemSelecionada = data.getData();
            String caminhoImagem = imagemSelecionada.toString();

            //Configurar imagem no ImageView
            if (requestCode == 1) {
                img1.setImageURI(imagemSelecionada);
            } else if (requestCode == 2) {
                img2.setImageURI(imagemSelecionada);
            } else if (requestCode == 3) {
                img3.setImageURI(imagemSelecionada);
            }

            listaFotos.add(caminhoImagem);
        }

    }

    private void carregarDadosSpinner() {
        String [] estados = getResources().getStringArray(R.array.estados);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,
                estados
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstados.setAdapter(adapter);

        String [] racas = getResources().getStringArray(R.array.racas);
        ArrayAdapter<String> adapterRacas = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,
                racas
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRacas.setAdapter(adapterRacas);
    }

    private void salvarPet() {

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Salvando pet")
                .setCancelable(false)
                .build();
        dialog.show();


        //Salvar imagem Storage
        for(int i=0; i <listaFotos.size(); i++){
            String urlImagem = listaFotos.get(i);
            int tamanhoLista = listaFotos.size();
            salvarFotoStorage(urlImagem, tamanhoLista, i );
        }
        Toast.makeText(this,
                "Salvo com sucesso!",
                Toast.LENGTH_SHORT).show();
    }

    private void salvarFotoStorage(String urlFoto, int totalFotos, int contador){

        //Criar nó Storage
        final StorageReference imagemAnuncio = storage.child("imagens")
                .child("pets")
                .child(pets.getIdPet())
                .child("Imagem" + contador);

        //Fazer upload do arquivo
        UploadTask uploadTask = imagemAnuncio.putFile(Uri.parse(urlFoto));
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imagemAnuncio.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Uri> task) {
                        Uri url = task.getResult();
                        String urlConvertida = url.toString();

                        listaUrlFotos.add(urlConvertida);

                        if(totalFotos == listaUrlFotos.size()){
                            pets.setFotos(listaUrlFotos);
                            pets.salvar();
                            dialog.dismiss();
                            finish();
                        }
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                exibirMensagemErro("Falha ao fazer upload!");
            }
        });

    }


    public void validarDadosCadastro(View view){

        pets = configurarAnuncio();

        if(listaFotos.size() != 0){
            if(!pets.getEstado().isEmpty()){
                if(!pets.getRacas().isEmpty()){
                    if(!pets.getNome().isEmpty()){
                        if(!pets.getRaca().isEmpty()){
                            if(!pets.getTelefone().isEmpty() && pets.getTelefone().length() >=10){
                                if(!pets.getDescricao().isEmpty()){
                                    salvarPet();
                                }else{
                                    exibirMensagemErro("Digite uma descrição do pet!");

                                }
                            }else{
                                exibirMensagemErro("Digite um telefone");

                            }
                        }else{
                            exibirMensagemErro("Digite a raça do pet!");

                        }
                    }else{
                        exibirMensagemErro("Digite o nome do pet!");
                    }
                }else{
                    exibirMensagemErro("Selecione o tipo de pet!");
                }
            }else{
                exibirMensagemErro("Selecione o estado!");
            }
        }else{
            exibirMensagemErro("Selecione ao menos uma foto!");
        }

    }

    private Pets configurarAnuncio(){
        String estado = spinnerEstados.getSelectedItem().toString();
        String racas = spinnerRacas.getSelectedItem().toString();
        String nome = txtNome.getText().toString();
        String raca = txtRaca.getText().toString();
        String telefone = txtTelefone.getText().toString();
        String descricao = txtDescricao.getText().toString();

        Pets pets = new Pets();
        pets.setEstado(estado);
        pets.setRacas(racas);
        pets.setNome(nome);
        pets.setRaca(raca);
        pets.setTelefone(telefone);
        pets.setDescricao(descricao);

        return pets;

    }

    public void exibirMensagemErro(String mensagem){
        Toast.makeText(this,
                mensagem,
                Toast.LENGTH_SHORT).show();
    }

    private void iniciarComponentes() {

        txtNome = findViewById(R.id.txtNome);
        txtRaca = findViewById(R.id.txtRaca);
        txtTelefone = findViewById(R.id.txtTelefone);
        txtDescricao = findViewById(R.id.txtDescricao);
        img1 = findViewById(R.id.img1);
        img1.setOnClickListener(this);
        img2 = findViewById(R.id.img2);
        img2.setOnClickListener(this);
        img3 = findViewById(R.id.img3);
        img3.setOnClickListener(this);

        spinnerEstados = findViewById(R.id.spinnerEstados);
        spinnerRacas = findViewById(R.id.spinnerRacas);
    }
}