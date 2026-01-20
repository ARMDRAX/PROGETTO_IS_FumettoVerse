package control;

import model.ComicDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@WebServlet("/admin/addComic")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,       // 1MB
    maxFileSize = 1024 * 1024 * 5,         // 5MB
    maxRequestSize = 1024 * 1024 * 10      // 10MB
)
public class AdminAddComicServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Controllo autenticazione e ruolo admin
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
            String title = request.getParameter("title");
            double price = Double.parseDouble(request.getParameter("price"));
            String tipo = request.getParameter("tipo");

            Part imagePart = request.getPart("image");
            String fileName = Path.of(imagePart.getSubmittedFileName()).getFileName().toString();

            // ⬇️ Legge il percorso upload dalla configurazione in web.xml
            String uploadPath = getServletContext().getInitParameter("uploadDir");

            File uploads = new File(uploadPath);
            if (!uploads.exists()) uploads.mkdirs();

            // ⬇️ Genera nome file unico
            String newFileName = System.currentTimeMillis() + "_" + fileName;
            File file = new File(uploads, newFileName);

            // ⬇️ Salva il file
            try (InputStream input = imagePart.getInputStream()) {
                Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            // ⬇️ Salva info nel database
            ComicDAO dao = new ComicDAO();
            dao.addComic(title, price, tipo, newFileName); // salva solo il nome file, non il path

            response.sendRedirect(request.getContextPath() + "/admin/catalog?added=1");



        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("addComic.jsp?error=1");
        }
    }
}

