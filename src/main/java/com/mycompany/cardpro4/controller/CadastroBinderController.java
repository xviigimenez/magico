package com.mycompany.cardpro4.controller;

import dao.BinderDAO;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Session;
import models.Binder;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class CadastroBinderController implements Initializable {

    @FXML
    private TextField txtBinderName;
    @FXML
    private Button btnSaveBinder;
    @FXML
    private TextArea txtBinderDescription;
    @FXML
    private Button btnBack;

    private BinderDAO binderDAO;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        binderDAO = new BinderDAO();
    }

    @FXML
private void handleSaveBinder() {
    // Obtém os dados do usuário da sessão
    int userId = Session.getUser().getId();  // Pega o id do usuário da sessão
    
    String binderName = txtBinderName.getText();
    String binderDescription = txtBinderDescription.getText();
    
    if(binderName.isEmpty() || binderDescription.isEmpty()){
       exibirAlerta(Alert.AlertType.WARNING, "Erro", "Preencha os campos!");
       return;
    }
        
    
    
    // Cria um novo objeto Binder com os dados preenchidos
    Binder binder = new Binder();
    binder.setNome(txtBinderName.getText());
    binder.setDescricao(txtBinderDescription.getText());
    binder.setIdUser(userId);

    // Verifica se o nome do binder já existe para o usuário
    if (binderDAO.nomeBinderExiste(binder.getNome(), userId)) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText("Já existe um binder com esse nome. Escolha outro nome.");
        alert.showAndWait();
        return;  // Não salvar, se o nome já existir
    }

    // Salva o binder no banco de dados
    binderDAO.inserir(binder);
    
    
//  Cria a mensagem de sucesso!
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Sucesso");
    alert.setHeaderText(null);
    alert.setContentText("Os dados do Binder foram salvos com sucesso!");
    alert.showAndWait();
    
//  Limpa os campos apos o cadastro!
    txtBinderDescription.setText("");
    txtBinderName.setText("");


}

    @FXML
    private void goToCadastroCarta() {
        try {
            // Responsável por carregar a tela principal
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/cadastroCarta.fxml"));

            // Responsável por configurar a nova cena
            Scene scene = new Scene(root);

            // Responsável por obter o estágio atual
            Stage stage = (Stage) btnBack.getScene().getWindow();

            // Responsável por configurar o palco com a cena principal
            stage.setScene(scene);

	    // Centraliza a janela principal
	    stage.centerOnScreen();

            // Responsável por exibir a nova tela
            stage.show();
        } catch (IOException e) {
            showAlert("Erro", "Erro ao carregar a tela principal.", Alert.AlertType.ERROR);
        }
    }

    
    private void showAlert(String erro, String erro_ao_carregar_a_tela_principal, Alert.AlertType alertType) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    private void exibirAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
       Alert alert = new Alert(tipo);
       alert.setTitle(titulo);
       alert.setContentText(mensagem);
       alert.showAndWait();
    }
}
