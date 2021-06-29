package View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lucas.adocaoapp.R;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import Model.Pets;

public class DetalhesActivity extends AppCompatActivity {

    private TextView txtNomeDetalhes, txtRacaDetalhes, txtEstadoDetalhes, txtDescicaoDetalhes;
    private CarouselView carouselView;
    private Pets petsSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);
        getSupportActionBar().setTitle("Detalhes do Pet");

        iniciarComponentes();

        //Recuperar anuncio para exibição
        petsSelecionado = (Pets) getIntent().getSerializableExtra("petsSelecionados");

        if(petsSelecionado != null){
            txtNomeDetalhes.setText(petsSelecionado.getNome());
            txtRacaDetalhes.setText(petsSelecionado.getRaca());
            txtEstadoDetalhes.setText(petsSelecionado.getEstado());
            txtDescicaoDetalhes.setText(petsSelecionado.getDescricao());

            ImageListener imageListener = new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {
                    String urlString = petsSelecionado.getFotos().get(position);
                    Picasso.get().load(urlString).into(imageView);
                }
            };

            carouselView.setPageCount(petsSelecionado.getFotos().size());
            carouselView.setImageListener(imageListener);
        }
    }

    public void visualizarTelefone(View view){
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", petsSelecionado.getTelefone(), null ));
        startActivity( i );
    }


    private void iniciarComponentes(){

        txtNomeDetalhes = findViewById(R.id.txtNomeDetalhes);
        txtRacaDetalhes = findViewById(R.id.txtRacaDetalhes);
        txtEstadoDetalhes = findViewById(R.id.txtEstadoDetalhes);
        txtDescicaoDetalhes = findViewById(R.id.txtDescicaoDetalhes);
        carouselView = findViewById(R.id.carouselView);
    }
}