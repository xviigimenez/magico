package dao;

import interfaces.DAO;
import models.Binder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class BinderDAO implements DAO<Binder> {

    private static final String DATABASE_URL = "jdbc:sqlite:CardProData.db";

    @Override
    public void inserir(Binder binder) {
        String sql = "INSERT INTO binder (nome, descricao, id_user) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, binder.getNome());
            pstmt.setString(2, binder.getDescricao());
            pstmt.setInt(3, binder.getIdUser());

            pstmt.executeUpdate();
            System.out.println("Binder inserido com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Método para buscar o ID da coleção com base no nome
    public String buscarIdColecaoPorNome(String nomeColecao) {
    String idColecao = null;
    String sql = "SELECT id FROM binder WHERE nome = ?"; // Assumindo que você tenha uma tabela 'colecao'

    try (Connection conn = DriverManager.getConnection("jdbc:sqlite:CardProData.db");
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, nomeColecao);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            idColecao = rs.getString("id");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return idColecao;
}
        

    @Override
    public void alterar(Binder binder) {
        String sql = "UPDATE binder SET nome = ?, descricao = ?, id_user = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, binder.getNome());
            pstmt.setString(2, binder.getDescricao());
            pstmt.setInt(3, binder.getIdUser());
            pstmt.setInt(4, binder.getId());

            pstmt.executeUpdate();
            System.out.println("Binder atualizado com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void excluir(int id) {
        String sql = "DELETE FROM binder WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Binder excluído com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Binder consultar(int id) {
        String sql = "SELECT * FROM binder WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Binder(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        rs.getInt("id_user")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Binder> listar() {
        List<Binder> binders = new ArrayList<>();
        String sql = "SELECT * FROM binder";
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                binders.add(new Binder(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        rs.getInt("id_user")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return binders;
    }
    
    // Método para listar as coleções do usuário
    public List<String> listarBinderPorUsuario(int idUser) {
        List<String> colecoes = new ArrayList<>();
        String sql = "SELECT nome FROM binder WHERE id_user = ?";

        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, idUser);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                colecoes.add(rs.getString("nome"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return colecoes;
    }
    // Método para verificar se o nome do binder já existe no banco de dados para o usuário
    public boolean nomeBinderExiste(String nome, int idUser) {
        String sql = "SELECT COUNT(*) FROM binder WHERE nome = ? AND id_user = ?";
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, nome);
            pstmt.setInt(2, idUser);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                return true;  // Nome já existe
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Nome não existe
    }
    
//  Responsavel por usar o nome que esta no comboBox(binderBox) em uma pesquisa na tabela colecoes para buscar o id respectivo daquele nome
    public int buscarIdBinderPorNome(String nome, int idUsuario) {
        String sql = "SELECT id FROM binder WHERE nome = ? AND id_user = ?";
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, nome);
            pstmt.setInt(2, idUsuario);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");  // Retorna o ID do binder
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;  // Retorna -1 se não encontrar o binder
    }
   

 public Binder buscarBinderPorNome(String nome, int idUsuario) throws SQLException {
    String sql = "SELECT * FROM binder WHERE nome = ? AND id_user = ?";
    Binder binder = null;

    try (Connection conn = DriverManager.getConnection(DATABASE_URL);
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, nome);
        stmt.setInt(2, idUsuario);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            binder = new Binder(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("descricao"),
                rs.getInt("id_user")
            );
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return binder;
}

}

