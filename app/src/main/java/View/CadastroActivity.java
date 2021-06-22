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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.lucas.adocaoapp.R;

import org.jetbrains.annotations.NotNull;

import Controller.ConfigurarFirebase;
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

        btnCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String campoNome = txtNomecad.getText().toString();
                String campoEmail = txtEmailCad.getText().toString();
                String campoSenha = txtSenhaCad.getText().toString();

                if (!campoNome.isEmpty()){
                    if (!campoEmail.isEmpty()){
                        if (!campoSenha.isEmpty()){
                            user = new User();
                            user.setNome(campoNome);
                            user.setEmail(campoEmail);
                            user.setSenha(campoSenha);
                            cadastrarUsuario();
                        }else {
                            Toast.makeText(CadastroActivity.this,
                                    "Preencha uma senha forte",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(CadastroActivity.this,
                                "Preencha seu e-mail",
                                Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CadastroActivity.this,
                            "Preencha seu nome",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void cadastrarUsuario(){
        autenticacao = ConfigurarFirebase.getReferenciaAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                user.getEmail(), user.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CadastroActivity.this,
                            "Cadastrado com sucesso!",
                            Toast.LENGTH_SHORT).show();
                }else{
                    String excecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        excecao = "Digite uma senha mais forte!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "Por favor digite um e-mail válido!";
                    } catch (FirebaseAuthUserCollisionException e) {
                        excecao = "Essa conta já foi cadastrada!";
                    } catch (Exception e) {
                        excecao = "Digite uma senha mais forte!" + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void inicializarComponentes(){
        txtNomecad = findViewById(R.id.txtNomecad);
        txtEmailCad = findViewById(R.id.txtEmailCad);
        txtSenhaCad = findViewById(R.id.txtSenhaCad);
        btnCad = findViewById(R.id.btnCad);
    }
}