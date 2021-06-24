package View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

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
        }
        return super.onOptionsItemSelected(item);
    }

    private void iniciarComponentes(){
        recyclerAnunciosPublicos = findViewById(R.id.recyclerAnunciosPublicos);
        btnCategoria = findViewById(R.id.btnCategoria);
        btnRegiao = findViewById(R.id.btnRegiao);
    }
}