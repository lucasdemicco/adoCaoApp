package View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lucas.adocaoapp.R;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;

import Controller.Base64Custom;
import Controller.ConfigurarFirebase;
import Controller.Permissoes;
import Model.User;
import Model.UserFirebaseConfig;
import de.hdodenhof.circleimageview.CircleImageView;

public class ConfigsActivity extends AppCompatActivity {

    private ImageButton imgCamera, imgGaleria;
    private String[] permissoes = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;

    private CircleImageView imagemPerfil;
    private User user;
    private StorageReference storageReference;
    private String identificadorUsuario;
    private EditText txtNomeUsuario;
    private ImageView imgEditNome;
    private FirebaseUser usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configs);
        getSupportActionBar().setTitle("Ajustes");

        iniciarComponentes();

        user = UserFirebaseConfig.getDadosUsuarioLogado();

        Permissoes.validarPermissoes(permissoes, this, 1);
        storageReference = ConfigurarFirebase.getReferenciaStorage();
        identificadorUsuario = UserFirebaseConfig.getIdentificadorUsuario();

        usuario = UserFirebaseConfig.getUsuarioAtual();

        txtNomeUsuario.setText(usuario.getDisplayName());

        Uri url = usuario.getPhotoUrl();
        if(url != null){
            Glide.with(ConfigsActivity.this)
                    .load(url)
                    .into(imagemPerfil);
        }else{
            imagemPerfil.setImageResource(R.drawable.fotopadrao);
        }

        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i, SELECAO_CAMERA);
                }
            }
        });

        imgGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i, SELECAO_GALERIA);
                }
            }
        });

        imgEditNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nomeEditado = txtNomeUsuario.getText().toString();
                boolean retorno = UserFirebaseConfig.atualizarNomeUsuario(nomeEditado);
                if(retorno){
                    user.setNome( nomeEditado );
                    user.atualizarUsuarioChat();

                    Toast.makeText(ConfigsActivity.this,
                            "Alterado com sucesso",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            Bitmap imagem = null;

            try {

                switch (requestCode){
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                        break;
                }

                if(imagem != null){
                    imagemPerfil.setImageBitmap(imagem);

                    //Recuperar dados
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos );
                    byte[] dadosImagem = baos.toByteArray();

                    //Salvar Firebase
                     final StorageReference imagemRef = storageReference.child("imagens")
                             .child("perfil")
                             .child(identificadorUsuario)
                             .child("perfil.jpg");

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(ConfigsActivity.this,
                                    "Erro ao fazer upload da imagem",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(ConfigsActivity.this,
                                    "Sucesso ao fazer upload da imagem",
                                    Toast.LENGTH_SHORT).show();

                            imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Uri> task) {
                                    Uri url = task.getResult();
                                    atualizarFotoUsuario(url);
                                }
                            });

                        }
                    });
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public void atualizarFotoUsuario(Uri url){

        boolean retorno = UserFirebaseConfig.atualizarFoto(url);
        if(retorno){
            user.setFotoUsuario(url.toString());
            user.atualizarUsuarioChat();
        }
    }

    private void iniciarComponentes(){
        imgEditNome = findViewById(R.id.imgEditNome);
        txtNomeUsuario = findViewById(R.id.txtNomeUsuario);
        imgCamera = findViewById(R.id.imgCamera);
        imgGaleria = findViewById(R.id.imgGaleria);
        imagemPerfil = findViewById(R.id.imagemPerfil);
    }
}