package test;

import model.Comic;
import model.ComicDAO;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ComicDAOTest {

    private ComicDAO comicDAO;
    private String testTitle;

    @BeforeEach
    void setUp() {
        comicDAO = new ComicDAO();
        testTitle = "TestComic_" + System.currentTimeMillis();
    }

    /* ============================
       INSERIMENTO E RECUPERO
       ============================ */

    @Test
    void testAddAndGetComicById() throws Exception {

        comicDAO.addComic(
                testTitle,
                9.99,
                "fumetto",
                "test.png"
        );

        int id = comicDAO.getComicIdByTitle(testTitle);
        Comic comic = comicDAO.getComicById(id);

        assertNotNull(comic, "Fumetto inserito ma non trovato");
        assertEquals(testTitle, comic.getTitle());
    }

    /* ============================
       CATALOGO
       ============================ */

    @Test
    void testGetAllComicsNotEmpty() {
        List<Comic> comics = comicDAO.getAllComics();

        assertNotNull(comics, "Lista fumetti nulla");
        assertFalse(comics.isEmpty(), "Catalogo fumetti vuoto");
    }

    /* ============================
       FILTRO PER TIPO
       ============================ */

    @Test
    void testGetComicsByType() {
        List<Comic> comics = comicDAO.getComicsByType("fumetto");

        assertNotNull(comics, "Lista fumetti per tipo nulla");
    }

    /* ============================
       RECUPERO MULTIPLO
       ============================ */

    @Test
    void testGetComicsByIds() throws Exception {

        List<Comic> all = comicDAO.getAllComics();
        assertFalse(all.isEmpty(), "Nessun fumetto nel DB");

        int id1 = all.get(0).getId();
        List<Comic> result = comicDAO.getComicsByIds(Arrays.asList(id1));

        assertEquals(1, result.size(), "Numero fumetti recuperati errato");
    }

    /* ============================
       ELIMINAZIONE
       ============================ */

    @Test
    void testDeleteComic() throws Exception {

        comicDAO.addComic(
                testTitle,
                5.99,
                "fumetto",
                "delete.png"
        );

        int id = comicDAO.getComicIdByTitle(testTitle);
        comicDAO.deleteComic(id);

        Comic deleted = comicDAO.getComicById(id);
        assertNull(deleted, "Fumetto non eliminato correttamente");
    }
}
