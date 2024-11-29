package model;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Session {

    private static final String FILE_PATH = "user_session.json";  // Arquivo JSON para salvar os dados do usuário

    
    // Salva os dados do usuário em um arquivo JSON
    public static void setUser(int id, String name, String email) {
        User user = new User(id, name, email, "", "");  // Considerando que o telefone e a senha não são necessários neste momento
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write(userJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Obtém os dados do usuário do arquivo JSON
    public static User getUser() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(FILE_PATH)) {
            return gson.fromJson(reader, User.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;  // Retorna null se o arquivo não existir ou houver um erro
        }
    }

    // Limpa os dados da sessão (apaga o arquivo)
    public static void clearSession() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
    }
}
