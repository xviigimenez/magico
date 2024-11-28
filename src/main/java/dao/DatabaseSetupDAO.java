package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseSetupDAO {

    private static final String DATABASE_URL = "jdbc:sqlite:CardProData.db"; // Banco de dados renomeado para CardProData

    public void createDatabase() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            if (connection != null) {
                System.out.println("Conexão com o banco de dados estabelecida.");

                // Chama o método para criar as tabelas
                createTables(connection);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Exibe erro no console caso falhe
        }
    }

    private void createTables(Connection connection) {
        // SQL para criar a tabela de usuários
        String createUsersTableSQL = "CREATE TABLE IF NOT EXISTS usuarios (" +
                                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                     "name TEXT NOT NULL," +
                                     "password TEXT NOT NULL," +
                                     "tel TEXT NOT NULL," +
                                     "email TEXT UNIQUE NOT NULL" +
                                     ");";

        // SQL para criar a tabela de coleções(Binder)
        String createBinderTableSQL = "CREATE TABLE IF NOT EXISTS binder (" +
                                           "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                           "nome TEXT NOT NULL," +
                                           "descricao TEXT NOT NULL," +
                                           "id_user INTEGER NOT NULL," +
                                           "FOREIGN KEY (id_user) REFERENCES usuarios(id) ON DELETE CASCADE" +
                                           ");";

        // SQL para criar a tabela de cartas
        String createCardsTableSQL = "CREATE TABLE IF NOT EXISTS cartas (" +
                             "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                             "nome TEXT NOT NULL," +
                             "raridade TEXT NOT NULL," +
                             "tema TEXT NOT NULL," +
                             "id_user INTEGER NOT NULL," +
                             "id_colecao INTEGER," +
                             "imagem BLOB, " + // Coluna para armazenar a imagem
                             "FOREIGN KEY (id_user) REFERENCES usuarios(id) ON DELETE CASCADE," +
                             "FOREIGN KEY (id_colecao) REFERENCES colecoes(id) ON DELETE SET NULL" +
                             ");";

                // SQL para criar a tabela de trocas
        String createTrocasTableSQL = "CREATE TABLE IF NOT EXISTS trocas (" +
                                          "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                          "id_carta1 INTEGER NOT NULL," +
                                          "id_carta2 INTEGER," +
                                          "id_user1 INTEGER NOT NULL," +
                                          "id_user2 INTEGER," +
                                          "raridadeMin TEXT NOT NULL," +
                                          "concluido BOOLEAN DEFAULT FALSE," +
                                          "description TEXT NOT NULL," +
                                          "isCartaOferecida BOOLEAN DEFAULT FALSE," +
                                          "FOREIGN KEY (id_carta1) REFERENCES cartas(id)," +
                                          "FOREIGN KEY (id_carta2) REFERENCES cartas(id)," +
                                          "FOREIGN KEY (id_user1) REFERENCES usuarios(id)," +
                                          "FOREIGN KEY (id_user2) REFERENCES usuarios(id)" +
                                          ");";


        try (Statement stmt = connection.createStatement()) {
            // Cria a tabela de usuários
            stmt.execute(createUsersTableSQL);
            System.out.println("Tabela de usuários criada (se não existia).");

            // Cria a tabela de coleções
            stmt.execute(createBinderTableSQL);
            System.out.println("Tabela de coleções criada (se não existia).");

            // Cria a tabela de cartas
            stmt.execute(createCardsTableSQL);
            System.out.println("Tabela de cartas criada (se não existia).");
            
            
             // Cria a tabela de trocas
            stmt.execute(createTrocasTableSQL);
            System.out.println("Tabela de trocas criada (se não existia).");
        } catch (SQLException e) {
            e.printStackTrace(); // Exibe erro no console caso falhe
        }
    }
}
