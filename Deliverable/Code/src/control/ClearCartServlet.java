package control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/clearCart")
public class ClearCartServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ✅ Crea la sessione anche se l'utente non è autenticato
        HttpSession session = request.getSession(true);
        if (session != null) {
            session.removeAttribute("cart"); // oppure setAttribute("cart", null);
        }

        response.sendRedirect("cart");
    }
}
