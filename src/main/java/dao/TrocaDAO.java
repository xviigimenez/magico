package dao;

import model.Carta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrocaDAO {
    private static final String URL = "jdbc:sqlite:CardProData.db";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public List<Carta> buscarCartasFiltradas(String raridade, String tema, int id_user) throws SQLException {
        String sql = "SELECT * FROM cartas WHERE raridade = ? AND tema = ? AND id_user = ?";
        List<Carta> cartas = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, raridade);
            stmt.setString(2, tema);
            stmt.setInt(3, id_user);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Carta carta = new Carta(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("raridade"),
                        rs.getString("tema"),
                        rs.getString("id_colecao"),
                        rs.getBytes("imagem")
                );
                cartas.add(carta);
            }
        }

        return cartas;
    }
}
