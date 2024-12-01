package com.mycompany.cardpro4.controller;

import dao.BinderDAO;
import dao.CartaDAO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Carta;
import model.Session;
import model.User;

public class CollectionController implements Initializable {

    private final BinderDAO binderDAO = new BinderDAO(); 
    private final CartaDAO cartaDAO = new CartaDAO();
    
    @FXML 
    private TextField txtCartaName;
    @FXML 
    private ComboBox<String> binderBox, temaBox, raridadeBox; 
    @FXML 
    private Label lblUserName;
    @FXML 
    private Button btnExcluirCarta, btnCadastrarCarta, btnFilterCards, btnTrocas, btnUser, btnResetCards;
    @FXML 
    private ScrollPane scrollPaneCartas;
    @FXML 
    private TilePane tilePaneCartas;
    
    private static final String ALL = "All";

    private static final String QUALQUER_UM = "Qualquer um";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            exibirNomeUsuario();
            carregarCartasUsuarioAtual(); 
            configurarComboBoxes();
            
            temaBox.setVisible(false);
            raridadeBox.setVisible(false);
            btnResetCards.setVisible(false);
        });
    }

    private void exibirNomeUsuario() {
        User user = Session.getUser();
        if (user != null) {
            lblUserName.setText("Bem-vindo, " + user.getName() + "!");
        } else {
            lblUserName.setText("Usuário não encontrado.");
        }
    }

    private void carregarCartasUsuarioAtual() {
        try {
            User user = Session.getUser();
            if (user == null) {
                mostrarAlerta(Alert.AlertType.ERROR, "Sessão não encontrada", "Nenhum usuário ativo na sessão.");
                return;
            }

            int idUsuario = user.getId();
            List<Carta> cartas = cartaDAO.buscarCartasPorUsuario(idUsuario);
            if (cartas.isEmpty()) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Nenhuma carta encontrada", "Adicione novas cartas no botão Adicionar Cartas.");
                return;
            }

            tilePaneCartas.getChildren().clear();
            tilePaneCartas.setHgap(20);
            tilePaneCartas.setVgap(10);

            for (Carta carta : cartas) {
                VBox cartaContainer = criarCartaContainer(carta);
                if (cartaContainer != null) {
                    tilePaneCartas.getChildren().add(cartaContainer);
                }
            }
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro ao carregar cartas", "Ocorreu um erro ao buscar as cartas: " + e.getMessage());
        }
    }

    private VBox criarCartaContainer(Carta carta) {
        if (carta.getImagem() != null) {
            ImageView imageView = criarImageView(carta.getImagem());

            String nomeCarta = carta.getNome();
            if (nomeCarta.length() > 10) {
                nomeCarta = nomeCarta.substring(0, 10) + "...";
            }

            Label lblNomeCarta = new Label(nomeCarta);
            lblNomeCarta.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-alignment: center;");

            VBox cartaContainer = new VBox(5);
            cartaContainer.getChildren().addAll(imageView, lblNomeCarta);
            cartaContainer.setStyle("-fx-alignment: center;");

            return cartaContainer;
        }
        return null;
    }

    private ImageView criarImageView(byte[] imagemBlob) {
        if (imagemBlob != null) {
            ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(imagemBlob)));
            imageView.setFitWidth(100);
            imageView.setFitHeight(150);
            imageView.setPreserveRatio(true);
            return imageView;
        }
        return null;
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    
    @FXML
    private void goToCadastroCarta(ActionEvent event) {
        abrirNovaTela("/fxml/cadastroCarta.fxml", "Cadastro");
    }

    @FXML
    private void goToExcluir(ActionEvent event) {
        abrirNovaTela("/fxml/excluirCarta.fxml", "Excluir");
    }
    
    @FXML
    private void goToTrocas(ActionEvent event) {
        abrirNovaTela("/fxml/trocas.fxml", "Trocas");
    }

    @FXML
    private void goToUsuario(ActionEvent event) {
        abrirNovaTela("/fxml/usuario.fxml", "Usuário");
    }

    private void abrirNovaTela(String fxml, String titulo) {
        try {
            Stage currentStage = (Stage) btnCadastrarCarta.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Scene scene = new Scene(root);
            Stage newStage = new Stage();
            newStage.setResizable(false);
            newStage.setTitle(titulo);
            newStage.setScene(scene);
	    newStage.centerOnScreen();
            newStage.show();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao carregar a nova tela.");
        }
    }

    @FXML
    private void mostrarCombos(ActionEvent event) {
        // Alternar visibilidade dos ComboBoxes
        boolean temaBoxVisivel = temaBox.isVisible();
        boolean raridadeBoxVisivel = raridadeBox.isVisible();
        boolean btnResetCardsVisivel = btnResetCards.isVisible();
	boolean binderBoxVisivel = binderBox.isVisible();

        btnResetCards.setVisible(!btnResetCardsVisivel);
        temaBox.setVisible(!temaBoxVisivel);
        raridadeBox.setVisible(!raridadeBoxVisivel);
	binderBox.setVisible(!binderBoxVisivel);
    }
    @FXML
    private void carregarColecoes() {
        User user = Session.getUser();
        if (user == null || user.getId() == 0) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Nenhum usuário está logado.");
            return;
        }

        List<String> binder = binderDAO.listarBinderPorUsuario(user.getId());
        binderBox.getItems().clear();
        binderBox.getItems().add(QUALQUER_UM);
        binderBox.getItems().addAll(binder);
    }

    private void configurarComboBoxes() {
        raridadeBox.getItems().addAll(
                QUALQUER_UM, "Comum", "Incomum", "Raro", "Raro Holográfico", "Ultra Raro", "Hiper Raro", "Secreto Raro"
        );

        temaBox.getItems().addAll(
                QUALQUER_UM, "Pokémon", "Yu-Gi-Oh!", "Magic: The Gathering", "Digimon", "Dragon Ball"
        );

        carregarColecoes();
    }
    @FXML
