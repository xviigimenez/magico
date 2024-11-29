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
import model.Carta;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import com.google.gson.Gson;
import java.util.Map;
import dao.CartaDAO;
import model.Carta;
import model.Session;
import model.User;

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
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarCartas();
    }    

    @FXML
    private void goToTrocas(ActionEvent event) {
    }

}
