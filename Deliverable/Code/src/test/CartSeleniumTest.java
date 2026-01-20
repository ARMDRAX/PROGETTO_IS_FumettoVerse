package test;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CartSeleniumTest {

    private WebDriver driver;
    private JavascriptExecutor js;

    private final String BASE_URL = "http://localhost:8081/FumettoVerse";

    /* =====================
       SETUP / TEARDOWN
       ===================== */

    @BeforeEach
    void setUp() {
        System.setProperty(
            "webdriver.chrome.driver",
            "C:\\Users\\Armando\\Desktop\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe"
        );
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;
        driver.manage().window().maximize();
        slow(1000);
    }

    @AfterEach
    void tearDown() {
        slow(2000);
        driver.quit();
    }

    /* =====================
       UTILITY
       ===================== */
    private void addTwoDifferentComicsToCart() {
        driver.get(BASE_URL + "/index");
        slow(1500);

        // primo fumetto
        driver.findElements(By.className("add-to-cart")).get(0).click();
        slow(800);

        // secondo fumetto (diverso)
        driver.findElements(By.className("add-to-cart")).get(1).click();
        slow(800);
    }

   
    private void slow(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }

    private void openIndex() {
        driver.get(BASE_URL + "/index");
        slow(1500);
    }

    private void openCart() {
        driver.get(BASE_URL + "/cart");
        slow(1500);
    }

    /** Aggiunge il PRIMO fumetto visibile */
    private void addFirstComicToCart() {
        openIndex();
        WebElement addBtn =
            driver.findElements(By.className("add-to-cart")).get(0);
        addBtn.click();
        slow(1200);
    }

    /** Login utente valido */
    private void login() {
        driver.get(BASE_URL + "/login.jsp");
        slow(1200);

        driver.findElement(By.id("email"))
              .sendKeys("obiwanreal@gmail.com");
        driver.findElement(By.id("password"))
              .sendKeys("StarWars88");

        driver.findElement(By.cssSelector("button[type='submit']")).click();
        slow(1500);
    }

    /* =========================================================
       8.7 AGGIUNGI AL CARRELLO
       ========================================================= */

    /** TC7.1: TS1, PF1, DF1, QV1 */
    @Test @Order(1)
    void TC7_1_aggiuntaCarrelloUtenteAutenticato() {
        login();
        addFirstComicToCart();
        openCart();

        Assertions.assertFalse(
            driver.findElements(By.className("cart-item")).isEmpty(),
            "Fumetto non aggiunto al carrello (utente autenticato)"
        );
    }

    /** TC7.2: TS2, PF1, DF1, QV1 */
    @Test @Order(2)
    void TC7_2_aggiuntaCarrelloGuest() {
        addFirstComicToCart();
        openCart();

        Assertions.assertFalse(
            driver.findElements(By.className("cart-item")).isEmpty(),
            "Fumetto non aggiunto al carrello (guest)"
        );
    }

    /* =========================================================
       8.8 RIMUOVI DAL CARRELLO
       ========================================================= */

    /** TC8.2: TS1, IC1, Q2 */
    @Test @Order(3)
    void TC8_2_rimozioneCompletaUtente() {
        login();
        addFirstComicToCart();
        openCart();

        driver.findElement(By.className("remove-btn")).click();
        slow(1200);

        Assertions.assertTrue(
            driver.getPageSource().toLowerCase().contains("carrello è vuoto")
            || driver.findElements(By.className("cart-item")).isEmpty(),
            "Fumetto non rimosso dal carrello"
        );
    }

    /** TC8.4: TS2, IC1, Q2 */
    @Test @Order(4)
    void TC8_4_rimozioneCompletaGuest() {
        addFirstComicToCart();
        openCart();

        driver.findElement(By.className("remove-btn")).click();
        slow(1200);

        Assertions.assertTrue(
            driver.findElements(By.className("cart-item")).isEmpty(),
            "Fumetto non rimosso dal carrello (guest)"
        );
    }

    /* =========================================================
       8.9 AGGIORNA QUANTITÀ
       ========================================================= */

    /** TC9.1: IC1, QV1 */
    @Test @Order(5)
    void TC9_1_aggiornaQuantitaValida() {
        addFirstComicToCart();
        openCart();

        WebElement qty =
            driver.findElement(By.cssSelector("input[type='number']"));
        qty.clear();
        qty.sendKeys("2");

        driver.findElement(By.className("update-btn")).click();
        slow(1200);

        Assertions.assertTrue(
            driver.getPageSource().contains("x 2")
            || qty.getAttribute("value").equals("2"),
            "Quantità non aggiornata correttamente"
        );
    }
    
    @Test@Order(6)
    void TC9_2_aggiornaQuantitaNonValida() {

        // PRECONDIZIONE: carrello con 1 fumetto
        addFirstComicToCart();

        driver.get(BASE_URL + "/cart");
        slow(1500);

        // Imposta quantità NON valida
        WebElement qtyInput = driver.findElement(By.cssSelector("input.quantity-input"));
        qtyInput.clear();
        qtyInput.sendKeys("0"); // ❌ min=1

        slow(800);

        WebElement checkoutBtn = driver.findElement(By.id("checkoutBtn"));

        // ==========================
        // ORACOLO 1 – BLOCCO CLIENT-SIDE
        // ==========================

        Assertions.assertFalse(
            checkoutBtn.isEnabled(),
            "Checkout non disabilitato con quantità non valida"
        );

        // ==========================
        // ORACOLO 2 – MESSAGGIO DI ERRORE
        // ==========================

        Assertions.assertTrue(
            driver.findElement(By.id("cart-error"))
                  .getText()
                  .toLowerCase()
                  .contains("correggi le quantità"),
            "Messaggio di errore client-side non mostrato"
        );

        // ==========================
        // ORACOLO 3 – CARRELLO INVARIATO
        // ==========================

        Assertions.assertFalse(
            driver.getPageSource().toLowerCase().contains("il tuo carrello è vuoto"),
            "Item rimosso erroneamente con quantità non valida"
        );

        // ==========================
        // ORACOLO 4 – NESSUN REDIRECT
        // ==========================

        Assertions.assertTrue(
            driver.getCurrentUrl().endsWith("/cart"),
            "Redirect avvenuto nonostante quantità non valida"
        );
    }




    /* =========================================================
       8.10 VISUALIZZAZIONE CARRELLO VUOTO
       ========================================================= */

    /** TC10.2: PC2 */
    @Test @Order(8)
    void TC10_2_visualizzazioneCarrelloVuoto() {
        openCart();

        Assertions.assertTrue(
            driver.getPageSource().toLowerCase().contains("carrello è vuoto"),
            "Messaggio carrello vuoto non mostrato"
        );
    }

    /* =========================================================
       8.11 SVUOTA CARRELLO
       ========================================================= */

    /** TC11.1: PC1 → Carrello svuotato correttamente */
    @Test @Order(12)
    void TC11_1_svuotaCarrelloConDueFumetti() {

        // PRECONDIZIONE: carrello con 2 fumetti diversi
        addTwoDifferentComicsToCart();
        openCart();

        // Verifica che ci siano almeno 2 item
        Assertions.assertTrue(
            driver.findElements(By.className("cart-item")).size() >= 2,
            "Precondizione non valida: meno di due fumetti nel carrello"
        );

        // Azione: svuota carrello
        driver.findElement(By.className("clear-cart-btn")).click();
        slow(1500);

        // ORACOLO:
        Assertions.assertTrue(
            driver.getPageSource().toLowerCase().contains("carrello è vuoto")
            || driver.getPageSource().toLowerCase().contains("vuoto"),
            "Carrello non svuotato correttamente"
        );
    }

   
}
