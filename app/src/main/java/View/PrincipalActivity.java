package View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.lucas.adocaoapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Controller.AdapterPets;
import Controller.ConfigurarFirebase;
import Controller.RecyclerItemClickListener;
import Model.Pets;
import dmax.dialog.SpotsDialog;

public class PrincipalActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private RecyclerView recyclerAnunciosPublicos;
    private Button btnRegiao, btnCategoria;
    private AdapterPets adapterPets;
    private List<Pets> petsList = new ArrayList<>();
    private DatabaseReference anunciosPublicosRef;
    private AlertDialog dialog;
    private String filtroEstado = "";
    private boolean filtrandoPorEstado = false;
    private String filtroCategoria = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        //Configs iniciais
        autenticacao = ConfigurarFirebase.getReferenciaAutenticacao();
        anunciosPublicosRef = ConfigurarFirebase.getReferenciaFirebase().child("pets");

        iniciarComponentes();


        recyclerAnunciosPublicos.setLayoutManager(new LinearLayoutManager(this));
        recyclerAnunciosPublicos.setHasFixedSize(true);
        adapterPets = new AdapterPets(petsList, this);
        recyclerAnunciosPublicos.setAdapter(adapterPets);

        recuperarAnunciosPublicos();

        //Adicionar eventos de clique
        recyclerAnunciosPublicos.addOnItemTouchListener(new RecyclerItemClickListener(
                this,
                recyclerAnunciosPublicos,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Pets petsSelecionados = petsList.get(position);
                        Intent i = new Intent(PrincipalActivity.this, DetalhesActivity.class);
                        i.putExtra("petsSelecionados", petsSelecionados);
                        startActivity(i);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        ));

    }

   public void recuperarAnunciosPublicos(){

        dialog = new SpotsDialog.Builder()
                .setContext( this )
                .setMessage("Recuperando anúncios")
                .setCancelable( false )
                .build();
        dialog.show();

        petsList.clear();
        anunciosPublicosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for(DataSnapshot estados : snapshot.getChildren()){
                    for(DataSnapshot categorias : estados.getChildren()){
                        for(DataSnapshot racas : categorias.getChildren()){

                            Pets pet = racas.getValue(Pets.class);
                            petsList.add( pet );
                        }
                        Collections.reverse(petsList);
                        adapterPets.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void filtrarEstado(View view){

        AlertDialog.Builder dialogEstado = new AlertDialog.Builder(this);
        dialogEstado.setTitle("Selecione o estado desejado: ");

        //Configurar Spinner
        View viewSpinner = getLayoutInflater().inflate(R.layout.dialog_spinner, null);

        Spinner spinnerEstadoFiltro = viewSpinner.findViewById(R.id.spinnerFiltro);
        String [] estados = getResources().getStringArray(R.array.estados);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,
                estados
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstadoFiltro.setAdapter(adapter);

        dialogEstado.setView(viewSpinner);

        dialogEstado.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                filtroEstado = spinnerEstadoFiltro.getSelectedItem().toString();
                recuperarAnunciosPorEstado();
                filtrandoPorEstado = true;

            }
        });

        dialogEstado.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = dialogEstado.create();
        dialog.show();
    }

    public void filtrarCategoria(View view) {

        if(filtrandoPorEstado == true){
            AlertDialog.Builder dialogCategorias = new AlertDialog.Builder(this);
            dialogCategorias.setTitle("Selecione a categoria desejada: ");

            //Configurar Spinner
            View viewSpinner = getLayoutInflater().inflate(R.layout.dialog_spinner, null);

            Spinner spinnerCategoriaFiltro = viewSpinner.findViewById(R.id.spinnerFiltro);
            String [] estados = getResources().getStringArray(R.array.racas);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item,
                    estados
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategoriaFiltro.setAdapter(adapter);

            dialogCategorias.setView(viewSpinner);


            dialogCategorias.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    filtroCategoria = spinnerCategoriaFiltro.getSelectedItem().toString();
                    recuperarAnunciosPorCategoria();
                }
            });

            dialogCategorias.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog dialog = dialogCategorias.create();
            dialog.show();
        }else{
            Toast.makeText(this,
                    "Escolha primeiro uma região",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void recuperarAnunciosPorCategoria(){

        dialog = new SpotsDialog.Builder()
                .setContext( this )
                .setMessage("Recuperando anúncios")
                .setCancelable( false )
                .build();
        dialog.show();

        anunciosPublicosRef = ConfigurarFirebase.getReferenciaFirebase()
                .child("pets")
                .child(filtroEstado)
                .child(filtroCategoria);

        anunciosPublicosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                petsList.clear();
                for (DataSnapshot racas : snapshot.getChildren()) {

                    Pets pet = racas.getValue(Pets.class);
                    petsList.add(pet);
                }

                Collections.reverse(petsList);
                adapterPets.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    public void recuperarAnunciosPorEstado(){

        dialog = new SpotsDialog.Builder()
                .setContext( this )
                .setMessage("Recuperando anúncios")
                .setCancelable( false )
                .build();
        dialog.show();


        //Configurar nós por estado
        anunciosPublicosRef = ConfigurarFirebase.getReferenciaFirebase()
                .child("pets")
                .child(filtroEstado);

        anunciosPublicosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                petsList.clear();
                for(DataSnapshot categorias : snapshot.getChildren()) {
                    for (DataSnapshot racas : categorias.getChildren()) {

                        Pets pet = racas.getValue(Pets.class);
                        petsList.add(pet);
                    }
                    Collections.reverse(petsList);
                    adapterPets.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.sair:
                autenticacao.signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.meus_pets:
                startActivity(new Intent(PrincipalActivity.this, MeusPetsActivity.class));
                break;
            case R.id.configs:
                startActivity(new Intent(PrincipalActivity.this, ConfigsActivity.class));
                break;
            case R.id.chat:
                startActivity(new Intent(PrincipalActivity.this, ChatActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void iniciarComponentes(){
        recyclerAnunciosPublicos = findViewById(R.id.recyclerAnunciosPublicos);
        btnCategoria = findViewById(R.id.btnCategoria);
        btnRegiao = findViewById(R.id.btnRegiao);
    }

}