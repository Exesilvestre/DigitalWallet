
import com.example.ui_tests.pages.DashboardPage;
import com.example.ui_tests.pages.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DashboardTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Implicit wait
        loginPage = new LoginPage(driver);
        dashboardPage = new DashboardPage(driver);

        // Realiza el login antes de cada prueba del dashboard
        driver.get("http://localhost:3000/login");
        loginPage.enterEmail("exesilvestre@gmail.com");
        loginPage.enterPassword("Exequiel99.");
        loginPage.clickLogin();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }


    @Test
    public void testNavbarUserGreeting() {
        // Verifica que el saludo en el Navbar es correcto
        String expectedGreeting = "ESHOLA, EXEQUIEL SILVESTRE"; // Saludo esperado
        String actualGreeting = dashboardPage.getGreeting().replace("\n", "").trim(); // Elimina saltos de línea y espacios


        assertEquals(expectedGreeting, actualGreeting, "El saludo en el Navbar no es el esperado.");
    }
}