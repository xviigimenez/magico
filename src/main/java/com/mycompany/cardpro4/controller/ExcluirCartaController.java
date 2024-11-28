package com.mycompany.cardpro4.controller;

import dao.BinderDAO;
import dao.CartaDAO;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import model.Carta;
import model.Session;
import model.User;
import models.Binder;

/**
 * FXML Controller class
 *
 * @author PC
 */
public class ExcluirCartaController implements Initializable {

    @FXML
    private Button btnBack, btnExcluir;

    @FXML
    private ComboBox<String> tipoBox, listaBox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarComboBox();
    }

    @FXML
    private void goToCollection(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/collection.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setTitle("Collection");
            stage.setResizable(false);
            stage.setScene(scene);
	    stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            showAlert("Erro", "Erro ao carregar o Menu.", Alert.AlertType.ERROR);
        }
    }

    private void configurarComboBox() {
        User usuarioAtual = Session.getUser();

        if (usuarioAtual == null) {
            showAlert("Erro", "Nenhum usuário está logado na sessão.", Alert.AlertType.ERROR);
            return;
        }

        int idUsuario = usuarioAtual.getId();
        tipoBox.getItems().addAll("Carta", "Binder");

        tipoBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            listaBox.getItems().clear(); // Limpa itens existentes no ComboBox

            if (newValue != null) {
                try {
                    if ("Binder".equals(newValue)) {
                        BinderDAO binderDAO = new BinderDAO();
                        List<String> binders = binderDAO.listarBinderPorUsuario(idUsuario);
                        listaBox.getItems().addAll(binders); // Adiciona todos os nomes diretamente
                    } else if ("Carta".equals(newValue)) {
                        CartaDAO cartaDAO = new CartaDAO();
                        List<Carta> cartas = cartaDAO.buscarCartasPorUsuario(idUsuario);
                        for (Carta carta : cartas) {
                            listaBox.getItems().add(carta.getNome());
                        }
                    }
                } catch (SQLException e) {
                    showAlert("Erro", "Erro ao carregar os dados: " + e.getMessage(), Alert.AlertType.ERROR);
                    e.printStackTrace();
                }
            }
        });
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
