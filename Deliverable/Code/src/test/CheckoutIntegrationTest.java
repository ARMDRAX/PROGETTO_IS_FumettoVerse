package test;

import model.*;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CheckoutIntegrationTest {

    private UserDAO userDAO;
    private ComicDAO comicDAO;

    @BeforeEach
    void setUp() {
        userDAO = new UserDAO();
        comicDAO = new ComicDAO();
    }

    @Test
    void testCheckoutIntegrationCompleto() throws Exception {

        // 1️⃣ Utente valido
        User user = userDAO.doLogin("obiwanreal@gmail.com", "StarWars88");
        assertNotNull(user, "Utente non autenticato");

        // 2️⃣ Recupero fumetto reale
        List<Comic> comics = comicDAO.getAllComics();
        assertFalse(comics.isEmpty(), "Catalogo vuoto");

        Comic comic = comics.get(0);

        // 3️⃣ Simulazione carrello
        int quantity = 2;
        BigDecimal total =
                comic.getPrice().multiply(BigDecimal.valueOf(quantity));

        // 4️⃣ Verifiche di integrazione
        assertNotNull(comic.getId());
        assertTrue(total.compareTo(BigDecimal.ZERO) > 0);

        // 👉 Questo test dimostra che:
        // UserDAO + ComicDAO + modello dati funzionano insieme
    }
}

