package com.mycompany.cardpro4.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dao.TrocasDAO;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import model.Session;
import model.Trocas;
import model.Contexto;
import model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class TrocasController {

    @FXML
    private TableView<Trocas> tblTrocas;
    @FXML
    private TableColumn<Trocas, Integer> colId;
    @FXML
    private TableColumn<Trocas, Integer> colCarta1;
    @FXML
    private TableColumn<Trocas, Integer> colCarta2;
    @FXML
    private TableColumn<Trocas, Integer> colUser1;
    @FXML
    private TableColumn<Trocas, Integer> colUser2;
    @FXML
    private TableColumn<Trocas, String> colRaridade;
    @FXML
    private TableColumn<Trocas, Boolean> colConcluido;
    @FXML
    private TableColumn<Trocas, String> colDescription;
    @FXML
    private Button btnCollection;

    private TrocasDAO trocasDAO;

    @FXML
    private TableView<Trocas> tblTodasTrocas;
    @FXML
    private TableColumn<Trocas, Integer> colIdTodos;
    @FXML
    private TableColumn<Trocas, Integer> colCarta1Todos;
    @FXML
    private TableColumn<Trocas, Integer> colCarta2Todos;
    @FXML
    private TableColumn<Trocas, Integer> colUser1Todos;
    @FXML
    private TableColumn<Trocas, Integer> colUser2Todos;
    @FXML
    private TableColumn<Trocas, String> colRaridadeTodos;
    @FXML
    private TableColumn<Trocas, Boolean> colConcluidoTodos;
    @FXML
    private TableColumn<Trocas, String> colDescriptionTodos;

    @FXML
    private Label lblUserName;
    @FXML
    private Button btnCadastrarTroca;
    @FXML
    private Button btnExcluirTroca;
    @FXML
    private Button btnFazerProposta;
    @FXML
    private Button btnUser;
    @FXML
    private Button btnTrocas;

    public void initialize() {
        trocasDAO = new TrocasDAO();

        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colCarta1.setCellValueFactory(cellData -> cellData.getValue().idCarta1Property().asObject());
        colCarta2.setCellValueFactory(cellData -> cellData.getValue().idCarta2Property().asObject());
        colUser1.setCellValueFactory(cellData -> cellData.getValue().idUser1Property().asObject());
        colUser2.setCellValueFactory(cellData -> cellData.getValue().idUser2Property().asObject());
        colRaridade.setCellValueFactory(cellData -> cellData.getValue().raridadeMinProperty());
        colConcluido.setCellValueFactory(cellData -> cellData.getValue().concluidoProperty().asObject());
        colDescription.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        
        colIdTodos.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colCarta1Todos.setCellValueFactory(cellData -> cellData.getValue().idCarta1Property().asObject());
        colCarta2Todos.setCellValueFactory(cellData -> cellData.getValue().idCarta2Property().asObject());
        colUser1Todos.setCellValueFactory(cellData -> cellData.getValue().idUser1Property().asObject());
        colUser2Todos.setCellValueFactory(cellData -> cellData.getValue().idUser2Property().asObject());
        colRaridadeTodos.setCellValueFactory(cellData -> cellData.getValue().raridadeMinProperty());
        colConcluidoTodos.setCellValueFactory(cellData -> cellData.getValue().concluidoProperty().asObject());
        colDescriptionTodos.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        carregarMinhasTrocas();
        btnExcluirTroca.setOnAction(this::excluirTroca);
        carregarTrocas();
    }

    private void excluirTroca(ActionEvent event) {
        Trocas trocaSelecionada = tblTrocas.getSelectionModel().getSelectedItem();

        if (trocaSelecionada != null) {
            try {
                trocasDAO.excluir(trocaSelecionada);
                showAlert("Sucesso", "Troca excluída com sucesso!", Alert.AlertType.INFORMATION);
                carregarMinhasTrocas();
                carregarTrocas();
            } catch (SQLException e) {
                showAlert("Erro", "Erro ao excluir a troca: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Erro", "Selecione uma troca para excluir.", Alert.AlertType.WARNING);
        }
    }

    private void carregarMinhasTrocas() {
        User user = Session.getUser();
        if (user != null) {
            List<Trocas> trocasList = trocasDAO.listarPorUsuario(user.getId());
            List<Trocas> trocasNaoConcluidas = trocasList.stream()
                    .filter(troca -> !troca.isConcluido())
                    .collect(Collectors.toList());
            ObservableList<Trocas> observableTrocas = FXCollections.observableArrayList(trocasNaoConcluidas);
            tblTrocas.setItems(observableTrocas);
        } else {
            showAlert("Erro", "Nenhum usuário logado.", Alert.AlertType.WARNING);
        }
    }

    private void carregarTrocas() {
        List<Trocas> trocasList = trocasDAO.listar();
        List<Trocas> trocasNaoConcluidas = trocasList.stream()
                .filter(troca -> !troca.isConcluido())
                .collect(Collectors.toList());

        if (trocasNaoConcluidas.isEmpty()) {
            showAlert("Aviso", "Nenhuma troca encontrada.", Alert.AlertType.INFORMATION);
        }

        ObservableList<Trocas> observableTrocas = FXCollections.observableArrayList(trocasNaoConcluidas);
        tblTodasTrocas.setItems(observableTrocas);
    }

    @FXML
    private void handleNegarTroca() {
        if (tblTrocas.getSelectionModel().getSelectedItem() == null) {
            showAlert("Atenção", "Nenhuma troca selecionada!", Alert.AlertType.WARNING);
            return;
        }
        Trocas trocaSelecionada = tblTrocas.getSelectionModel().getSelectedItem();
        trocaSelecionada.setConcluido(true);
        try {
            trocasDAO.atualizar(trocaSelecionada);
            carregarMinhasTrocas();
            carregarTrocas();
            showAlert("Sucesso", "Troca negada com sucesso!", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            showAlert("Erro", "Erro ao negar a troca: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void goToPropostaTrocas(ActionEvent event) {
        if (tblTodasTrocas.getSelectionModel().getSelectedItem() == null) {
            showAlert("Atenção", "Por favor, selecione uma troca antes de fazer uma proposta.", Alert.AlertType.WARNING);
            return;
        }
        Trocas trocaSelecionada = tblTodasTrocas.getSelectionModel().getSelectedItem();
        Contexto.setIdTrocaSelecionada(trocaSelecionada.getId());
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/propostaTroca.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnFazerProposta.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            showAlert("Erro", "Erro ao carregar a tela de cadastro de trocas.", AlertType.ERROR);
        }
    }

    @FXML
    private void goToCadastroTrocas(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/cadastroTrocas.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnCadastrarTroca.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            showAlert("Erro", "Erro ao carregar a tela de cadastro de trocas.", AlertType.ERROR);
        }
    }

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
    private void goToPerfil(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/perfil.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnUser.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            showAlert("Erro", "Erro ao carregar a tela de perfil.", Alert.AlertType.ERROR);
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

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
