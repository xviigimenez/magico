/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

package com.mycompany.cardpro4.controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import dao.TrocasDAO;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.Carta;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import com.google.gson.Gson;
import java.util.Map;
import dao.CartaDAO;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import model.Carta;
import model.Session;
import model.User;
import model.Contexto;
import model.Trocas;

/**
 * FXML Controller class
 *
 * @author vinicius
 */
public class PropostaTrocaController implements Initializable {

    @FXML
    private Button btnCriarTroca;
    @FXML
    private Button btnBack;
    @FXML
    private ComboBox<String> cartasBox;
    @FXML
    private Label lblDetalhesTroca;
    
    private final CartaDAO cartaDAO = new CartaDAO();

    public int getUsuarioAtivoId(String sessionJson) {
        Gson gson = new Gson();
        Map<String, Object> sessionData = gson.fromJson(sessionJson, Map.class);
        return ((Double) sessionData.get("id")).intValue();
    }
    public List<Carta> carregarCartasUsuarioAtivo(String sessionJson) {
    int idUsuario = getUsuarioAtivoId(sessionJson);
    CartaDAO cartaDAO = new CartaDAO();
    try {
        return cartaDAO.buscarCartasPorUsuario(idUsuario);
    } catch (SQLException e) {
        e.printStackTrace();
        return null;
        }
    }
    public void carregarCartas() {
        try {
            User usuarioAtivo = Session.getUser();
            if (usuarioAtivo == null) {
                System.out.println("Nenhum usuário ativo na sessão.");
                return;
            }

            int idUsuario = usuarioAtivo.getId();

            List<Carta> cartas = cartaDAO.buscarCartasPorUsuario(idUsuario);

            cartasBox.getItems().clear();
            for (Carta carta : cartas) {
                cartasBox.getItems().add(carta.getNome());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void handleTroca(ActionEvent event){
        try{
            String cartaSelecionada = cartasBox.getValue();
            if (cartaSelecionada == null || cartaSelecionada.isEmpty()) {
                showAlert("Erro", "Por favor, selecione uma carta.", Alert.AlertType.WARNING);
                return;
            }
            int idCarta2 = cartaDAO.buscarIdPorNome(cartaSelecionada);
            User usuarioAtivo = Session.getUser();
            int idUsuario2 = usuarioAtivo.getId();
            int idTroca = Contexto.getIdTrocaSelecionada();
            
            TrocasDAO trocasDAO = new TrocasDAO();
            Trocas troca = trocasDAO.consultar(idTroca);
            if (troca == null) {
                showAlert("Erro", "Troca não encontrada.", Alert.AlertType.ERROR);
                return;
            }

            troca.setIdCarta2(idCarta2);
            troca.setIdUser2(idUsuario2);

            trocasDAO.alterar(troca);

            showAlert("Sucesso", "Troca atualizada com sucesso!", Alert.AlertType.INFORMATION);
            
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/trocas.fxml"));
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
            
            }catch(SQLException e) {
                e.printStackTrace();
                showAlert("Erro", "Erro ao processar a troca.", Alert.AlertType.ERROR);
        }
    }
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarCartas();
        int idTroca = Contexto.getIdTrocaSelecionada();
    }

    @FXML
    private void goToTrocas(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/trocas.fxml"));
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
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
