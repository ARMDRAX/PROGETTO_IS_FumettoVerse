package control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.RequestDispatcher;
import model.DatabaseConnection;
import model.Order;
import model.OrderItem;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

@WebServlet("/admin/orders")
public class AdminOrdersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /* --- sicurezza: solo ADMIN_ORDERS ------------------------------- */
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("authToken") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String role = (String) session.getAttribute("role");
        if (role == null || !role.equals("ADMIN_ORDERS")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accesso non autorizzato");
            return;
        }
        /* ---------------------------------------------------------------- */

        // Messaggi da operazioni (annullamento / cambio stato)
        String message = null;
        String errorMessage = null;
        String msgParam = request.getParameter("message");
        String errParam = request.getParameter("error");

        if ("canceled".equals(msgParam)) {
            message = "Ordine annullato con successo.";
        } else if ("statusUpdated".equals(msgParam)) {
            message = "Stato dell'ordine aggiornato con successo.";
        }

        if ("cancel".equals(errParam)) {
            errorMessage = "Errore durante l'annullamento dell'ordine.";
        } else if ("status".equals(errParam)) {
            errorMessage = "Errore durante l'aggiornamento dello stato dell'ordine.";
        }

        String fromDate = request.getParameter("fromDate");
        String toDate   = request.getParameter("toDate");

        // Cambiato: filtro per email (parametro "email" dalla JSP)
        String email = request.getParameter("email");

        Map<Integer, Order> ordersMap = new LinkedHashMap<>();

        String query =
                "SELECT o.order_number, o.user, o.comic_id, o.quantity, o.payment_method, " +
                "       o.total_price, o.order_date, o.status, " +
                "       c.title, c.price, c.image " +
                "FROM orders o " +
                "JOIN comics c ON o.comic_id = c.id " +
                "WHERE 1=1";

        List<Object> params = new ArrayList<>();

        if (fromDate != null && !fromDate.isEmpty()) {
            query += " AND o.order_date >= ?";
            params.add(java.sql.Date.valueOf(fromDate));
        }

        if (toDate != null && !toDate.isEmpty()) {
            LocalDate nextDay = java.sql.Date.valueOf(toDate).toLocalDate().plusDays(1);
            query += " AND o.order_date < ?";
            params.add(java.sql.Date.valueOf(nextDay));
        }

        // Filtro email (parziale, consigliato)
        if (email != null && !email.trim().isEmpty()) {
            query += " AND o.user LIKE ?";
            params.add("%" + email.trim() + "%");
        }

        query += " ORDER BY o.order_date DESC, o.order_number DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int orderNumber     = rs.getInt("order_number");
                    String userEmail    = rs.getString("user");
                    String paymentMethod = rs.getString("payment_method");
                    double totalPrice   = rs.getDouble("total_price");
                    Timestamp orderDate = rs.getTimestamp("order_date");
                    String status       = rs.getString("status");

                    Order order = ordersMap.get(orderNumber);
                    if (order == null) {
                        order = new Order(orderNumber, paymentMethod, totalPrice,
                                orderDate, new ArrayList<>(), userEmail, status);
                        ordersMap.put(orderNumber, order);
                    }

                    int comicId   = rs.getInt("comic_id");
                    String title  = rs.getString("title");
                    int quantity  = rs.getInt("quantity");
                    double price  = rs.getDouble("price");
                    String image  = rs.getString("image");

                    OrderItem item = new OrderItem(comicId, title, quantity, price, image);
                    order.getItems().add(item);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", true);
        }

        request.setAttribute("orders", new ArrayList<>(ordersMap.values()));
        request.setAttribute("fromDate", fromDate);
        request.setAttribute("toDate", toDate);

        // Cambiato: rimando alla JSP l’email cercata
        request.setAttribute("email", email);

        request.setAttribute("message", message);
        request.setAttribute("errorMessage", errorMessage);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/orders.jsp");
        dispatcher.forward(request, response);
    }
}

