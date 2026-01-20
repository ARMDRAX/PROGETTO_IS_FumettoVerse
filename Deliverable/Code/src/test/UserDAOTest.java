package test;

import model.User;
import model.UserDAO;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {

    private UserDAO userDAO;
    private String testEmail;
    private final String TEST_PASSWORD = "Password123";
    private final String SECURITY_QUESTION = "pet";
    private final String SECURITY_ANSWER = "R2D2";

    @BeforeEach
    void setUp() {
        userDAO = new UserDAO();
        testEmail = "user_test_" + System.currentTimeMillis() + "@mail.com";
    }

    /* ============================
       REGISTRAZIONE UTENTE
       ============================ */

    @Test
    void testRegisterUser() throws Exception {

        userDAO.registerUser(
                "TestUser",
                testEmail,
                TEST_PASSWORD,
                SECURITY_QUESTION,
                SECURITY_ANSWER
        );

        // Verifica indiretta: login possibile
        User user = userDAO.doLogin(testEmail, TEST_PASSWORD);
        assertNotNull(user, "Utente registrato ma non autenticabile");
    }

    /* ============================
       LOGIN
       ============================ */

    @Test
    void testLoginCorretto() throws Exception {
        User user = userDAO.doLogin("obiwanreal@gmail.com", "StarWars88");

        assertNotNull(user, "Login valido non riconosciuto");
        assertEquals("obiwanreal@gmail.com", user.getEmail());
    }

    @Test
    void testLoginPasswordErrata() throws Exception {
        User user = userDAO.doLogin("obiwanreal@gmail.com", "WRONGPASS");

        assertNull(user, "Login con password errata accettato");
    }

    @Test
    void testLoginEmailNonPresente() throws Exception {
        User user = userDAO.doLogin("inesistente_" + System.currentTimeMillis() + "@mail.com", "Qualsiasi");

        assertNull(user, "Login con email inesistente accettato");
    }

    /* ============================
       DOMANDA DI SICUREZZA
       ============================ */

    @Test
    void testGetSecurityQuestionByEmail() throws Exception {
        String question = userDAO.getSecurityQuestionByEmail("obiwanreal@gmail.com");

        assertNotNull(question, "Domanda di sicurezza non trovata");
    }

    /* ============================
       RESET PASSWORD
       ============================ */

    @Test
    void testResetPasswordWithCorrectAnswer() throws Exception {

        // Reset password
        boolean updated = userDAO.resetPasswordWithSecurityAnswer(
                testEmail,
                SECURITY_ANSWER,
                "NuovaPassword88"
        );

        // Può fallire se l’utente non è quello appena creato
        // quindi prima lo registriamo se serve
        if (!updated) {
            userDAO.registerUser(
                    "ResetUser",
                    testEmail,
                    TEST_PASSWORD,
                    SECURITY_QUESTION,
                    SECURITY_ANSWER
            );

            updated = userDAO.resetPasswordWithSecurityAnswer(
                    testEmail,
                    SECURITY_ANSWER,
                    "NuovaPassword88"
            );
        }

        assertTrue(updated, "Reset password con risposta corretta fallito");

        // Verifica login con nuova password
        User user = userDAO.doLogin(testEmail, "NuovaPassword88");
        assertNotNull(user, "Login con nuova password fallito");
    }

    @Test
    void testResetPasswordWithWrongAnswer() throws Exception {

        boolean updated = userDAO.resetPasswordWithSecurityAnswer(
                "obiwanreal@gmail.com",
                "RISPOSTA_SBAGLIATA",
                "PasswordQualsiasi"
        );

        assertFalse(updated, "Reset password con risposta errata accettato");
    }
}
