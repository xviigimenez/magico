package dao;

import model.Carta;
import interfaces.DAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartaDAO implements DAO<Carta> {
    private static final String URL = "jdbc:sqlite:CardProData.db";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

@Override
public void inserir(Carta carta) {
    String sql = "INSERT INTO cartas (nome, raridade, tema, id_user, id_colecao, imagem) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        stmt.setString(1, carta.getNome());
        stmt.setString(2, carta.getRaridade());
        stmt.setString(3, carta.getTema());
        stmt.setInt(4, carta.getIdUser()); // ID do usuário
        if (carta.getIdColecao() == null || carta.getIdColecao().equals("Nenhuma")) {
            stmt.setNull(5, Types.VARCHAR);
        } else {
            stmt.setString(5, carta.getIdColecao());
        }
        stmt.setBytes(6, carta.getImagem());

        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                carta.setId(rs.getInt(1));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

@Override
public void alterar(Carta carta) {
    String sql = "UPDATE cartas SET nome = ?, raridade = ?, tema = ?, id_colecao = ?, imagem = ? WHERE id = ?";

    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, carta.getNome());
        stmt.setString(2, carta.getRaridade());
        stmt.setString(3, carta.getTema());
        if (carta.getIdColecao() == null || carta.getIdColecao().equals("Nenhuma")) {
            stmt.setNull(4, Types.VARCHAR);
        } else {
            stmt.setString(4, carta.getIdColecao());
        }
        stmt.setBytes(5, carta.getImagem());
        stmt.setInt(6, carta.getId());

        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    @Override
    public void excluir(int id) {
        String sql = "DELETE FROM cartas WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

@Override
public Carta consultar(int id) {
    String sql = "SELECT * FROM cartas WHERE id = ?";
    Carta carta = null;

    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            carta = new Carta(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("raridade"),
                rs.getString("tema"),
                rs.getString("id_colecao"), // Pode ser null
                rs.getBytes("imagem")
            );
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return carta;
}

    @Override
    public List<Carta> listar() {
        String sql = "SELECT * FROM cartas";
        List<Carta> cartas = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartas;
    }
}
