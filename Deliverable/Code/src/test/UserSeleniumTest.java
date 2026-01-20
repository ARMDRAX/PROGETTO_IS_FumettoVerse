package test;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserSeleniumTest {

    private WebDriver driver;
    private JavascriptExecutor js;
    private final String BASE_URL = "http://localhost:8081/FumettoVerse";
    static String TEST_EMAIL;
    static String TEST_PASSWORD = "Password123";
    static String TEST_NEW_PASSWORD = "NuovaPassword88";
    static String SECURITY_ANSWER = "R2D2";

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

    private void slow(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }

    // 🔒 Blocca QUALSIASI submit automatico JS
    private void disableAutoSubmit() {
        js.executeScript("""
            const form = document.querySelector("form");
            if (form) {
                form.onsubmit = function(e) {
                    e.preventDefault();
                    return false;
                };
            }
        """);
        slow(300);
    }
    
    private void openForgotPassword() {
        driver.get(BASE_URL + "/forgotPassword.jsp");
        slow(1500);
        disableAutoSubmit();
    }
    
    // 🔓 Riabilita submit SOLO quando vogliamo
    private void enableSubmit() {
        js.executeScript("""
            const form = document.querySelector("form");
            if (form && form.onsubmit) {
                form.onsubmit = null;
            }
        """);
        slow(300);
    }

    // ✍️ Compila campo SENZA trigger JS
    private void fillJS(String fieldId, String value) {
        js.executeScript(
            "document.getElementById(arguments[0]).value = arguments[1];",
            fieldId, value
        );
        slow(400);
    }

    // 🚀 Submit controllato
    private void submitForm() {
        enableSubmit();
        WebElement submit =
            driver.findElement(By.cssSelector("form button[type='submit']"));
        slow(600);
        submit.click();
        slow(1500);
    }

    private void openRegister() {
        driver.get(BASE_URL + "/register.jsp");
        slow(1500);
        disableAutoSubmit();
    }

    private void openLogin() {
        driver.get(BASE_URL + "/login.jsp");
        slow(1500);
        disableAutoSubmit();
    }

    private void selectSecurityQuestion() {
        new Select(driver.findElement(By.id("securityQuestion")))
                .selectByValue("pet");
        slow(500);
    }

    /* =====================
       8.1 REGISTRAZIONE
       ===================== */

    @Test @Order(1)
    void TC1_1_registrazioneCorretta() {
        openRegister();

        TEST_EMAIL = "user" + System.currentTimeMillis() + "@mail.com";

        fillJS("name", "Luke");
        fillJS("email", TEST_EMAIL);
        fillJS("password", TEST_PASSWORD);
        selectSecurityQuestion();
        fillJS("securityAnswer", SECURITY_ANSWER);

        submitForm();

        Assertions.assertTrue(
            driver.getCurrentUrl().contains("index"),
            "Registrazione non riuscita"
        );
    }


    @Test @Order(2)
    void TC1_2_emailGiaRegistrata() {
        openRegister();

        fillJS("name", "Luke");
        fillJS("email", "obiwanreal@gmail.com");
        fillJS("password", "Password123");
        selectSecurityQuestion();
        fillJS("securityAnswer", "R2D2");

        submitForm();
        Assertions.assertTrue(driver.getCurrentUrl().contains("register"));
    }

    @Test @Order(3)
    void TC1_3_nomeNonValido() {
        openRegister();

        fillJS("name", "");
        fillJS("email", "user" + System.currentTimeMillis() + "@mail.com");
        fillJS("password", "Password123");
        selectSecurityQuestion();
        fillJS("securityAnswer", "R2D2");

        submitForm();
        Assertions.assertTrue(driver.getCurrentUrl().contains("register"));
    }

    @Test @Order(4)
    void TC1_4_emailNonValida() {
        openRegister();

        fillJS("name", "Luke");
        fillJS("email", "email-non-valida");
        fillJS("password", "Password123");
        selectSecurityQuestion();
        fillJS("securityAnswer", "R2D2");

        submitForm();
        Assertions.assertTrue(driver.getCurrentUrl().contains("register"));
    }

    @Test @Order(5)
    void TC1_5_passwordNonValida() {
        openRegister();

        fillJS("name", "Luke");
        fillJS("email", "user" + System.currentTimeMillis() + "@mail.com");
        fillJS("password", "123");
        selectSecurityQuestion();
        fillJS("securityAnswer", "R2D2");

        submitForm();
        Assertions.assertTrue(driver.getCurrentUrl().contains("register"));
    }

    @Test @Order(6)
    void TC1_6_domandaNonSelezionata() {
        openRegister();

        fillJS("name", "Luke");
        fillJS("email", "user" + System.currentTimeMillis() + "@mail.com");
        fillJS("password", "Password123");
        fillJS("securityAnswer", "R2D2");

        submitForm();
        Assertions.assertTrue(driver.getCurrentUrl().contains("register"));
    }

    @Test @Order(7)
    void TC1_7_rispostaNonValida() {
        openRegister();

        fillJS("name", "Luke");
        fillJS("email", "user" + System.currentTimeMillis() + "@mail.com");
        fillJS("password", "Password123");
        selectSecurityQuestion();
        fillJS("securityAnswer", "");

        submitForm();
        Assertions.assertTrue(driver.getCurrentUrl().contains("register"));
    }

    /* =====================
       8.2 LOGIN
       ===================== */

    @Test @Order(8)
    void TC2_1_loginCorretto() {
        openLogin();

        fillJS("email", "obiwanreal@gmail.com");
        fillJS("password", "StarWars88");

        submitForm();
        Assertions.assertTrue(driver.getCurrentUrl().contains("index"));
    }

    @Test @Order(10)
    void TC2_3_passwordErrata() {
        openLogin();

        fillJS("email", "obiwanreal@gmail.com");
        fillJS("password", "WRONGPASS");

        submitForm();
        Assertions.assertTrue(driver.getCurrentUrl().contains("login"));
    }
    /**
     * TC2.2: PE2, CP2
     * Oracolo: Credenziali non valide
     */
    @Test @Order(9)
    void TC2_2_emailNonPresente() {
        openLogin();

        fillJS("email", "utenteinesistente@mail.com");
        fillJS("password", "Qualsiasi123");

        submitForm();

        Assertions.assertTrue(
            driver.getCurrentUrl().contains("login"),
            "Login non valido non bloccato"
        );
    }

    
  

    /* =====================
       8.3 LOGOUT
       ===================== */

    @Test @Order(10)
    void TC3_1_logout() {
        TC2_1_loginCorretto();
        slow(1000);

        driver.get(BASE_URL + "/logout");
        slow(1500);

        Assertions.assertTrue(driver.getCurrentUrl().contains("index"));
    }

    /* =====================
       8.4 PASSWORD DIMENTICATA
       ===================== */

    /**
     * TC4.1: PE1, RS1
     * Oracolo: Procedura di reset password avviata
     */
    @Test @Order(11)
    void TC4_1_emailPresenteRispostaCorretta() {
        openForgotPassword();

        // STEP 1 – email dell’utente creato in 8.1
        fillJS("email", TEST_EMAIL);
        submitForm();

        // STEP 2 – risposta corretta
        fillJS("securityAnswer", SECURITY_ANSWER);
        fillJS("newPassword", TEST_NEW_PASSWORD);
        submitForm();

        Assertions.assertTrue(
            driver.getPageSource().contains("Password aggiornata")
            || driver.getPageSource().contains("successo"),
            "Reset password non avviato"
        );
    }

    @Test @Order(12)
    void TC4_1_verificaLoginConNuovaPassword() {
        openLogin();

        fillJS("email", TEST_EMAIL);
        fillJS("password", TEST_NEW_PASSWORD);
        submitForm();

        Assertions.assertTrue(
            driver.getCurrentUrl().contains("index"),
            "Login con nuova password fallito"
        );
    }


    /**
     * TC4.2: PE2
     * Oracolo: E-mail non associata ad alcun account
     */
    @Test @Order(13)
    void TC4_2_emailNonPresente() {
        openForgotPassword();

        fillJS("email", "inesistente@mail.com");
        submitForm();

        Assertions.assertTrue(
            driver.getPageSource().contains("Email non trovata"),
            "Errore email non presente non mostrato"
        );
    }

    /**
     * TC4.3: PE1, RS2
     * Oracolo: Risposta di sicurezza errata
     */
    @Test @Order(14)
    void TC4_3_rispostaErrata() {
        openForgotPassword();

        // STEP 1 – email valida
        fillJS("email", "obiwanreal@gmail.com");
        submitForm();

        // STEP 2 – risposta errata
        fillJS("securityAnswer", "RISPOSTA_SBAGLIATA");
        fillJS("newPassword", "NuovaPassword88");

        submitForm();

        Assertions.assertTrue(
            driver.getPageSource().contains("Risposta errata")
            || driver.getPageSource().contains("errore"),
            "Risposta errata non rilevata"
        );
    }

    /* =====================
       8.5 VISUALIZZAZIONE ORDINI
       ===================== */
    @Test @Order(15)
    void TC5_1_ordiniVisualizzatiUtenteConOrdini() {
        // Login utente con ordini
        openLogin();
        fillJS("email", "obiwanreal@gmail.com");
        fillJS("password", "StarWars88");
        submitForm();

        slow(1500);

        driver.get(BASE_URL + "/myorders");
        slow(1500);

        Assertions.assertTrue(
            driver.getPageSource().toLowerCase().contains("ordine")
            || driver.getPageSource().toLowerCase().contains("totale"),
            "Ordini non visualizzati per utente con ordini"
        );
    }
    
    @Test @Order(16)
    void TC5_2_utenteSenzaOrdini() {
        // Login utente creato in 8.1
        openLogin();
        fillJS("email", TEST_EMAIL);
        fillJS("password", TEST_NEW_PASSWORD);
        submitForm();

        slow(1500);

        driver.get(BASE_URL + "/myorders");
        slow(1500);

        // Oracolo corretto: nessun elemento ordine presente
        Assertions.assertTrue(
            driver.findElements(By.className("order")).isEmpty(),
            "Sono presenti ordini per un utente che non dovrebbe averne"
        );

        // E NON deve essere rediretto al login
        Assertions.assertFalse(
            driver.getCurrentUrl().contains("login"),
            "Utente autenticato rediretto al login"
        );
    }
    
        @Test @Order(17)
        void TC5_3_ordiniNonAutenticato() {
            driver.get(BASE_URL + "/myorders");
            slow(1500);

            Assertions.assertTrue(
                driver.getCurrentUrl().contains("login"),
                "Utente non autenticato non rediretto al login"
            );
        }
    
    /* =====================
    8.6 VISUALIZZAZIONE CATALOGO
    ===================== */

 /**
  * TC6.2 – Catalogo visualizzato correttamente (utente non registrato)
  */
 @Test @Order(19)
 void TC6_2_catalogoGuest() {
     driver.get(BASE_URL + "/comics");
     slow(1500);

     // Verifica presenza di almeno un fumetto
     Assertions.assertFalse(
         driver.findElements(By.className("comic")).isEmpty(),
         "Catalogo non visibile per utente guest"
     );
 }

 /**
  * TC6.1 – Catalogo visualizzato correttamente (utente registrato)
  */
 @Test @Order(18)
 void TC6_1_catalogoUtenteRegistrato() {
     // Login (riuso test esistente)
     openLogin();
     fillJS("email", "obiwanreal@gmail.com");
     fillJS("password", "StarWars88");
     submitForm();

     slow(1200);

     driver.get(BASE_URL + "/comics");
     slow(1500);

     Assertions.assertFalse(
         driver.findElements(By.className("comic")).isEmpty(),
         "Catalogo non visibile per utente registrato"
     );
 }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}

