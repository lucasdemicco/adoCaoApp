package View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.lucas.adocaoapp.R;

import Model.User;

public class CadastroActivity extends AppCompatActivity {

    private EditText txtEmailCad, txtSenhaCad, txtNomecad;
    private Button btnCad;
    private FirebaseAuth autenticacao;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        inicializarComponentes();

    }

    private void inicializarComponentes(){
        txtNomecad = findViewById(R.id.txtNomecad);
        txtEmailCad = findViewById(R.id.txtEmailCad);
        txtSenhaCad = findViewById(R.id.txtSenhaCad);
        btnCad = findViewById(R.id.btnCad);
    }
}