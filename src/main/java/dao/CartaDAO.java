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
    
    //novo metodo de listar cartas!
    public List<Carta> buscarCartasPorUsuario(int idUsuario) throws SQLException {
    List<Carta> cartas = new ArrayList<>();
    String query = "SELECT * FROM cartas WHERE id_user = ? LIMIT 20"; // Limita para 20 cartas

    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setInt(1, idUsuario);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Carta carta = new Carta(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("raridade"),
                rs.getString("tema"),
                rs.getString("id_colecao"),
                rs.getBytes("imagem"),
                rs.getInt("id_user")
            );
            cartas.add(carta);
        }
    }
    return cartas;
}
    public void atualizarUserCarta(int id, int idUser) throws SQLException {
    String query = "UPDATE cartas SET id_user = ? WHERE id = ?";
    try (Connection conn = getConnection(); 
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, idUser);
        stmt.setInt(2, id);
        stmt.executeUpdate();
    }
}
    
    
//    Antigo buscarCartasFiltradas sem a consulta de binder
// public List<Carta> buscarCartasFiltradas(int idUsuario, String nome, String raridade, String tema) throws SQLException {
//    List<Carta> cartas = new ArrayList<>();
//    StringBuilder query = new StringBuilder("SELECT * FROM cartas WHERE id_user = ?");
//
//    // Adicionar filtros dinamicamente
//    if (nome != null && !nome.isEmpty()) {
//        query.append(" AND LOWER(nome) LIKE LOWER(?)");
//    }
//    if (raridade != null) {
//        query.append(" AND raridade = ?");
//    }
//    if (tema != null) {
//        query.append(" AND tema = ?");
//    }
//
//    try (Connection conn = getConnection();
//         PreparedStatement stmt = conn.prepareStatement(query.toString())) {
//
//        int paramIndex = 1;
//        stmt.setInt(paramIndex++, idUsuario);
//
//        if (nome != null && !nome.isEmpty()) {
//            stmt.setString(paramIndex++, "%" + nome + "%");
//        }
//        if (raridade != null) {
//            stmt.setString(paramIndex++, raridade);
//        }
//        if (tema != null) {
//            stmt.setString(paramIndex++, tema);
//        }
//
//        ResultSet rs = stmt.executeQuery();
//        while (rs.next()) {
//            Carta carta = new Carta(
//                rs.getInt("id"),
//                rs.getString("nome"),
//                rs.getString("raridade"),
//                rs.getString("tema"),
//                rs.getString("id_colecao"),
//                rs.getBytes("imagem")
//            );
//            cartas.add(carta);
//        }
//    }
//    return cartas;
//}

public List<Carta> buscarCartasFiltradas(int idUsuario, String nome, String raridade, String tema, Integer idBinder) throws SQLException {
    List<Carta> cartas = new ArrayList<>();
    StringBuilder query = new StringBuilder("SELECT * FROM cartas WHERE id_user = ?");

    // Adicionar filtros dinamicamente
    if (nome != null && !nome.isEmpty()) {
        query.append(" AND LOWER(nome) LIKE LOWER(?)");
    }
    if (raridade != null) {
        query.append(" AND raridade = ?");
    }
    if (tema != null) {
        query.append(" AND tema = ?");
    }
    if (idBinder != null) {
        query.append(" AND id_colecao = ?");
    }

    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(query.toString())) {

        int paramIndex = 1;
        stmt.setInt(paramIndex++, idUsuario);

        if (nome != null && !nome.isEmpty()) {
            stmt.setString(paramIndex++, "%" + nome + "%");
        }
        if (raridade != null) {
            stmt.setString(paramIndex++, raridade);
        }
        if (tema != null) {
            stmt.setString(paramIndex++, tema);
        }
        if (idBinder != null) {
            stmt.setInt(paramIndex++, idBinder);
        }

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

public void excluirCartasPorBinder(int idBinder) throws SQLException {
    String sql = "DELETE FROM cartas WHERE id_colecao = ?";

    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, idBinder);
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


public int buscarIdPorNome(String nomeCarta) throws SQLException {
    String sql = "SELECT id FROM cartas WHERE nome = ?";
    
    try (Connection conn = DriverManager.getConnection(URL);
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, nomeCarta);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("id"); // Retorna o ID da carta encontrada
            } else {
                throw new SQLException("Carta com o nome '" + nomeCarta + "' não encontrada.");
            }
        }
    }
}


}

