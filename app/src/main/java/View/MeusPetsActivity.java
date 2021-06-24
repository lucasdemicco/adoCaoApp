package View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.lucas.adocaoapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import Controller.AdapterPets;
import Controller.ConfigurarFirebase;
import Controller.RecyclerItemClickListener;
import Model.Pets;
import Model.User;
import dmax.dialog.SpotsDialog;

public class MeusPetsActivity extends AppCompatActivity {

    private RecyclerView recyclerPets;
    private List<Pets> petsList = new ArrayList<>();
    private Pets pets;
    private AdapterPets adapterPets;
    private DatabaseReference anuncioUsuarioRef;
    private final FirebaseAuth autenticacao = ConfigurarFirebase.getReferenciaAutenticacao();
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_meus_pets);
        super.onCreate(savedInstanceState);

        //Configs iniciais
        anuncioUsuarioRef = ConfigurarFirebase.getReferenciaFirebase()
                .child("meus_pets")
                .child(ConfigurarFirebase.getIdUsuario());

        //onStart
        iniciarComponentes();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CadastrarPetActivity.class));
            }
        });

        recyclerPets.setLayoutManager(new LinearLayoutManager(this));
        recyclerPets.setHasFixedSize(true);
        adapterPets = new AdapterPets(petsList, this);
        recyclerPets.setAdapter(adapterPets);

        recuperarAnuncios();

    }

    private void recuperarAnuncios(){

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Buscando pets")
                .setCancelable(false)
                .build();
        dialog.show();

        anuncioUsuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                petsList.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    petsList.add(ds.getValue(Pets.class));
                }
                Collections.reverse(petsList);
                adapterPets.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        //Adicionar evento de clique
        recyclerPets.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerPets,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                                Pets petsSelecionado = petsList.get(position);
                                petsSelecionado.remover();

                                adapterPets.notifyDataSetChanged();

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

    }





    private void iniciarComponentes(){
        recyclerPets = findViewById(R.id.recyclerPets);
    }
}