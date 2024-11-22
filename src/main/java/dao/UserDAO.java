package dao;

import model.User;
import interfaces.DAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserDAO implements DAO<User> {
    private static final String URL = "jdbc:sqlite:CardProData.db"; // Banco de dados renomeado para CardProData

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    @Override
public void inserir(User user) {
    String sql = "INSERT INTO usuarios (name, password, tel, email) VALUES (?, ?, ?, ?)";
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        stmt.setString(1, user.getName());
        stmt.setString(2, hashPassword(user.getPassword())); // Transforma a senha em hash
        stmt.setString(3, user.getTel());
        stmt.setString(4, user.getEmail());

        int rowsAffected = stmt.executeUpdate();

        if (rowsAffected > 0) {
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
}


    private String hashPassword(String password) {
    try {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException("Erro ao gerar hash da senha", e);
    }
}

    public User consultarLogin(String email, String plainPassword) {
    String sql = "SELECT * FROM usuarios WHERE email = ?";
    User user = null;

    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            String storedPasswordHash = rs.getString("password"); // Senha armazenada como hash
            String providedPasswordHash = hashPassword(plainPassword); // Hash da senha digitada

            // Compara hashes
            if (storedPasswordHash.equals(providedPasswordHash)) {
                user = new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    storedPasswordHash, // Mantenha o hash no objeto User
                    rs.getString("tel"),
                    rs.getString("email")
                );
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return user;
}

    

    @Override
    public void alterar(User user) {
        String sql = "UPDATE usuarios SET name = ?, password = ?, tel = ?, email = ? WHERE id = ?"; // Removido 'address'

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getTel());
            stmt.setString(4, user.getEmail());
            stmt.setInt(5, user.getId());

            stmt.executeUpdate();
            System.out.println("Usuário atualizado com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    @Override
    public void excluir(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Usuário excluído com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao excluir usuário: " + e.getMessage());
        }
    }

    @Override
    public User consultar(int id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        User user = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getString("tel"),
                        rs.getString("email")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao consultar usuário: " + e.getMessage());
        }
        return user;
    }

    @Override
    public List<User> listar() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("password"),
                    rs.getString("tel"),
                    rs.getString("email")
                );
                users.add(user);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar usuários: " + e.getMessage());
        }
        return users;
    }
    
    public boolean emailExists(String email) {
    String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ?";
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return rs.getInt(1) > 0; // Se o contador for maior que 0, o email já existe
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

}

