package View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.lucas.adocaoapp.R;

import Controller.ConfigurarFirebase;

public class BemVindoActivity extends IntroActivity {

    private FirebaseAuth autenticacao;
    private Button btnGoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_bem_vindo);

        verificarUsuarioLogado();

        btnGoLogin = findViewById(R.id.btnGoLogin);

        setButtonBackVisible(false);
        setButtonNextVisible(false);

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.black)
                .fragment(R.layout.intro1)
                .canGoBackward(false)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro2)
                .canGoForward(false)
                .build());
    }

    public void irParaAuthActivity(View view) {
        startActivity(new Intent(BemVindoActivity.this, LoginActivity.class));
        finish();
    }

    public void verificarUsuarioLogado(){
        autenticacao = ConfigurarFirebase.getReferenciaAutenticacao();
        if(autenticacao.getCurrentUser() != null){
            abrirTelaPrincipal();
        }
    }

    public void abrirTelaPrincipal(){
        startActivity(new Intent(BemVindoActivity.this, PrincipalActivity.class));
        finish();
    }
}
