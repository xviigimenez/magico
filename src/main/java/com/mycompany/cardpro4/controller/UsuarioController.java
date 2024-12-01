package com.mycompany.cardpro4.controller;

import dao.BinderDAO;
import dao.CartaDAO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Carta;
import model.Session;
import model.User;

public class UsuarioController implements Initializable {

    @FXML 
    private Label lblUserName, lblUserEmail, lblUserTelefone, lblUserSenha;

    @FXML
    private Button btnCollection, btnUser, btnTrocas, btnSair;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            exibirNomeUsuario();
	    exibirEmailUsuario();
	    exibirTelefoneUsuario();
	    exibirSenhaUsuario();
        });
    }

    private void exibirNomeUsuario() {
        User user = Session.getUser();
        if (user != null) {
            lblUserName.setText("Usuário: " + user.getName());
        } else {
            lblUserName.setText("Usuário não encontrado.");
        }
    }

    private void exibirEmailUsuario() {
        User user = Session.getUser();
        if (user != null) {
            lblUserEmail.setText("Email: " + user.getPassword()); // Invertido com exibirSenhaUsuario()
        } else {
            lblUserEmail.setText("Usuário não encontrado.");
        }
    }

    private void exibirTelefoneUsuario() {
        User user = Session.getUser();
        if (user != null) {
            lblUserTelefone.setText("Telefone: " + user.getTel());
        } else {
            lblUserTelefone.setText("Usuário não encontrado.");
        }
    }

    private void exibirSenhaUsuario() {
        User user = Session.getUser();
        if (user != null) {
            lblUserSenha.setText("Senha: " + user.getEmail()); // Invertido com exibirEmailUsuario()
        } else {
            lblUserSenha.setText("Usuário não encontrado.");
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    
    // Navegação

    @FXML
    private void goToCollection(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/collection.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnCollection.getScene().getWindow();
            stage.setTitle("Collection");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            showAlert("Erro", "Erro ao carregar a tela de coleções.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void goToUsuario(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/usuario.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnUser.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            showAlert("Erro", "Erro ao carregar a tela de usuário.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void goToTrocas(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/trocas.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnTrocas.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            showAlert("Erro", "Erro ao carregar a tela de trocas.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void sairConta(ActionEvent event) {
        try {
	    // Encerra a sessão
	    Session.clearSession();
	    // Exibe um alerta antes de mudar de tela
	    showAlert("Logout", "Desconectado da conta com sucesso!", Alert.AlertType.INFORMATION);
	    // Carrega a tela de login novamente
            Parent root = FXMLLoader.load(getClass().getResource("/com/mycompany/cardpro4/login.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnSair.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            showAlert("Erro", "Erro ao carregar a tela de login.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
