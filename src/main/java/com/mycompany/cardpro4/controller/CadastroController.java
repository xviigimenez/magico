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

public class CadastroController {

    @FXML
    private TextField txtName;
    @FXML
    private PasswordField txtPassword, txtConfirmPassword;    
    @FXML
    private TextField txtTel;
    @FXML
    private TextField txtEmail;
    @FXML
    private Button btnBack;

    private UserDAO userDAO = new UserDAO();

    // Responsável por voltar à tela de login
    @FXML
    public void goToLogin() {
        try {
            Stage currentStage = (Stage) btnBack.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/mycompany/cardpro4/login.fxml"));
            Scene scene = new Scene(root);
            Stage newStage = new Stage();
            newStage.setResizable(false);
            newStage.setTitle("Login");
            newStage.setScene(scene);
	    newStage.centerOnScreen();
            newStage.show();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace(); // Exibe erro no console caso falhe
        }
    }

    // Responsável por registrar o usuário
    @FXML
    public void registerUser() {
        // Responsável por obter os dados preenchidos no formulário
        String name = txtName.getText();
        String password = txtPassword.getText();
        String tel = txtTel.getText();
        String email = txtEmail.getText();
        String confirmPassword = txtConfirmPassword.getText();

        // Responsável por verificar se algum campo está vazio
        if (name.isEmpty() || password.isEmpty() || tel.isEmpty() || email.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Erro", "Todos os campos devem ser preenchidos!", AlertType.ERROR);
            return;
        }

        // Responsável por verificar se as senhas coincidem
        if (!password.equals(confirmPassword)) {
            showAlert("Erro", "As senhas não coincidem!", AlertType.ERROR);
            return;
        }

        // Responsável por verificar se o telefone tem um formato válido (apenas números)
        if (!tel.matches("\\d+")) {
            showAlert("Erro", "O número de telefone deve conter apenas números!", AlertType.ERROR);
            return;
        }

        // Responsável por verificar se o email tem um formato válido simples
        if (!email.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z]{2,}$")) {
            showAlert("Erro", "O email informado é inválido!", AlertType.ERROR);
            return;
        }

        // Responsável por verificar se o e-mail já está cadastrado
        if (userDAO.emailExists(email)) {
            showAlert("Erro", "O e-mail já está cadastrado!", AlertType.ERROR);
            return;
        }

        // Responsável por criar um novo objeto User com id sendo nulo, o banco irá gerar
        User user = new User(name, email, tel, password);

        // Responsável por inserir o usuário no banco
        userDAO.inserir(user);

        // Responsável por exibir um alerta informando o sucesso
        showAlert("Sucesso", "Usuário cadastrado com sucesso!", AlertType.INFORMATION);
        
        
//      Deixa em branco os campos
        txtName.setText("");
        txtEmail.setText("");
        txtTel.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");

    }

    // Responsável por exibir alertas
    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
