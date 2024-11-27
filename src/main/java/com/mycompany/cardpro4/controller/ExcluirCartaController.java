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
import javafx.scene.control.ButtonType;
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
            // Carrega a tela principal
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/collection.fxml"));

            // Configura a nova cena
            Scene scene = new Scene(root);

            // Obtém o estágio atual
            Stage stage = (Stage) btnBack.getScene().getWindow();

            // Configura o palco com a nova cena
            stage.setScene(scene);

	    // Centraliza a janela
	    stage.centerOnScreen();

            // Exibe a nova tela
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

@FXML
private void excluir(ActionEvent event) {
    // Obtém o tipo e o item selecionado diretamente
    String tipoSelecionado = tipoBox.getValue();
    String itemSelecionado = listaBox.getValue();

    // Validação inicial
    if (tipoSelecionado == null || itemSelecionado == null) {
        showAlert("Erro", "Selecione um tipo e um item para excluir.", Alert.AlertType.ERROR);
        return;
    }

    // Confirmação da exclusão
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirmação de Exclusão");
    alert.setHeaderText("Deseja realmente excluir?");
    alert.setContentText("Esta ação não poderá ser desfeita.");
    
    // Ação somente se o usuário confirmar
    alert.showAndWait().ifPresent(response -> {
        if (response == ButtonType.OK) {
            try {
                if ("Binder".equals(tipoSelecionado)) {
                    excluirBinder(itemSelecionado);
                } else if ("Carta".equals(tipoSelecionado)) {
                    excluirCarta(itemSelecionado);
                }
                showAlert("Sucesso", "Exclusão realizada com sucesso.", Alert.AlertType.INFORMATION);
                listaBox.getItems().remove(itemSelecionado); // Atualiza a ComboBox
            } catch (SQLException e) {
                showAlert("Erro", "Erro ao excluir o item: " + e.getMessage(), Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    });
}

private void excluirBinder(String nomeBinder) throws SQLException {
    BinderDAO binderDAO = new BinderDAO();
    Binder binder = binderDAO.buscarBinderPorNome(nomeBinder, Session.getUser().getId());

    if (binder == null) {
        showAlert("Erro", "Binder não encontrado.", Alert.AlertType.ERROR);
        return;
    }

    // Excluir cartas associadas e o Binder
    CartaDAO cartaDAO = new CartaDAO();
    cartaDAO.excluirCartasPorBinder(binder.getId());
    binderDAO.excluir(binder.getId());
    System.out.println("Binder excluído com sucesso!");
}

private void excluirCarta(String nomeCarta) throws SQLException {
    CartaDAO cartaDAO = new CartaDAO();
    List<Carta> cartas = cartaDAO.buscarCartasPorUsuario(Session.getUser().getId());

    // Encontrar a carta pelo nome e excluir
    Carta cartaParaExcluir = cartas.stream()
                                   .filter(carta -> carta.getNome().equals(nomeCarta))
                                   .findFirst()
                                   .orElse(null);

    if (cartaParaExcluir == null) {
        showAlert("Erro", "Carta não encontrada.", Alert.AlertType.ERROR);
        return;
    }

    cartaDAO.excluir(cartaParaExcluir.getId());
    System.out.println("Carta excluída com sucesso!");
}



    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
