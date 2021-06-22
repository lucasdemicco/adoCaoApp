package View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.lucas.adocaoapp.R;

import Controller.ConfigurarFirebase;
import Model.User;

public class LoginActivity extends AppCompatActivity {

    private EditText txtEmail, txtSenha;
    private Button btnEntrar;

    private FirebaseAuth autenticacao;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inicializarComponentes();

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String campoEmail = txtEmail.getText().toString();
                String campoSenha = txtSenha.getText().toString();

                if (!campoEmail.isEmpty()){
                    if (!campoSenha.isEmpty()){
                        User user = new User();
                        user.setEmail(campoEmail);
                        user.setSenha(campoSenha);
                    }else{
                        Toast.makeText(LoginActivity.this,
                                "Preencha sua senha",
                                Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this,
                            "Preencha seu e-mail",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void validarLogin(){
        autenticacao = ConfigurarFirebase.getReferenciaAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                user.getEmail(),
                user.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    abrirTelaPrincipal();
                }else{
                    String excecao="";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "Por favor digite um e-mail válido!";
                    }catch (FirebaseAuthInvalidUserException e){
                        excecao = "Usuário não cadastrado!";
                    }catch (Exception e){
                        excecao = "Digite uma senha mais forte!" + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void abrirTelaPrincipal(){
        startActivity(new Intent(this, PrincipalActivity.class));
        finish();
    }

    public void irParaCadastroActivity(View view) {
        startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
    }

    private void inicializarComponentes(){
        txtEmail = findViewById(R.id.txtEmail);
        txtSenha = findViewById(R.id.txtSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
    }

}