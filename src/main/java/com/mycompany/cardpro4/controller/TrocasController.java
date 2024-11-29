//Maua Version

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

    // Tabela para todas as trocas (exceto do usuário logado)
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
        // Instancia o DAO
        trocasDAO = new TrocasDAO();

        // Configura as colunas da tabela de trocas
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colCarta1.setCellValueFactory(cellData -> cellData.getValue().idCarta1Property().asObject());
        colCarta2.setCellValueFactory(cellData -> cellData.getValue().idCarta2Property().asObject());
        colUser1.setCellValueFactory(cellData -> cellData.getValue().idUser1Property().asObject());
        colUser2.setCellValueFactory(cellData -> cellData.getValue().idUser2Property().asObject());
        colRaridade.setCellValueFactory(cellData -> cellData.getValue().raridadeMinProperty());
        colConcluido.setCellValueFactory(cellData -> cellData.getValue().concluidoProperty().asObject());
        colDescription.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        
        
        // Configura as colunas da tabela de trocas
        colIdTodos.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colCarta1Todos.setCellValueFactory(cellData -> cellData.getValue().idCarta1Property().asObject());
        colCarta2Todos.setCellValueFactory(cellData -> cellData.getValue().idCarta2Property().asObject());
        colUser1Todos.setCellValueFactory(cellData -> cellData.getValue().idUser1Property().asObject());
        colUser2Todos.setCellValueFactory(cellData -> cellData.getValue().idUser2Property().asObject());
        colRaridadeTodos.setCellValueFactory(cellData -> cellData.getValue().raridadeMinProperty());
        colConcluidoTodos.setCellValueFactory(cellData -> cellData.getValue().concluidoProperty().asObject());
        colDescriptionTodos.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        // Carrega as trocas do usuário logado
        carregarMinhasTrocas();

        // Associa o método de exclusão ao botão
        btnExcluirTroca.setOnAction(this::excluirTroca);

        // Carrega todas as trocas na segunda tabela (sem o filtro do usuário logado)
        carregarTrocas();
    }

    private void excluirTroca(ActionEvent event) {
        // Obtém o item selecionado na tabela
        Trocas trocaSelecionada = tblTrocas.getSelectionModel().getSelectedItem();

        if (trocaSelecionada != null) {
            try {
                // Exclui a troca selecionada
                trocasDAO.excluir(trocaSelecionada);

                // Exibe uma mensagem de sucesso
                showAlert("Sucesso", "Troca excluída com sucesso!", Alert.AlertType.INFORMATION);

                // Atualiza a tabela após a exclusão
                carregarMinhasTrocas();
                
                carregarTrocas(); // Atualiza todas as trocas (tabela geral)
            } catch (SQLException e) {
                showAlert("Erro", "Erro ao excluir a troca: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Erro", "Selecione uma troca para excluir.", Alert.AlertType.WARNING);
        }
    }

    // Método para carregar as trocas do usuário logado no TableView
    private void carregarMinhasTrocas() {
        User user = Session.getUser();
        if (user != null) {
            // Carrega as trocas do usuário logado
            List<Trocas> trocasList = trocasDAO.listarPorUsuario(user.getId());
            ObservableList<Trocas> observableTrocas = FXCollections.observableArrayList(trocasList);
            tblTrocas.setItems(observableTrocas);
        } else {
            showAlert("Erro", "Nenhum usuário logado.", Alert.AlertType.WARNING);
        }
    }

    // Método para carregar todas as trocas no TableView
    private void carregarTrocas() {
        // Carrega todas as trocas
        List<Trocas> trocasList = trocasDAO.listar();
        if (trocasList.isEmpty()) {
            showAlert("Aviso", "Nenhuma troca encontrada.", Alert.AlertType.INFORMATION);
        }
        // Mapeia as trocas para o formato ObservableList
        ObservableList<Trocas> observableTrocas = FXCollections.observableArrayList(trocasList);
        tblTodasTrocas.setItems(observableTrocas);  // Atribui os dados à tabela
   }
    @FXML
    private void goToPropostaTrocas(ActionEvent event) {
        if (tblTodasTrocas.getSelectionModel().getSelectedItem() == null) {
        // Mostra um alerta caso nenhum item esteja selecionado
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

    
    
    
    
    //   -----------------------------------     BOTÕES     ---------------------------------------------
    
    
    
    
    
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
                showAlert("Erro", "Erro ao carregar o Menu .", Alert.AlertType.ERROR);
            }
    
    }
    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
