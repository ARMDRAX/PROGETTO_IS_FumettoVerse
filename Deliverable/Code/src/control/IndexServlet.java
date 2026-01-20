package control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Comic;
import model.ComicDAO;

import java.io.IOException;
import java.util.List;

@WebServlet("/index")
public class IndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ComicDAO dao = new ComicDAO();

        // Fumetti mostrati manualmente per la homepage (One Piece 110, ecc.)
        List<Comic> comics = dao.getComicsByIds(List.of(72, 2, 3, 4));

        request.setAttribute("comics", comics);
        request.getRequestDispatcher("/homepage.jsp").forward(request, response);
    }
}
