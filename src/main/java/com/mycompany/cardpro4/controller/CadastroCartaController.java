package com.mycompany.cardpro4.controller;

import dao.CartaDAO;
import dao.BinderDAO;  // Importe o BinderDAO
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import model.Carta;
import model.Session;
import model.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CadastroCartaController {

    private static final String SESSION_FILE = "user_session";
    private final BinderDAO binderDAO = new BinderDAO(); // Instância do BinderDAO
    private final CartaDAO cartaDAO = new CartaDAO(); // Instância do CartaDAO

    @FXML
    private TextField nomeField;

    @FXML
    private ComboBox<String> raridadeBox;

    @FXML
    private ComboBox<String> temaBox;

    @FXML
    private ComboBox<String> colecaoBox;

    @FXML
    private Button selectImageButton;

    @FXML
    private ImageView imagePreview;

    private File selectedImage;
    @FXML
    private Button addCollectionButton;
    @FXML
    private Button btnBack;
    @FXML
    private Button addButton;

    public void initialize() {
        configurarComboBoxes();
        configurarBotoes();
        carregarColecoes(); // Carrega as coleções do usuário
    }

    private void configurarComboBoxes() {
        raridadeBox.getItems().addAll(
                "Qualquer um",
                "Comum", 
                "Incomum", 
                "Raro", 
                "Raro Holográfico", 
                "Ultra Raro", 
                "Hiper Raro", 
                "Secreto Raro"
        );

        temaBox.getItems().addAll(
                "Qualquer um",
                "Pokémon", 
                "Yu-Gi-Oh!", 
                "Magic: The Gathering", 
                "Digimon", 
                "Dragon Ball"
        );
    }

    private void configurarBotoes() {
        selectImageButton.setOnAction(event -> selecionarImagem());
    }

    private void selecionarImagem() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecione uma Imagem");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imagens", "*.jpg", "*.png", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(selectImageButton.getScene().getWindow());
        if (file != null) {
            selectedImage = file;
            imagePreview.setImage(new Image(file.toURI().toString()));
        }
    }

    private void carregarColecoes() {
        User user = Session.getUser();
        if (user == null || user.getId() == 0) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Nenhum usuário está logado.");
            return;
        }

        List<String> binder = binderDAO.listarBinderPorUsuario(user.getId());
        colecaoBox.getItems().clear();
        colecaoBox.getItems().add("Nenhum");
        colecaoBox.getItems().addAll(binder); // Adiciona os nomes dos Binders do banco
    }

    @FXML
    private void cadastrarCarta() {
    // Recupera o usuário da sessão
    User user = Session.getUser();
    if (user == null || user.getId() == 0) {
        exibirAlerta(Alert.AlertType.ERROR, "Erro", "Nenhum usuário está logado.");
        return;
    }

    int idUser = user.getId(); // Obtém o ID do usuário
    String nome = nomeField.getText();
    Optional<String> raridade = Optional.ofNullable(raridadeBox.getValue());
    Optional<String> tema = Optional.ofNullable(temaBox.getValue());
    Optional<String> colecao = Optional.ofNullable(colecaoBox.getValue());

    
//    FUNCAO GENERALISTA DE COMPROVACAO DE PREENCHIMENTO, FOI SUBSTITUIDA PELA ESTRUTURA LOGICA ABAIXO!
//    if (nome.isEmpty() || raridade.isEmpty() || tema.isEmpty() || colecao.isEmpty() || selectedImage == null) {
//        exibirAlerta(Alert.AlertType.ERROR, "Erro", "Preencha todos os campos!");
//        return;
//    }

// Responsavel por conferir e avisar sobre o preenchimento de cada campo do formulario
    if(nome.isEmpty()){
        exibirAlerta(Alert.AlertType.WARNING, "Erro", "Preencha o campo nome!");
        return;
    } else if(raridade.isEmpty()){
        exibirAlerta(Alert.AlertType.WARNING, "Erro", "Preencha o campo raridade!");
        return;
    } else if(tema.isEmpty()){
        exibirAlerta(Alert.AlertType.WARNING, "Erro", "Preencha o campo tema!");
        return;
    } else if(colecao.isEmpty()){
        exibirAlerta(Alert.AlertType.WARNING, "Erro", "Preencha o campo Binder!");
        return;
    } else if(selectedImage == null){
        exibirAlerta(Alert.AlertType.WARNING, "Erro", "Selecione uma imagem!");
        return;               
    }
  
    

    try (FileInputStream imageStream = new FileInputStream(selectedImage)) {
        // Cria a nova carta com os dados fornecidos
        String nomeColecao = colecao.get();
        String idColecao = null;

        // Se não for "Nenhum", buscamos o id da coleção usando BinderDAO
        if (!nomeColecao.equals("Nenhum")) {
            idColecao = binderDAO.buscarIdColecaoPorNome(nomeColecao);  // Chama o método do BinderDAO
        }

        // Cria o objeto Carta com o id da coleção obtido
        Carta carta = new Carta(
                nome,
                raridade.get(),
                tema.get(),
                idColecao,  // Agora estamos salvando o ID da coleção, e não o nome
                imageStream.readAllBytes()
        );

        // Define o ID do usuário antes de salvar
        carta.setIdUser(idUser);

        cartaDAO.inserir(carta); // Insere a carta no banco
        exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Carta cadastrada com sucesso!");
        limparCampos();

    } catch (IOException e) {
        e.printStackTrace();
        exibirAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao processar a imagem: " + e.getMessage());
    } catch (Exception e) {
        e.printStackTrace();
        exibirAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao cadastrar carta: " + e.getMessage());
    }
}


    private void exibirAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void limparCampos() {
        nomeField.clear();
        raridadeBox.setValue(null);
        temaBox.setValue(null);
        colecaoBox.setValue(null);
        imagePreview.setImage(null);
        selectedImage = null;
    }

    @FXML
    private void goToCadastroBinder(ActionEvent event) {
        try {
                // Responsável por carregar a tela principal
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/cadastroBinder.fxml"));

                // Responsável por configurar a nova cena
                Scene scene = new Scene(root);

                // Responsável por obter o estágio atual
                Stage stage = (Stage) addCollectionButton.getScene().getWindow();

                // Responsável por configurar o palco com a cena principal
                stage.setScene(scene);

	    	// Centraliza a janela principal
	    	stage.centerOnScreen();

                // Responsável por exibir a nova tela
                stage.show();
            } catch (IOException e) {
                showAlert("Erro", "Erro ao carregar a tela de criacao de Binder.", Alert.AlertType.ERROR);
            }
    }

    private void showAlert(String erro, String erro_ao_carregar_a_tela_de_criacao_de_Bin, Alert.AlertType alertType) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @FXML
    private void goToCollection(ActionEvent event) {
        
                try {
                // Responsável por carregar a tela principal
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/collection.fxml"));

                // Responsável por configurar a nova cena
                Scene scene = new Scene(root);

                // Responsável por obter o estágio atual
                Stage stage = (Stage) addCollectionButton.getScene().getWindow();

                // Responsável por configurar o palco com a cena principal
                stage.setScene(scene);

	    	// Centraliza a janela principal
	    	stage.centerOnScreen();

                // Responsável por exibir a nova tela
                stage.show();
            } catch (IOException e) {
                showAlert("Erro", "Erro ao carregar o Menu .", Alert.AlertType.ERROR);
            }
    
    }
    
    
}
