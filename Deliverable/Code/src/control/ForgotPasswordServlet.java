package control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.UserDAO;

import java.io.IOException;

@WebServlet("/forgotPassword")
public class ForgotPasswordServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("emailStep".equals(action)) {
            handleEmailStep(request, response);
        } else if ("resetStep".equals(action)) {
            handleResetStep(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
    }

    private void handleEmailStep(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");

        try {
            UserDAO userDAO = new UserDAO();
            // recupera codice domanda dal DB
            String questionCode = userDAO.getSecurityQuestionByEmail(email); // da implementare
            if (questionCode == null) {
                request.setAttribute("error", "Email non trovata.");
                request.getRequestDispatcher("forgotPassword.jsp").forward(request, response);
                return;
            }

            String questionText;
            switch (questionCode) {
                case "pet":
                    questionText = "Nome del tuo primo animale domestico?";
                    break;
                case "city":
                    questionText = "In che città sei nato?";
                    break;
                case "school":
                    questionText = "Nome della tua scuola elementare?";
                    break;
                default:
                    questionText = "Domanda di sicurezza";
            }

            request.setAttribute("email", email);
            request.setAttribute("securityQuestionText", questionText);
            request.setAttribute("step", "question");
            request.getRequestDispatcher("forgotPassword.jsp?step=question").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Errore durante il recupero. Riprova.");
            request.getRequestDispatcher("forgotPassword.jsp").forward(request, response);
        }
    }

    private void handleResetStep(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String securityAnswer = request.getParameter("securityAnswer");
        String newPassword = request.getParameter("newPassword");

        try {
            UserDAO userDAO = new UserDAO();

            boolean ok = userDAO.resetPasswordWithSecurityAnswer(email, securityAnswer, newPassword);
            if (ok) {
                request.setAttribute("message", "Password aggiornata con successo. Ora puoi effettuare il login.");
            } else {
                request.setAttribute("error", "Risposta errata o utente non trovato.");
            }
            request.getRequestDispatcher("forgotPassword.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Errore durante l'aggiornamento della password.");
            request.getRequestDispatcher("forgotPassword.jsp").forward(request, response);
        }
    }
}
