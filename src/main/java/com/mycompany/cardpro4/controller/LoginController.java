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
            Session.setUser(user.getId(), user.getName(), user.getEmail());
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
            // Responsável por obter o estágio (janela) atual
            Stage currentStage = (Stage) btn_CreateAccount.getScene().getWindow();

            // Responsável por carregar o arquivo FXML do formulário de cadastro
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/cadastro.fxml"));

            // Responsável por criar uma nova cena para o formulário de cadastro
            Scene scene = new Scene(root);

            // Responsável por criar um novo palco (janela) para exibir o formulário de cadastro
            Stage newStage = new Stage();
            newStage.setTitle("Cadastro");
            newStage.setResizable(false);
            newStage.setScene(scene);

            // Responsável por exibir o novo formulário
            newStage.show();

            // Responsável por fechar o formulário de login
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace(); // Exibe erro no console caso falhe
            showAlert("Erro", "Erro ao carregar o formulário de cadastro.", AlertType.ERROR);
        }
    }

    // Responsável por abrir a tela principal após login
    private void openMainScreen() {
        try {
            // Responsável por carregar a tela principal
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/collection.fxml"));

            // Responsável por configurar a nova cena
            Scene scene = new Scene(root);

            // Responsável por obter o estágio atual
            Stage stage = (Stage) btn_Login.getScene().getWindow();

            // Responsável por configurar o palco com a cena principal
            stage.setScene(scene);

            // Responsável por exibir a nova tela
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
