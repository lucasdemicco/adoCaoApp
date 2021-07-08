package View;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.lucas.adocaoapp.R;

import Controller.ConfigurarFirebase;
import Model.Pets;
import Model.User;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatConversaActivity extends AppCompatActivity {

    private CircleImageView circleImageFoto;
    private TextView txtNomeToolbar;
    private User usuarioDestinatario;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_conversa);
        getSupportActionBar().setTitle("Conversas");

        iniciarComponentes();

        //Recuperar usuário
        /*Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            usuarioDestinatario = (User) bundle.getSerializable("chatConversa");
            txtNomeToolbar.setText(usuarioDestinatario.getNome());

            String foto = usuarioDestinatario.getFotoUsuario();
            if(foto != null){
                Uri url = Uri.parse(usuarioDestinatario.getFotoUsuario());
                Glide.with(ChatConversaActivity.this)
                        .load(url)
                        .into(circleImageFoto);
            }else{
                circleImageFoto.setImageResource(R.drawable.fotopadrao);
            }
        }*/

    }

    private void iniciarComponentes(){
        txtNomeToolbar = findViewById(R.id.txtNomeToolbar);
        circleImageFoto = findViewById(R.id.circleImageFoto);
    }
}