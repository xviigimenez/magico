package com.mycompany.cardpro4.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dao.TrocaDAO;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import model.Carta;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class TrocasController {

    @FXML
    private Button btnRefresh;

    @FXML
    private ComboBox<String> raridadeBox1, raridadeBox2;

    @FXML
    private ComboBox<String> temaBox1, temaBox2;

    @FXML
    private ComboBox<String> cartaBox1;

    @FXML
    private TextField nomeField;

    private final TrocaDAO trocasDAO = new TrocaDAO();

    // Método para obter o ID do usuário a partir do JSON
    private int getUserIdFromSession() {
    try (FileReader reader = new FileReader("user_session.json")) {
        JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
        int userId = jsonObject.get("id").getAsInt();
        System.out.println("ID do Usuário obtido: " + userId); // Log para depuração
        return userId;
    } catch (IOException e) {
        e.printStackTrace();
        return -1; // Retorna -1 se ocorrer um erro ao ler o JSON
    }
    
}


    @FXML
    public void initialize() {
        // Inicializa os valores dos ComboBox
        raridadeBox1.getItems().addAll(
                "Comum",
                "Incomum",
                "Raro",
                "Raro Holográfico",
                "Ultra Raro",
                "Hiper Raro",
                "Secreto Raro"
        );
        temaBox1.getItems().addAll(
                "Pokémon",
                "Yu-Gi-Oh!",
                "Magic: The Gathering",
                "Digimon",
                "Dragon Ball"
        );

        raridadeBox2.getItems().addAll(
                "Comum",
                "Incomum",
                "Raro",
                "Raro Holográfico",
                "Ultra Raro",
                "Hiper Raro",
                "Secreto Raro"
        );
        temaBox2.getItems().addAll(
                "Pokémon",
                "Yu-Gi-Oh!",
                "Magic: The Gathering",
                "Digimon",
                "Dragon Ball"
        );
    }

    private void atualizarCartaBox() {
        String raridade = raridadeBox1.getValue();
        String tema = temaBox1.getValue();
        int idUsuario = getUserIdFromSession();  // Obtém o ID do usuário do JSON

        if (raridade != null && tema != null && idUsuario != -1) {
            try {
                List<Carta> cartas = trocasDAO.buscarCartasFiltradas(raridade, tema, idUsuario);
                cartaBox1.getItems().clear();
                for (Carta carta : cartas) {
                    cartaBox1.getItems().add(carta.getNome());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleRefreshButton() {
        atualizarCartaBox();
    }
}
