package test;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CheckoutSeleniumTest {

    private WebDriver driver;
    private WebDriverWait wait;

    private final String BASE_URL = "http://localhost:8081/FumettoVerse";
    private final String EMAIL = "obiwanreal@gmail.com";
    private final String PASSWORD = "StarWars88";

    @BeforeEach
    void setUp() throws Exception {
        System.setProperty(
                "webdriver.chrome.driver",
                "C:\\Users\\Armando\\Desktop\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe"
        );

        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.manage().window().maximize();
        slow();
    }

    @AfterEach
    void tearDown() throws Exception {
        slow();
        driver.quit();
    }

    // ==========================
    // UTILITY
    // ==========================

    private void slow() throws Exception {
        Thread.sleep(1500); // ⏱ rallenta tutto
    }

    private void login() throws Exception {
        driver.get(BASE_URL + "/login.jsp");
        slow();

        driver.findElement(By.id("email")).sendKeys(EMAIL);
        slow();
        driver.findElement(By.id("password")).sendKeys(PASSWORD);
        slow();

        driver.findElement(By.tagName("button")).click();
        slow();
    }

    private void addOneComicToCart() throws Exception {
        driver.get(BASE_URL + "/index");
        slow();

        WebElement addButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector(".add-to-cart")
                )
        );
        addButton.click();
        slow();
    }

    private void goToCartAndCheckout() throws Exception {
        driver.get(BASE_URL + "/cart");
        slow();
        driver.findElement(By.cssSelector(".checkout-btn")).click();
        slow();
    }

    // ==========================
    // 8.13 – AVVIO CHECKOUT
    // ==========================

    @Test
    @Order(1)
    void TC13_1_AU1_CV1_checkoutVisibile() throws Exception {
        login();
        addOneComicToCart();

        driver.get(BASE_URL + "/cart");
        slow();

        driver.findElement(By.cssSelector(".checkout-btn")).click();
        slow();

        Assertions.assertTrue(
            driver.getPageSource().contains("Riepilogo ordine")
        );
    }

    @Test
    @Order(2)
    void TC13_2_AU2_CV1_redirectLogin() throws Exception {
        addOneComicToCart();

        driver.get(BASE_URL + "/cart");
        slow();

        driver.findElement(By.cssSelector(".checkout-btn")).click();
        slow();

        Assertions.assertTrue(
            driver.getCurrentUrl().contains("login")
        );
    }

    @Test
    @Order(3)
    void TC13_3_AU1_CV2_carrelloVuoto() throws Exception {
        login();

        driver.get(BASE_URL + "/cart");
        slow();

        Assertions.assertTrue(
            driver.getPageSource().contains("Il tuo carrello è vuoto")
        );

        Assertions.assertTrue(
            driver.findElements(By.cssSelector(".checkout-btn")).isEmpty()
        );
    }

    // ==========================
    // 8.14 – DATI DI SPEDIZIONE
    // ==========================

    @Test
    @Order(4)
    void TC14_2_viaNonValida_contrassegno() throws Exception {
        login();
        addOneComicToCart();

        driver.get(BASE_URL + "/cart");
        slow();
        driver.findElement(By.cssSelector(".checkout-btn")).click();
        slow();

        // DATI SPEDIZIONE (via NON valida)
        driver.findElement(By.id("address")).sendKeys("A"); // FV2
        driver.findElement(By.id("houseNumber")).sendKeys("10"); // FC1
        driver.findElement(By.id("zip")).sendKeys("80100"); // CAP1
        driver.findElement(By.id("city")).sendKeys("Napoli"); // FCT1

        // ✔ Metodo di pagamento: CONTRASSEGNO
        driver.findElement(
            By.cssSelector("input[value='Contrassegno']")
        ).click();
        slow();

        String urlBefore = driver.getCurrentUrl();

        driver.findElement(By.cssSelector("button[type='submit']")).click();
        slow();

        // ✔ L’utente NON deve avanzare
        Assertions.assertEquals(urlBefore, driver.getCurrentUrl());
    }

    @Test
    @Order(5)
    void TC14_3_civicoNonValido() throws Exception {
        login();
        addOneComicToCart();

        driver.get(BASE_URL + "/cart");
        slow();
        driver.findElement(By.cssSelector(".checkout-btn")).click();
        slow();

        driver.findElement(By.id("address")).sendKeys("Via Roma"); // FV1 ✔
        driver.findElement(By.id("houseNumber")).sendKeys("-1");  // FC2 ❌
        driver.findElement(By.id("zip")).sendKeys("80100");       // CAP1 ✔
        driver.findElement(By.id("city")).sendKeys("Napoli");     // FCT1 ✔

        driver.findElement(By.cssSelector("input[value='Contrassegno']")).click();
        slow();

        String urlBefore = driver.getCurrentUrl();
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        slow();

        Assertions.assertEquals(urlBefore, driver.getCurrentUrl());
    }


    @Test
    @Order(6)
    void TC14_4_cittaNonValida() throws Exception {
        login();
        addOneComicToCart();

        driver.get(BASE_URL + "/cart");
        slow();
        driver.findElement(By.cssSelector(".checkout-btn")).click();
        slow();

        driver.findElement(By.id("address")).sendKeys("Via Roma"); // FV1 ✔
        driver.findElement(By.id("houseNumber")).sendKeys("10");  // FC1 ✔
        driver.findElement(By.id("zip")).sendKeys("80100");       // CAP1 ✔
        driver.findElement(By.id("city")).sendKeys("A");          // FCT2 ❌

        driver.findElement(By.cssSelector("input[value='Contrassegno']")).click();
        slow();

        String urlBefore = driver.getCurrentUrl();
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        slow();

        Assertions.assertEquals(urlBefore, driver.getCurrentUrl());
    }


    @Test
    @Order(7)
    void TC14_5_capNonValido() throws Exception {
        login();
        addOneComicToCart();

        driver.get(BASE_URL + "/cart");
        slow();
        driver.findElement(By.cssSelector(".checkout-btn")).click();
        slow();

        driver.findElement(By.id("address")).sendKeys("Via Roma"); // FV1 ✔
        driver.findElement(By.id("houseNumber")).sendKeys("10");  // FC1 ✔
        driver.findElement(By.id("zip")).sendKeys("123");         // CAP2 ❌
        driver.findElement(By.id("city")).sendKeys("Napoli");     // FCT1 ✔

        driver.findElement(By.cssSelector("input[value='Contrassegno']")).click();
        slow();

        String urlBefore = driver.getCurrentUrl();
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        slow();

        Assertions.assertEquals(urlBefore, driver.getCurrentUrl());
    }


    // ==========================
    // 8.15 – CARTA DI CREDITO
    // ==========================

    private void selectCreditCard() {
        driver.findElement(By.cssSelector("input[value='Carta di Credito']")).click();
    }

    @Test
    @Order(9)
    void TC15_2_numeroCartaNonValido() throws Exception {
        login();
        addOneComicToCart();
        goToCartAndCheckout();

        // Spedizione valida
        driver.findElement(By.id("address")).sendKeys("Via Roma");
        driver.findElement(By.id("houseNumber")).sendKeys("10");
        driver.findElement(By.id("zip")).sendKeys("80100");
        driver.findElement(By.id("city")).sendKeys("Napoli");

        driver.findElement(By.cssSelector("input[value='Carta di Credito']")).click();
        slow();

        driver.findElement(By.id("cardHolder")).sendKeys("Obi Wan");
        driver.findElement(By.id("cardNumber")).sendKeys("123"); // ❌
        driver.findElement(By.id("expiry")).sendKeys("12/30");
        driver.findElement(By.id("cvv")).sendKeys("123");

        String urlBefore = driver.getCurrentUrl();
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        slow();

        Assertions.assertEquals(urlBefore, driver.getCurrentUrl());
    }


    @Test
    @Order(10)
    void TC15_3_intestatarioNonValido() throws Exception {
        login();
        addOneComicToCart();
        goToCartAndCheckout();

        // Spedizione valida
        driver.findElement(By.id("address")).sendKeys("Via Roma");
        driver.findElement(By.id("houseNumber")).sendKeys("10");
        driver.findElement(By.id("zip")).sendKeys("80100");
        driver.findElement(By.id("city")).sendKeys("Napoli");

        driver.findElement(By.cssSelector("input[value='Carta di Credito']")).click();
        slow();

        driver.findElement(By.id("cardHolder")).sendKeys("1"); // ❌
        driver.findElement(By.id("cardNumber")).sendKeys("4111111111111111");
        driver.findElement(By.id("expiry")).sendKeys("12/30");
        driver.findElement(By.id("cvv")).sendKeys("123");

        String urlBefore = driver.getCurrentUrl();
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        slow();

        Assertions.assertEquals(urlBefore, driver.getCurrentUrl());
    }

    @Test
    @Order(11)
    void TC15_4_scadenzaNonValida() throws Exception {
        login();
        addOneComicToCart();
        goToCartAndCheckout();

        // Spedizione valida
        driver.findElement(By.id("address")).sendKeys("Via Roma");
        driver.findElement(By.id("houseNumber")).sendKeys("10");
        driver.findElement(By.id("zip")).sendKeys("80100");
        driver.findElement(By.id("city")).sendKeys("Napoli");

        driver.findElement(By.cssSelector("input[value='Carta di Credito']")).click();
        slow();

        driver.findElement(By.id("cardHolder")).sendKeys("Obi Wan");
        driver.findElement(By.id("cardNumber")).sendKeys("4111111111111111");
        driver.findElement(By.id("expiry")).sendKeys("01/20"); // ❌
        driver.findElement(By.id("cvv")).sendKeys("123");

        String urlBefore = driver.getCurrentUrl();
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        slow();

        Assertions.assertEquals(urlBefore, driver.getCurrentUrl());
    }


    @Test
    @Order(12)
    void TC15_5_cvvNonValido() throws Exception {
        login();
        addOneComicToCart();
        goToCartAndCheckout();

        // Spedizione valida
        driver.findElement(By.id("address")).sendKeys("Via Roma");
        driver.findElement(By.id("houseNumber")).sendKeys("10");
        driver.findElement(By.id("zip")).sendKeys("80100");
        driver.findElement(By.id("city")).sendKeys("Napoli");

        driver.findElement(By.cssSelector("input[value='Carta di Credito']")).click();
        slow();

        driver.findElement(By.id("cardHolder")).sendKeys("Obi Wan");
        driver.findElement(By.id("cardNumber")).sendKeys("4111111111111111");
        driver.findElement(By.id("expiry")).sendKeys("12/30");
        driver.findElement(By.id("cvv")).sendKeys("1"); // ❌

        String urlBefore = driver.getCurrentUrl();
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        slow();

        Assertions.assertEquals(urlBefore, driver.getCurrentUrl());
    }


    // ==========================
    // 8.16 – CONFERMA CHECKOUT
    // ==========================

    @Test
    @Order(13)
    void TC16_1_checkoutCompletato() throws Exception {
        login();
        addOneComicToCart();
        goToCartAndCheckout();

        driver.findElement(By.id("address")).sendKeys("Via Roma");
        driver.findElement(By.id("houseNumber")).sendKeys("10");
        driver.findElement(By.id("zip")).sendKeys("80100");
        driver.findElement(By.id("city")).sendKeys("Napoli");

        selectCreditCard();

        driver.findElement(By.id("cardHolder")).sendKeys("Obi Wan");
        driver.findElement(By.id("cardNumber")).sendKeys("4111111111111111");
        driver.findElement(By.id("expiry")).sendKeys("12/30");
        driver.findElement(By.id("cvv")).sendKeys("123");

        driver.findElement(By.cssSelector("button[type='submit']")).click();
        slow();

        Assertions.assertTrue(driver.getCurrentUrl().contains("thankyou"));
    }


    @Test
    @Order(14)
    void TC16_3_datiIncompleti() throws Exception {
        login();
        addOneComicToCart();
        goToCartAndCheckout();

        // Tentativo di submit SENZA compilare i campi
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        slow();

        // ORACOLO 1: messaggio di errore client-side visibile
        WebElement errorDiv = driver.findElement(By.id("error-message"));

        Assertions.assertFalse(
            errorDiv.getText().isBlank(),
            "Messaggio di errore client-side non mostrato"
        );

        // ORACOLO 2: nessun redirect (resta sulla pagina di checkout)
        Assertions.assertTrue(
            driver.getCurrentUrl().contains("checkout"),
            "Redirect avvenuto nonostante dati incompleti"
        );
    }

}

