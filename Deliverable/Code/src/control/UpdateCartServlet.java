package control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/updateCart")
public class UpdateCartServlet extends HttpServlet {

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    HttpSession session = request.getSession(true);
    List cart = (List) session.getAttribute("cart");

    if (cart == null) {
      response.sendRedirect("cart");
      return;
    }

    try {
      int comicId = Integer.parseInt(request.getParameter("comicId"));
      int quantity = Integer.parseInt(request.getParameter("quantity"));

      // BLOCCO: quantità non valida => niente checkout possibile
      if (quantity < 1) {
        response.sendRedirect("cart?error=invalidQuantity");
        return;
      }

      // rimuovi tutte le occorrenze del fumetto
      cart.removeIf(id -> ((Integer) id) == comicId);

      // aggiungi N occorrenze
      for (int i = 0; i < quantity; i++) {
        cart.add(comicId);
      }

      session.setAttribute("cart", cart);
      response.sendRedirect("cart");

    } catch (NumberFormatException e) {
      e.printStackTrace();
      response.sendRedirect("cart?error=invalidQuantity");
    }
  }
}
