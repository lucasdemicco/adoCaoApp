package Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import Controller.ConfigurarFirebase;

public class User implements Serializable {

    private String idUser;
    private String nome;
    private String email;
    private String senha;
    private String fotoUsuario;

    public User() {
    }

    public void salvarUsuarioChat() {
        DatabaseReference firebaseRef = ConfigurarFirebase.getReferenciaFirebase();
        DatabaseReference usuario = firebaseRef.child("usuarios")
                .child(getIdUser());

        usuario.setValue(this);
    }

    public void atualizarUsuarioChat() {

        String identificadorUsuario = UserFirebaseConfig.getIdentificadorUsuario();
        DatabaseReference databaseReference = ConfigurarFirebase.getReferenciaFirebase();
        DatabaseReference usuariosRef = databaseReference.child("usuarios")
                .child(identificadorUsuario);

        Map<String, Object> valoresUsuario = converterParaMap();

        usuariosRef.updateChildren(valoresUsuario);

    }

    @Exclude
    public Map<String, Object> converterParaMap() {
        HashMap<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("email", getEmail());
        usuarioMap.put("nome", getNome());
        usuarioMap.put("foto", getFotoUsuario());

        return usuarioMap;
    }

    public User(String nome, String email, String senha, String idUser) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.idUser = idUser;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getFotoUsuario() {
        return fotoUsuario;
    }

    public void setFotoUsuario(String fotoUsuario) {
        this.fotoUsuario = fotoUsuario;
    }
}
