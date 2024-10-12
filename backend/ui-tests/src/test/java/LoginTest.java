import com.example.ui_tests.pages.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTest {
    private WebDriver driver;
    private LoginPage loginPage;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Implicit wait
        loginPage = new LoginPage(driver); // Inicializa la página de login
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testLogin() {
        driver.get("http://localhost:3000/login");

        // Realiza el login
        loginPage.enterEmail("exesilvestre@gmail.com");
        loginPage.enterPassword("Exequiel99.");
        loginPage.clickLogin();

        // Verifica que se redirige al dashboard
        String expectedDashboardUrl = "http://localhost:3000/";
        String actualUrl = driver.getCurrentUrl();
        assertEquals(expectedDashboardUrl, actualUrl, "La URL después del inicio de sesión no es la esperada.");
    }
}