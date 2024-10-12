import com.example.ui_tests.pages.CardsPage;
import com.example.ui_tests.pages.DashboardPage;
import com.example.ui_tests.pages.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Random;

import static org.springframework.test.util.AssertionErrors.assertTrue;

public class CardsTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;
    private CardsPage cardsPage;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Implicit wait
        loginPage = new LoginPage(driver);
        dashboardPage = new DashboardPage(driver);
        cardsPage = new CardsPage(driver);

        // Log in before each dashboard test
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
    public void testAddNewCard() {
        // Navegar a la página de tarjetas
        driver.get("http://localhost:3000/cards");

        // Verificar que el botón "Agregar tarjeta" esté visible
        assertTrue("El botón de agregar tarjeta no es visible", cardsPage.isAddCardButtonDisplayed());

        // Hacer clic en el botón "Agregar tarjeta"
        cardsPage.clickAddCardButton();

        // Verificar que el formulario de tarjeta esté visible
        assertTrue("El formulario de tarjeta no es visible", cardsPage.isCardFormDisplayed());

        // Generar los últimos 4 dígitos de la tarjeta de forma aleatoria
        Random random = new Random();
        int lastFourDigits = 1000 + random.nextInt(9000); // Generar un número aleatorio entre 1000 y 9999
        String cardNumber = "1234 5678 9012 " + lastFourDigits; // Crear el número completo de la tarjeta

        // Llenar el formulario de tarjeta usando métodos de CardsPage
        cardsPage.enterCardNumber(cardNumber);
        cardsPage.enterCardName("Exequiel Silvestre");
        cardsPage.enterCardExpiry("12/25");
        cardsPage.enterCardCVC("123");
        cardsPage.submitCard();

        // Esperar un poco después de enviar el formulario
        try {
            Thread.sleep(2000); // Espera de 2 segundos antes de redirigir
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Esperar a que el encabezado de la lista de tarjetas se vuelva visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(text(), 'Tus tarjetas')]"))); // Esperar el encabezado de la lista de tarjetas

        // Verificar que la tarjeta se haya agregado a la lista
        assertTrue("La tarjeta no se ha agregado a la lista.", isCardInList(lastFourDigits));
    }

    private boolean isCardInList(int lastFourDigits) {
        // Crear el selector usando el ID específico para el <p>
        By cardSelector = By.id("card-last-four-" + lastFourDigits);

        List<WebElement> elements = driver.findElements(cardSelector);
        if (elements.isEmpty()) {
            System.out.println("No card found for last four digits: " + lastFourDigits);
        } else {
            System.out.println("Found card: " + elements.get(0).getText());
        }
        return !elements.isEmpty();
    }

}
