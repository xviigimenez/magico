package dao;

import interfaces.DAO;
import model.Trocas;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrocasDAO implements DAO<Trocas> {

    private static final String DATABASE_URL = "jdbc:sqlite:CardProData.db";

    @Override
    public void inserir(Trocas trocas) {
        String sql = "INSERT INTO trocas (id_carta1, id_carta2, id_user1, id_user2, raridadeMin, concluido, description, isCartaOferecida) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, trocas.getIdCarta1());
            stmt.setObject(2, trocas.getIdCarta2(), Types.INTEGER);
            stmt.setInt(3, trocas.getIdUser1());
            stmt.setObject(4, trocas.getIdUser2(), Types.INTEGER);
            stmt.setString(5, trocas.getRaridadeMin());
            stmt.setBoolean(6, trocas.isConcluido());
            stmt.setString(7, trocas.getDescription());
            stmt.setBoolean(8, trocas.isCartaOferecida());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void alterar(Trocas trocas) {
        String sql = "UPDATE trocas SET id_carta1 = ?, id_carta2 = ?, id_user1 = ?, id_user2 = ?, " +
                     "raridadeMin = ?, concluido = ?, description = ?, isCartaOferecida = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, trocas.getIdCarta1());
            stmt.setObject(2, trocas.getIdCarta2(), Types.INTEGER);
            stmt.setInt(3, trocas.getIdUser1());
            stmt.setObject(4, trocas.getIdUser2(), Types.INTEGER);
            stmt.setString(5, trocas.getRaridadeMin());
            stmt.setBoolean(6, trocas.isConcluido());
            stmt.setString(7, trocas.getDescription());
            stmt.setBoolean(8, trocas.isCartaOferecida());
            stmt.setInt(9, trocas.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void excluir(int id) {
        String sql = "DELETE FROM trocas WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Trocas consultar(int id) {
        String sql = "SELECT * FROM trocas WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Trocas(
                            rs.getInt("id"),
                            rs.getInt("id_carta1"),
                            (Integer) rs.getObject("id_carta2"),
                            rs.getInt("id_user1"),
                            (Integer) rs.getObject("id_user2"),
                            rs.getString("raridadeMin"),
                            rs.getBoolean("concluido"),
                            rs.getString("description"),
                            rs.getBoolean("isCartaOferecida")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Trocas> listar() {
        List<Trocas> trocasList = new ArrayList<>();
        String sql = "SELECT * FROM trocas";
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Trocas troca = new Trocas(
                        rs.getInt("id"),
                        rs.getInt("id_carta1"),
                        (Integer) rs.getObject("id_carta2"),
                        rs.getInt("id_user1"),
                        (Integer) rs.getObject("id_user2"),
                        rs.getString("raridadeMin"),
                        rs.getBoolean("concluido"),
                        rs.getString("description"),
                        rs.getBoolean("isCartaOferecida")
                );
                trocasList.add(troca);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trocasList;
    }
    
    public List<Trocas> listarPorUsuario(int idUser) {
    List<Trocas> trocasList = new ArrayList<>();
    String sql = "SELECT * FROM trocas WHERE id_user1 = ?";
    try (Connection conn = DriverManager.getConnection(DATABASE_URL);
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, idUser);  // Passa o id do usuário para o SQL
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Trocas troca = new Trocas(
                        rs.getInt("id"),
                        rs.getInt("id_carta1"),
                        (Integer) rs.getObject("id_carta2"),
                        rs.getInt("id_user1"),
                        (Integer) rs.getObject("id_user2"),
                        rs.getString("raridadeMin"),
                        rs.getBoolean("concluido"),
                        rs.getString("description"),
                        rs.getBoolean("isCartaOferecida")
                );
                trocasList.add(troca);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return trocasList;
}
    
    public void excluir(Trocas troca) throws SQLException {
    String sql = "DELETE FROM trocas WHERE id = ?";
    
    try (Connection conn = DriverManager.getConnection(DATABASE_URL);
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, troca.getId()); // Passa o ID da troca a ser excluída
        stmt.executeUpdate();  // Executa a exclusão
    }
}
}
