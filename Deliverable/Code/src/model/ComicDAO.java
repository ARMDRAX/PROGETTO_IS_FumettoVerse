package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComicDAO {

    public void addComic(String title, double price, String tipo, String image) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO comics (title, price, tipo, image) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, title);
                stmt.setDouble(2, price);
                stmt.setString(3, tipo);
                stmt.setString(4, image);
                stmt.executeUpdate();
            }
        }
    }

    public void deleteComic(int id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM comics WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
        }
    }

    public void updateComic(int id, String title, double price, String tipo, String image) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE comics SET title = ?, price = ?, tipo = ?, image = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, title);
                stmt.setDouble(2, price);
                stmt.setString(3, tipo);
                stmt.setString(4, image);
                stmt.setInt(5, id);
                stmt.executeUpdate();
            }
        }
    }
    
    
    public List<Comic> getAllComics() {
        List<Comic> comics = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM comics");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Comic comic = new Comic();
                comic.setId(rs.getInt("id"));
                comic.setTitle(rs.getString("title"));
                comic.setPrice(rs.getBigDecimal("price"));
                comic.setImage(rs.getString("image"));
                comic.setTipo(rs.getString("tipo"));
                comics.add(comic);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return comics;
    }

    
    public List<Comic> getComicsByType(String tipo) {
        List<Comic> list = new ArrayList<>();
        String sql = "SELECT * FROM comics WHERE tipo = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tipo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Comic c = new Comic();
                    c.setId(rs.getInt("id"));
                    c.setTitle(rs.getString("title"));
                    c.setPrice(rs.getBigDecimal("price"));
                    c.setImage(rs.getString("image"));
                    c.setTipo(rs.getString("tipo"));
                    list.add(c);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    
    public List<Comic> getComicsByIds(List<Integer> ids) {
        List<Comic> list = new ArrayList<>();
        if (ids == null || ids.isEmpty()) return list;

        String placeholders = String.join(",", ids.stream().map(i -> "?").toArray(String[]::new));
        String sql = "SELECT * FROM comics WHERE id IN (" + placeholders + ")";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < ids.size(); i++) {
                ps.setInt(i + 1, ids.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Comic c = new Comic();
                    c.setId(rs.getInt("id"));
                    c.setTitle(rs.getString("title"));
                    c.setPrice(rs.getBigDecimal("price"));
                    c.setImage(rs.getString("image"));
                    c.setTipo(rs.getString("tipo"));
                    list.add(c);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    public String getComicTitle(int id) throws  Exception {
        String title = null;
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT title FROM comics WHERE id = ?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            title = rs.getString("title");
        }

        rs.close();
        stmt.close();
        conn.close();
        return title;
    }

    
    public Comic getComicById(int id) throws Exception {
        Comic comic = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM comics WHERE id = ?")) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    comic = new Comic();
                    comic.setId(rs.getInt("id"));
                    comic.setTitle(rs.getString("title"));
                    comic.setPrice(rs.getBigDecimal("price"));
                    comic.setTipo(rs.getString("tipo"));
                    comic.setImage(rs.getString("image"));
                }
            }
        }
        return comic;
    } 
    
    public int getComicIdByTitle(String title) throws Exception {
        String query = "SELECT id FROM comics WHERE title = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                throw new Exception("Fumetto non trovato con titolo: " + title);
            }
        }
    }
    
}
