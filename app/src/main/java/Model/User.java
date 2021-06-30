package Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import Controller.ConfigurarFirebase;

public class User {

    private String idUser;
    private String nome;
    private String email;
    private String senha;

    public User() {
    }

    public void salvarUsuarioChat(){
        DatabaseReference firebaseRef = ConfigurarFirebase.getReferenciaFirebase();
        DatabaseReference usuario = firebaseRef.child("usuarios")
                .child(getIdUser());

        usuario.setValue( this );
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
}
