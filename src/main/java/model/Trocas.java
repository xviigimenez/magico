package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Trocas {
    private IntegerProperty id;
    private IntegerProperty idCarta1;
    private IntegerProperty idCarta2; // Agora pode ser null ou com valor default
    private IntegerProperty idUser1;
    private IntegerProperty idUser2; // Agora pode ser null ou com valor default
    private StringProperty raridadeMin;
    private BooleanProperty concluido;
    private StringProperty description;
    private BooleanProperty isCartaOferecida;

    // Construtores
    public Trocas() {
        this.id = new SimpleIntegerProperty();
        this.idCarta1 = new SimpleIntegerProperty();
        this.idCarta2 = new SimpleIntegerProperty();  // Valor default possível
        this.idUser1 = new SimpleIntegerProperty();
        this.idUser2 = new SimpleIntegerProperty();  // Valor default possível
        this.raridadeMin = new SimpleStringProperty();
        this.concluido = new SimpleBooleanProperty();
        this.description = new SimpleStringProperty();
        this.isCartaOferecida = new SimpleBooleanProperty();
    }

    public Trocas(int id, int idCarta1, Integer idCarta2, int idUser1, Integer idUser2, 
                  String raridadeMin, boolean concluido, String description, boolean isCartaOferecida) {
        this();
        setId(id);
        setIdCarta1(idCarta1);
        setIdCarta2(idCarta2 != null ? idCarta2 : -1);  // Se null, atribui valor default
        setIdUser1(idUser1);
        setIdUser2(idUser2 != null ? idUser2 : -1);  // Se null, atribui valor default
        setRaridadeMin(raridadeMin);
        setConcluido(concluido);
        setDescription(description);
        setCartaOferecida(isCartaOferecida);
    }

    // Getters e Setters
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public int getIdCarta1() {
        return idCarta1.get();
    }

    public void setIdCarta1(int idCarta1) {
        this.idCarta1.set(idCarta1);
    }

    public IntegerProperty idCarta1Property() {
        return idCarta1;
    }

    public Integer getIdCarta2() {
        return idCarta2.get() == -1 ? null : idCarta2.get();  // Retorna null se o valor for -1
    }

    public void setIdCarta2(Integer idCarta2) {
        this.idCarta2.set(idCarta2 != null ? idCarta2 : -1);  // Se null, atribui valor default
    }

    public IntegerProperty idCarta2Property() {
        return idCarta2;
    }

    public int getIdUser1() {
        return idUser1.get();
    }

    public void setIdUser1(int idUser1) {
        this.idUser1.set(idUser1);
    }

    public IntegerProperty idUser1Property() {
        return idUser1;
    }

    public Integer getIdUser2() {
        return idUser2.get() == -1 ? null : idUser2.get();  // Retorna null se o valor for -1
    }

    public void setIdUser2(Integer idUser2) {
        this.idUser2.set(idUser2 != null ? idUser2 : -1);  // Se null, atribui valor default
    }

    public IntegerProperty idUser2Property() {
        return idUser2;
    }

    public String getRaridadeMin() {
        return raridadeMin.get();
    }

    public void setRaridadeMin(String raridadeMin) {
        this.raridadeMin.set(raridadeMin);
    }

    public StringProperty raridadeMinProperty() {
        return raridadeMin;
    }

    public boolean isConcluido() {
        return concluido.get();
    }

    public void setConcluido(boolean concluido) {
        this.concluido.set(concluido);
    }

    public BooleanProperty concluidoProperty() {
        return concluido;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public boolean isCartaOferecida() {
        return isCartaOferecida.get();
    }

    public void setCartaOferecida(boolean isCartaOferecida) {
        this.isCartaOferecida.set(isCartaOferecida);
    }

    public BooleanProperty cartaOferecidaProperty() {
        return isCartaOferecida;
    }

    public boolean getConcluido() {
        return concluido.get();
    }
}
