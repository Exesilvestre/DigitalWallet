import com.example.ui_tests.pages.LoginPage;
import com.example.ui_tests.pages.TransferPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransferTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private TransferPage transferPage;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Tiempo de espera implícito
        loginPage = new LoginPage(driver);
        transferPage = new TransferPage(driver);

        // Iniciar sesión antes de cada prueba de transferencia
        driver.get("http://localhost:3000/login");
        loginPage.enterEmail("exesilvestre@gmail.com");
        loginPage.enterPassword("Exequiel99.");
        loginPage.clickLogin();
    }

    @Test
    public void testTransfer() throws InterruptedException {
        // Paso 1: Navegar a la página de transferencia
        transferPage.goToSendMoneyPage();

        // Paso 2: Hacer clic en "Nueva Cuenta"
        transferPage.clickNewAccount();

        // Paso 3: Ingresar alias
        transferPage.enterAlias("película.primavera.vida");

        // Paso 4: Ingresar monto
        transferPage.enterAmount("100");

        // Paso 5: Confirmar la transferencia
        boolean isTransferSuccessful = transferPage.confirmTransfer();

        // Paso 6: Verificar que la transferencia fue exitosa
        assertTrue(isTransferSuccessful, "La transferencia no fue exitosa");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
