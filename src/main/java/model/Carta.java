package model;

public class Carta {
    private int id;
    private String nome;
    private String raridade;
    private String tema;
    private String idColecao;
    private byte[] imagem; // Para armazenar a imagem como BLOB
    private Integer idUser;

    // Construtor completo
    public Carta(Integer id, String nome, String raridade, String tema, String idColecao, byte[] imagem, Integer idUser) {
        this.id = id;
        this.nome = nome;
        this.raridade = raridade;
        this.tema = tema;
        this.idColecao = idColecao;
        this.imagem = imagem;
        this.idUser = idUser;
    }
    public Carta(int id, String nome, String raridade, String tema, String idColecao, byte[] imagem) {
        this.id = id;
        this.nome = nome;
        this.raridade = raridade;
        this.tema = tema;
        this.idColecao = idColecao;
        this.imagem = imagem;
    }
    public Carta(String nome, String raridade, String tema, String idColecao, byte[] imagem) {
   
        this.nome = nome;
        this.raridade = raridade;
        this.tema = tema;
        this.idColecao = idColecao;
        this.imagem = imagem;
    }   

    public Carta(int aInt, String string, byte[] bytes) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
    public Integer getIdUser() {
        return idUser;
    }

    // Getters e Setters
    public void setIdUser(Integer idUser) {    
        this.idUser = idUser;
    }

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

    public String getRaridade() {
        return raridade;
    }

    public void setRaridade(String raridade) {
        this.raridade = raridade;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getIdColecao() {
        return idColecao;
    }

    public void setIdColecao(String idColecao) {
        this.idColecao = idColecao;
    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }
}
