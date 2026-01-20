package control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.ComicDAO;

import java.io.IOException;
import java.util.List;

@WebServlet("/removeFromCart")
public class RemoveFromCartServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        List<Integer> cart = (List<Integer>) session.getAttribute("cart");

        try {
            int comicId = Integer.parseInt(request.getParameter("comicId"));
            cart.removeIf(id -> id == comicId);
            session.setAttribute("cart", cart);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        response.sendRedirect("cart");
    }
}
