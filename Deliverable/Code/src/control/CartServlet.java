package control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.Comic;
import model.ComicDAO;
import model.OrderItem;

import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    HttpSession session = request.getSession(true);
    List cart = (List) session.getAttribute("cart");

    if (cart == null || cart.isEmpty()) {
      request.setAttribute("emptyCart", true);
      request.setAttribute("total", 0.0);
      request.getRequestDispatcher("cart.jsp").forward(request, response);
      return;
    }

    // Conta le quantità di ogni fumetto
    Map<Integer, Integer> qtyMap = new HashMap<>();
    for (Object obj : cart) {
      Integer id = (Integer) obj;
      qtyMap.put(id, qtyMap.getOrDefault(id, 0) + 1);
    }

    ComicDAO dao = new ComicDAO();
    List<Comic> comics = dao.getComicsByIds(cart);

    List<OrderItem> items = new ArrayList<>();
    double total = 0.0;

    for (Comic comic : comics) {
      int qty = qtyMap.getOrDefault(comic.getId(), 1);

      // extra safety: se per qualche motivo qty < 1, la consideriamo invalida
      if (qty < 1) {
        response.sendRedirect("cart?error=invalidQuantity");
        return;
      }

      double price = comic.getPrice().doubleValue();
      total += price * qty;

      items.add(new OrderItem(comic.getId(), comic.getTitle(), qty, price, comic.getImage()));
    }

    request.setAttribute("items", items);
    request.setAttribute("total", total);
    request.setAttribute("user", session.getAttribute("user")); // può essere null

    request.getRequestDispatcher("cart.jsp").forward(request, response);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    PrintWriter out = response.getWriter();
    JSONObject json = new JSONObject();

    HttpSession session = request.getSession(true);

    try {
      int comicId = Integer.parseInt(request.getParameter("comicId"));

      List cart = (List) session.getAttribute("cart");
      if (cart == null) {
        cart = new ArrayList<>();
      }

      cart.add(comicId);
      session.setAttribute("cart", cart);

      json.put("success", true);
      json.put("cartCount", cart.size());

    } catch (NumberFormatException e) {
      json.put("success", false);
      json.put("message", "ID fumetto non valido.");
    } catch (Exception e) {
      json.put("success", false);
      json.put("message", "Errore durante l'aggiunta al carrello: " + e.getMessage());
    }

    out.print(json.toString());
    out.flush();
  }
}
