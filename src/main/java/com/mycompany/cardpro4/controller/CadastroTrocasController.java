/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.cardpro4.controller;

import dao.CartaDAO;
import dao.TrocasDAO;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.Carta;
import model.Session;
import model.Trocas;
import model.User;

/**
 * FXML Controller class
 *
 * @author PC
 */
public class CadastroTrocasController implements Initializable {

    @FXML
    private ComboBox<String> cartasBox;
    @FXML
    private ComboBox<String> raridadeMinBox;
   
    @FXML
    private Button btnCriarTroca, btnBack;
    @FXML
    private TextArea txtDescription;
 
  


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarComboBoxes();
        btnCriarTroca.setOnAction(event -> handleCriarTroca());
    }
    private void handleCriarTroca() {
        // Exibe uma caixa de diálogo de confirmação
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmação");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Deseja realmente criar essa troca?");

        // Se o usuário confirmar, cria a troca
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                criarTroca(); // Método para cadastrar a troca no banco
            }
        });
    }

    private void criarTroca() {
        User usuarioAtual = Session.getUser();

        if (usuarioAtual == null) {
            showAlert("Erro", "Nenhum usuário está logado na sessão.", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Validações básicas
            if (cartasBox.getValue() == null || raridadeMinBox.getValue() == null || txtDescription.getText().isEmpty()) {
                showAlert("Erro", "Preencha todos os campos obrigatórios.", Alert.AlertType.ERROR);
                return;
            }

            // Obtém os dados selecionados
            String cartaSelecionada = cartasBox.getValue();
            String raridadeMin = raridadeMinBox.getValue();
            String description = txtDescription.getText();

            // Busca o ID da carta pelo nome selecionado
            CartaDAO cartaDAO = new CartaDAO();
            int idCarta1 = cartaDAO.buscarIdPorNome(cartaSelecionada);

            // Cria o objeto Troca
            Trocas novaTroca = new Trocas();
            novaTroca.setIdCarta1(idCarta1);
            novaTroca.setIdUser1(usuarioAtual.getId());
            novaTroca.setRaridadeMin(raridadeMin);
            novaTroca.setDescription(description);
            novaTroca.setConcluido(false); // Padrão
            novaTroca.setCartaOferecida(false); // Padrão

            // Insere a troca no banco de dados
            TrocasDAO trocasDAO = new TrocasDAO();
            trocasDAO.inserir(novaTroca);

            showAlert("Sucesso", "Troca criada com sucesso!", Alert.AlertType.INFORMATION);

        } catch (SQLException e) {
            showAlert("Erro", "Erro ao criar a troca: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    
    
    private void configurarComboBoxes() {
        raridadeMinBox.getItems().addAll(
                "Qualquer um",
                "Comum", 
                "Incomum", 
                "Raro", 
                "Raro Holográfico", 
                "Ultra Raro", 
                "Hiper Raro", 
                "Secreto Raro"
        );

        
        configurarCartasComboBox();
    }
    
    private void configurarCartasComboBox() {
    User usuarioAtual = Session.getUser();

    if (usuarioAtual == null) {
        showAlert("Erro", "Nenhum usuário está logado na sessão.", Alert.AlertType.ERROR);
        return;
    }

    int idUsuario = usuarioAtual.getId();

    try {
        CartaDAO cartaDAO = new CartaDAO();
        List<Carta> cartas = cartaDAO.buscarCartasPorUsuario(idUsuario);

        // Limpa os itens existentes no ComboBox e adiciona os novos
        cartasBox.getItems().clear();
        for (Carta carta : cartas) {
            cartasBox.getItems().add(carta.getNome());
        }

    } catch (SQLException e) {
        showAlert("Erro", "Erro ao carregar as cartas: " + e.getMessage(), Alert.AlertType.ERROR);
        e.printStackTrace();
    }
}
    
    
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void goToTrocas(ActionEvent event) {
        try {
            // Responsável por carregar a tela principal
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/trocas.fxml"));

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
}
