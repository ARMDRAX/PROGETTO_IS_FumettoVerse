package control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/admin/cancelOrder")
public class AdminCancelOrderServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("authToken") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        String role = (String) session.getAttribute("role");
        if (role == null || !"ADMIN_ORDERS".equals(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accesso non autorizzato");
            return;
        }

        String orderNumberStr = request.getParameter("orderNumber");

        try {
            int orderNumber = Integer.parseInt(orderNumberStr);

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                         "DELETE FROM orders WHERE order_number = ?")) {

                stmt.setInt(1, orderNumber);
                stmt.executeUpdate();
            }

            response.sendRedirect(request.getContextPath() + "/admin/orders?message=canceled");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/orders?error=cancel");
        }
    }
}
