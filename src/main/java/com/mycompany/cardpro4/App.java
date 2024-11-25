package com.mycompany.cardpro4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import dao.DatabaseSetupDAO;
import java.io.IOException;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // Configurar o banco de dados
        DatabaseSetupDAO dbSetup = new DatabaseSetupDAO();
        dbSetup.createDatabase(); // Chama a função para criar o banco de dados

        scene = new Scene(loadFXML("login"));
        stage.setScene(scene);
	// Adiciona um título para a primeira janela
	stage.setTitle("Login");
	// Centraliza a janela
	stage.centerOnScreen();
	// Não deixa o usuário redimensionar a janela
        stage.setResizable(false);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
