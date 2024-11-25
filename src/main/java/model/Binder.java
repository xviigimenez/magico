package models;

public class Binder {
    private int id;
    private String nome;
    private String descricao;
    private int idUser;

    // Construtores
    public Binder() {}

    public Binder(int id, String nome, String descricao, int idUser) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.idUser = idUser;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    @Override
    public String toString() {
        return "Binder{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", idUser=" + idUser +
                '}';
    }
}
