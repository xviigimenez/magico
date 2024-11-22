/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.cardpro4.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author PC
 */

public class CollectionController implements Initializable {

    @FXML
    private Button btnExcluirCarta;
    @FXML
    private Button btnCadastrarCarta;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void goToCadastroCarta(ActionEvent event) {
        
       try {
            // Responsável por obter o estágio (janela) atual
            Stage currentStage = (Stage) btnCadastrarCarta.getScene().getWindow();

            // Responsável por carregar o arquivo FXML do formulário de cadastro
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/cadastroCarta.fxml"));

            // Responsável por criar uma nova cena para o formulário de cadastro
            Scene scene = new Scene(root);

            // Responsável por criar um novo palco (janela) para exibir o formulário de cadastro
            Stage newStage = new Stage();
            newStage.setTitle("Cadastro");
            newStage.setScene(scene);

            // Responsável por exibir o novo formulário
            newStage.show();

            // Responsável por fechar o formulário de login
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace(); // Exibe erro no console caso falhe
            showAlert("Erro", "Erro ao carregar o formulário de cadastro.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String erro, String erro_ao_carregar_o_formulário_de_cadastro, Alert.AlertType alertType) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
