package control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Comic;
import model.ComicDAO;

import java.io.IOException;
import java.util.List;

@WebServlet("/manga")
public class MangaServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ComicDAO dao = new ComicDAO();
        List<Comic> comics = dao.getComicsByType("MANGA");   

        request.setAttribute("comics", comics);
        request.getRequestDispatcher("/manga.jsp").forward(request, response);
    }
}
