package control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.RequestDispatcher;
import model.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin/users")
public class AdminUsersServlet extends HttpServlet {

    public static class SimpleUser {
        private int id;
        private String name;
        private String email;
        private boolean isAdmin;
        private String role;

        public SimpleUser(int id, String name, String email, boolean isAdmin, String role) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.isAdmin = isAdmin;
            this.role = role;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public boolean isAdmin() { return isAdmin; }
        public String getRole() { return role; }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /* --- sicurezza: solo ADMIN_USERS ------------------------------- */
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("authToken") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String role = (String) session.getAttribute("role");
        if (role == null || !role.equals("ADMIN_USERS")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accesso non autorizzato");
            return;
        }
        /* ---------------------------------------------------------------- */

        // Messaggi da operazioni di update/delete
        String message = null;
        String errorMessage = null;
        String msgParam = request.getParameter("message");
        String errParam = request.getParameter("error");

        if ("updated".equals(msgParam))  message = "Utente aggiornato con successo.";
        if ("deleted".equals(msgParam))  message = "Utente eliminato con successo.";
        if ("update".equals(errParam))   errorMessage = "Errore durante l'aggiornamento dell'utente.";
        if ("delete".equals(errParam))   errorMessage = "Errore durante l'eliminazione dell'utente.";

        // NUOVO: filtro per email (parametro "email" dalla JSP)
        String emailFilter = request.getParameter("email");

        List<SimpleUser> users = new ArrayList<>();

        String sql = "SELECT id, name, email, is_admin, role FROM users WHERE 1=1";
        List<Object> params = new ArrayList<>();

        if (emailFilter != null && !emailFilter.trim().isEmpty()) {
            sql += " AND email LIKE ?";
            params.add("%" + emailFilter.trim() + "%");  // ricerca parziale
        }

        sql += " ORDER BY id ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    boolean isAdmin = rs.getBoolean("is_admin");
                    String userRole = rs.getString("role");

                    users.add(new SimpleUser(id, name, email, isAdmin, userRole));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            errorMessage = "Errore nel caricamento degli utenti.";
        }

        request.setAttribute("users", users);

        // NUOVO: rimando alla JSP il filtro per tenerlo compilato
        request.setAttribute("email", emailFilter);

        request.setAttribute("message", message);
        request.setAttribute("errorMessage", errorMessage);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/users.jsp");
        dispatcher.forward(request, response);
    }
}
