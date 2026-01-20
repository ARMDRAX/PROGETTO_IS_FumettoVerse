package control;

import model.ComicDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import jakarta.servlet.RequestDispatcher;

@WebServlet("/admin/deleteComic")
public class AdminDeleteComicServlet extends HttpServlet {

    // GET → Mostra pagina di conferma con titolo
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("authToken") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        if (isAdmin == null || !isAdmin) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accesso non autorizzato");
            return;
        }

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            ComicDAO dao = new ComicDAO();
            String title = dao.getComicTitle(id);

            if (title == null) {
                response.sendRedirect(request.getContextPath() + "/admin/catalog.jsp?notfound=1");
                return;
            }

            request.setAttribute("id", id);
            request.setAttribute("title", title);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/deleteComic.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/catalog.jsp?error=1");
        }
    }

    // POST → Esegue eliminazione
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("authToken") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        if (isAdmin == null || !isAdmin) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accesso non autorizzato");
            return;
        }

        try {
            int id = Integer.parseInt(request.getParameter("id"));

            ComicDAO dao = new ComicDAO();
            dao.deleteComic(id);

            response.sendRedirect(request.getContextPath() + "/admin/catalog?deleted=1");


        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/catalog?error=1");

        }
    }
}
