package control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/admin/editUser")
public class AdminEditUserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("authToken") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        String role = (String) session.getAttribute("role");
        if (role == null || !"ADMIN_USERS".equals(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accesso non autorizzato");
            return;
        }

        String idStr = request.getParameter("id");
        String name  = request.getParameter("name");
        String email = request.getParameter("email");

        try {
            int id = Integer.parseInt(idStr);

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                         "UPDATE users SET name = ?, email = ? WHERE id = ?")) {

                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setInt(3, id);
                stmt.executeUpdate();
            }

            response.sendRedirect(request.getContextPath() + "/admin/users?message=updated");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/users?error=update");
        }
    }
}

