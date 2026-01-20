package control;

import model.User;
import model.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String name          = request.getParameter("name");
        String email         = request.getParameter("email");
        String password      = request.getParameter("password");
        String secQuestion   = request.getParameter("securityQuestion");
        String secAnswer     = request.getParameter("securityAnswer");

        try {
            UserDAO userDAO = new UserDAO();
            // nuova firma con domanda/risposta
            userDAO.registerUser(name, email, password, secQuestion, secAnswer);

            // Recupera l'utente appena registrato
            User newUser = userDAO.doLogin(email, password);

            if (newUser != null) {
                HttpSession session = request.getSession(true);
                session.setAttribute("authToken", UUID.randomUUID().toString());
                session.setAttribute("user", newUser.getName());
                session.setAttribute("userId", newUser.getId());
                session.setAttribute("isAdmin", newUser.isAdmin());

                if (newUser.isAdmin()) {
                    response.sendRedirect(request.getContextPath() + "/admin/admin.jsp");
                } else {
                    response.sendRedirect(request.getContextPath() + "/index.jsp");
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/register.jsp?error=1");
        }
    }
}
