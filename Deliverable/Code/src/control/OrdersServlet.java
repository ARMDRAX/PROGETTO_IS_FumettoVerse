package control;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import model.Order;
import model.OrderDAO;

import java.io.IOException;
import java.util.List;

@WebServlet("/myorders")
public class OrdersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Recupera la sessione
        HttpSession session = request.getSession(false);
        String authToken = (session != null) ? (String) session.getAttribute("authToken") : null;

        if (authToken == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String userEmail = (String) session.getAttribute("user"); // usiamo l'email come identificatore

        try {
            // 🔄 Chiede gli ordini tramite DAO (usando oggetti Order e OrderItem)
            OrderDAO dao = new OrderDAO();
            List<Order> orders = dao.getOrdersByUser(userEmail);

            // 🔁 Passa gli ordini alla JSP
            request.setAttribute("orders", orders);
            request.getRequestDispatcher("myorders.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore nel caricamento degli ordini.");
        }
    }
}

