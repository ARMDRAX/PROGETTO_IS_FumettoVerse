package model;

import java.sql.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserDAO {

    // Metodo per hashare (password o risposta) con SHA-256
    public String hashPassword(String value) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = md.digest(value.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // Registrazione con domanda e risposta di sicurezza
    public void registerUser(String name, String email, String password,
                             String securityQuestion, String securityAnswer) throws Exception {

        String hashedPassword = hashPassword(password);
        String hashedAnswer   = hashPassword(securityAnswer);

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO users " +
                         "(name, email, password, security_question, security_answer, is_admin) " +
                         "VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, hashedPassword);
                stmt.setString(4, securityQuestion);
                stmt.setString(5, hashedAnswer);
                stmt.setBoolean(6, false); // per default non admin
                stmt.executeUpdate();
            }
        }
    }

    public User doLogin(String email, String password) throws Exception {
        String hashedPassword = hashPassword(password);
        System.out.println("Tentativo login con email: '" + email +
                           "', password hashata: '" + hashedPassword + "'");

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                stmt.setString(2, hashedPassword);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    boolean isAdmin = rs.getBoolean("is_admin");
                    String role = rs.getString("role");

                    
                    System.out.println("Login riuscito per utente: " + name);
                    // per ora non servono domanda/risposta nel modello
                    return new User(id, name, email, isAdmin, role);
                    

                } else {
                    System.out.println("Nessun utente trovato con email e password date.");
                    return null;
                }
            }
        }
    }
 // Restituisce il codice della domanda di sicurezza dato l'email
    public String getSecurityQuestionByEmail(String email) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT security_question FROM users WHERE email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("security_question");
                    }
                    return null;
                }
            }
        }
    }

    // Verifica risposta e aggiorna la password
    public boolean resetPasswordWithSecurityAnswer(String email, String securityAnswer, String newPassword) throws Exception {
        String hashedAnswer = hashPassword(securityAnswer);
        String hashedNewPwd = hashPassword(newPassword);

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE users SET password = ? " +
                         "WHERE email = ? AND security_answer = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, hashedNewPwd);
                stmt.setString(2, email);
                stmt.setString(3, hashedAnswer);
                int rows = stmt.executeUpdate();
                return rows > 0;
            }
        }
    }

}
