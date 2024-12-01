package com.mycompany.cardpro4.controller;

import model.User;
import dao.UserDAO;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Session;

public class LoginController {

    @FXML
    private TextField txtLoginEmail;
    @FXML
    private PasswordField txtLoginPassword;
    @FXML
    private Button btn_CreateAccount, btn_Login;

    private UserDAO userDAO = new UserDAO();

    // Responsável por realizar o login
       
    @FXML
    public void fazerLoginUser() {
        String email = txtLoginEmail.getText();
        String password = txtLoginPassword.getText();

        // Verifica se o e-mail ou senha estão vazios
        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Erro", "Por favor, preencha o e-mail e a senha.", AlertType.ERROR);
            return;
        }

        // Verifica se o usuário existe no banco de dados e se a senha corresponde
        User user = userDAO.consultarLogin(email, password);

        // Exibe alerta de erro ou sucesso dependendo do resultado
        if (user == null) {
            showAlert("Erro", "E-mail ou senha incorretos!", AlertType.ERROR);
        } else {
            // Inicializa o Session e armazena os dados do usuário
            Session.setUser(user.getId(), user.getName(), user.getEmail(), user.getTel(), user.getPassword());
            showAlert("Sucesso", "Login bem-sucedido!", AlertType.INFORMATION);
            openMainScreen(); // Abre a próxima tela
        }
    }

    // Responsável por exibir alertas
    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Responsável por navegar para a tela de cadastro
    @FXML
    public void goToCadastro() {
        try {
            Stage currentStage = (Stage) btn_CreateAccount.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/cadastro.fxml"));
            Scene scene = new Scene(root);
            Stage newStage = new Stage();
            newStage.setTitle("Cadastro");
            newStage.setResizable(false);
            newStage.setScene(scene);
            newStage.show();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erro", "Erro ao carregar o formulário de cadastro.", AlertType.ERROR);
        }
    }

    // Responsável por abrir a tela principal após login
    private void openMainScreen() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/collection.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) btn_Login.getScene().getWindow();
            stage.setTitle("Collection");
            stage.setScene(scene);
	    stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            showAlert("Erro", "Erro ao carregar a tela principal.", AlertType.ERROR);
        }
    }

    // Responsável por associar a ação do botão de login e cadastro
    @FXML
    public void initialize() {
        // Responsável por associar a ação do botão de login
        btn_Login.setOnAction(event -> fazerLoginUser());

        // Responsável por associar a ação do botão de cadastro
        btn_CreateAccount.setOnAction(event -> goToCadastro());
    }
}
