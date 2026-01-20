package control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.Comic;
import model.ComicDAO;

import java.io.IOException;
import java.util.List;

@WebServlet("/allcomics")
public class AllComicsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ComicDAO dao = new ComicDAO();
        List<Comic> comics = dao.getAllComics();

        request.setAttribute("comics", comics);
        request.getRequestDispatcher("/allComics.jsp").forward(request, response);
    }
}
