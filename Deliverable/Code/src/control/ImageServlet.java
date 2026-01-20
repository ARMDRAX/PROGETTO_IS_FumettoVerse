package control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;

@WebServlet("/images/*")
public class ImageServlet extends HttpServlet {

    private String uploadPath;

    @Override
    public void init() throws ServletException {
        // Recupera il percorso dalla configurazione web.xml
        uploadPath = getServletContext().getInitParameter("uploadDir");
        if (uploadPath == null) {
            throw new ServletException("Parametro 'uploadDir' non trovato in web.xml");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // Estrae il nome file dalla URL
        String requestedFile = request.getPathInfo(); // es. /1719250045123_cover.jpg
        if (requestedFile == null || requestedFile.contains("..")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Nome file non valido");
            return;
        }

        File file = new File(uploadPath, requestedFile);
        if (!file.exists() || !file.isFile()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "File non trovato");
            return;
        }

        // Imposta il tipo MIME
        String mimeType = getServletContext().getMimeType(file.getName());
        response.setContentType(mimeType != null ? mimeType : "application/octet-stream");

        // Cache per 30 giorni
        response.setHeader("Cache-Control", "max-age=2592000");

        // Invia l’immagine
        try (InputStream in = new FileInputStream(file);
             OutputStream out = response.getOutputStream()) {
            in.transferTo(out);
        }
    }
}
