package Model;

import com.google.firebase.auth.FirebaseAuth;

import Controller.Base64Custom;
import Controller.ConfigurarFirebase;

public class UserFirebaseConfig {

    public static  String getIdentificadorUsuario(){
        FirebaseAuth usuario = ConfigurarFirebase.getReferenciaAutenticacao();
        String email = usuario.getCurrentUser().getEmail();
        String identificadorUsuario = Base64Custom.codificarBase64(email);

        return identificadorUsuario;
    }
}
