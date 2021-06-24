package Model;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

import Controller.ConfigurarFirebase;

public class Pets {

    private String idPet;
    private String estado;
    private String racas;
    private String nome;
    private String raca;
    private String telefone;
    private String descricao;
    private List<String> fotos;

    public Pets() {
        DatabaseReference anuncioref = ConfigurarFirebase.getReferenciaFirebase()
                .child("meus_pets");
        setIdPet(anuncioref.push().getKey());
    }

    public void salvar(){
        String idUsuario = ConfigurarFirebase.getIdUsuario();
        DatabaseReference anuncioref = ConfigurarFirebase.getReferenciaFirebase()
                .child("meus_pets");

        anuncioref.child(idUsuario)
                .child(getIdPet())
                .setValue(this);

    }

    public String getIdPet() {
        return idPet;
    }

    public void setIdPet(String idPet) {
        this.idPet = idPet;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getRacas() {
        return racas;
    }

    public void setRacas(String racas) {
        this.racas = racas;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<String> getFotos() {
        return fotos;
    }

    public void setFotos(List<String> fotos) {
        this.fotos = fotos;
    }
}
