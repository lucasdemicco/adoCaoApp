package View;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.lucas.adocaoapp.R;

import Controller.Permissoes;

public class ConfigsActivity extends AppCompatActivity {

    private ImageButton imgCamera, imgGaleria;
    private String[] permissoes = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configs);
        getSupportActionBar().setTitle("Ajustes");

        Permissoes.validarPermissoes(permissoes, this, 1);
        iniciarComponentes();

        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i, SELECAO_CAMERA);
                }
            }
        });

    }

    private void iniciarComponentes(){
        imgCamera = findViewById(R.id.imgCamera);
        imgGaleria = findViewById(R.id.imgGaleria);
    }
}