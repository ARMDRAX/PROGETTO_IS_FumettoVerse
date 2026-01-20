package control;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import model.UserDAO;
import model.User;
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8"); // Assicurati che sia impostato

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            UserDAO userDAO = new UserDAO();

            // Chiama solo doLogin, l'hashing è fatto internamente
            User userObj = userDAO.doLogin(email, password);

            if (userObj != null) {
                System.out.println("Login riuscito per utente: " + userObj.getName());

                HttpSession session = request.getSession(true);

                String token = UUID.randomUUID().toString();
                session.setAttribute("authToken", token);

                session.setAttribute("user", userObj.getName());
                session.setAttribute("userId", userObj.getId());

                // Imposta boolean isAdmin invece di stringa role
                
                session.setAttribute("role", userObj.getRole());
                session.setAttribute("isAdmin", userObj.isAdmin());

                if (userObj.isAdmin()) {
                    response.sendRedirect(request.getContextPath() + "/admin/admin.jsp");
                } else {
                    response.sendRedirect(request.getContextPath() + "/index.jsp");
                }

            } else {
                System.out.println("Login fallito: credenziali errate");
                response.sendRedirect(request.getContextPath() + "/login.jsp?error=1");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=1");
        }
    }
}


