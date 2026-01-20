package control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.*;

import java.io.IOException;
import java.util.*;
import java.util.Date;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String email = (String) session.getAttribute("user");
        List<Integer> cart = (List<Integer>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            response.sendRedirect("cart?error=Carrello+vuoto");
            return;
        }

        String paymentMethod = request.getParameter("paymentMethod");
        String totalPriceStr = request.getParameter("totalPrice");

        if (paymentMethod == null || totalPriceStr == null) {
            request.setAttribute("errorMessage", "Dati mancanti.");
            doGet(request, response);  // Mostra di nuovo il form
            return;
        }

        try {
            Map<Integer, Integer> quantities = new HashMap<>();
            for (Integer comicId : cart) {
                quantities.put(comicId, quantities.getOrDefault(comicId, 0) + 1);
            }

            ComicDAO comicDAO = new ComicDAO();
            List<Comic> comics = comicDAO.getComicsByIds(cart);

            List<OrderItem> items = new ArrayList<>();
            double calculatedTotal = 0.0;

            for (Comic comic : comics) {
                int qty = quantities.get(comic.getId());
                double price = comic.getPrice().doubleValue();
                double subtotal = qty * price;
                calculatedTotal += subtotal;

                items.add(new OrderItem(comic.getId(), comic.getTitle(), qty, price, comic.getImage()));
            }

            double totalPrice = Double.parseDouble(totalPriceStr);
            if (Math.abs(calculatedTotal - totalPrice) > 0.01) {
                throw new Exception("Il totale ricevuto non corrisponde al calcolo.");
            }

            int orderNumber = new Random().nextInt(900_000) + 100_000;
            Order order = new Order(orderNumber, paymentMethod, totalPrice, new Date(), items, email);

            OrderDAO orderDAO = new OrderDAO();
            orderDAO.saveOrder(email, order);

            session.removeAttribute("cart");
            response.sendRedirect("thankyou.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", e.getMessage());
            doGet(request, response); // Ricarica checkout.jsp con errore
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        List<Integer> cart = (List<Integer>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            request.setAttribute("items", new ArrayList<OrderItem>());
            request.setAttribute("total", 0.0);
            request.getRequestDispatcher("checkout.jsp").forward(request, response);
            return;
        }

        try {
            ComicDAO comicDAO = new ComicDAO();
            List<Comic> comics = comicDAO.getComicsByIds(cart);

            Map<Integer, Integer> quantities = new HashMap<>();
            double total = 0.0;
            List<OrderItem> items = new ArrayList<>();

            for (Integer id : cart) {
                quantities.put(id, quantities.getOrDefault(id, 0) + 1);
            }

            for (Comic c : comics) {
                int qty = quantities.get(c.getId());
                double price = c.getPrice().doubleValue();
                total += price * qty;

                items.add(new OrderItem(c.getId(), c.getTitle(), qty, price, c.getImage()));
            }

            request.setAttribute("items", items);
            request.setAttribute("total", total);

        } catch (Exception e) {
            request.setAttribute("errorMessage", "Errore durante il caricamento: " + e.getMessage());
        }

        request.getRequestDispatcher("checkout.jsp").forward(request, response);
    }
}

