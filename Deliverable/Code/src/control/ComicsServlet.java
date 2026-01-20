package control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Comic;
import model.ComicDAO;

import java.io.IOException;
import java.util.List;

@WebServlet("/comics")
public class ComicsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ComicDAO dao = new ComicDAO();
        List<Comic> comics = dao.getComicsByType("FUMETTO"); // "FUMETTO" come tipo nel DB

        request.setAttribute("comics", comics);
        request.getRequestDispatcher("/comics.jsp").forward(request, response);
    }
}
