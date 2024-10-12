import com.example.ui_tests.pages.CardsPage;
import com.example.ui_tests.pages.DepositPage;
import com.example.ui_tests.pages.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

import static org.springframework.test.util.AssertionErrors.assertTrue;

public class DepositTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private DepositPage depositPage;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Implicit wait
        loginPage = new LoginPage(driver);
        depositPage = new DepositPage(driver);

        driver.get("http://localhost:3000/login");
        loginPage.enterEmail("exesilvestre@gmail.com");
        loginPage.enterPassword("Exequiel99.");
        loginPage.clickLogin();

        waitForPageLoad();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testDepositMoney() {
        String initialBalance = depositPage.getAvailableBalance();
        System.out.println("Saldo inicial: " + initialBalance);

        driver.get("http://localhost:3000/load-money");

        waitForPageLoad();

        depositPage.clickSelectCardLink();
        waitForSeconds(2);

        String lastFourDigits = "7945";
        depositPage.selectCardEndingWith(lastFourDigits);

        waitForSeconds(3);

        String amount = "1000";
        depositPage.enterAmount(amount);

        depositPage.clickConfirmButton();

        waitForPageLoad();

        String updatedBalance = depositPage.getAvailableBalance();
        System.out.println("Saldo actualizado: " + updatedBalance);

        assertTrue("El saldo no se ha actualizado correctamente.", !updatedBalance.equals(initialBalance));
    }

    private void waitForPageLoad() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void waitForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}