private void filtrarCartas() {
    try {
        User user = Session.getUser();
        if (user == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Nenhum usuário está logado.");
            return;
        }

        String nomeCarta = txtCartaName.getText().trim();
        String raridadeSelecionada = raridadeBox.getValue();
        String temaSelecionado = temaBox.getValue();
        String binderSelecionado = binderBox.getValue();  // Captura a seleção do binder

        // Ajustar valores para representar a seleção padrão (Qualquer um)
        if (raridadeSelecionada == null || ALL.equals(raridadeSelecionada)) {
            raridadeSelecionada = null;
        }
        if (temaSelecionado == null || ALL.equals(temaSelecionado)) {
            temaSelecionado = null;
        }
        if (ALL.equals(binderSelecionado)) {
            binderSelecionado = null;  // Considera "Qualquer um" como não filtro
        }

        // Buscar o ID do binder selecionado (se houver)
        Integer idBinder = null;
        if (binderSelecionado != null && !binderSelecionado.isEmpty()) {
            BinderDAO binderDAO = new BinderDAO();
            idBinder = binderDAO.buscarIdBinderPorNome(binderSelecionado, user.getId());
            if (idBinder == -1) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Binder não encontrado.");
                return;
            }
        }

        // Buscar cartas com base nos filtros
        List<Carta> cartasFiltradas = cartaDAO.buscarCartasFiltradas(user.getId(), nomeCarta, raridadeSelecionada, temaSelecionado, idBinder);

        if (cartasFiltradas.isEmpty()) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Nenhuma carta encontrada", "Nenhuma carta encontrada com os filtros selecionados.");
            carregarCartasUsuarioAtual(); // Recarrega todas as cartas do usuário
            return;
        }

        // Atualizar a exibição das cartas
        tilePaneCartas.getChildren().clear();
        for (Carta carta : cartasFiltradas) {
            VBox cartaContainer = criarCartaContainer(carta);
            if (cartaContainer != null) {
                tilePaneCartas.getChildren().add(cartaContainer);
            }
        }
    } catch (SQLException e) {
        mostrarAlerta(Alert.AlertType.ERROR, "Erro ao filtrar cartas", "Ocorreu um erro ao aplicar os filtros: " + e.getMessage());
    }
}
    

        @FXML
    private void resetCards(){
        carregarCartasUsuarioAtual();
	// Tentativa de apagar o que está na caixa de pesquisa ao clicar no "Reset"
	txtCartaName.clear();
    }
    
}
