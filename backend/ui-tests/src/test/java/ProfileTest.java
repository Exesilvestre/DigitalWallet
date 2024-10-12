import com.example.ui_tests.pages.LoginPage;
import com.example.ui_tests.pages.ProfilePage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

import static org.springframework.test.util.AssertionErrors.assertTrue;

public class ProfileTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private ProfilePage profilePage;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Implicit wait
        loginPage = new LoginPage(driver);
        profilePage = new ProfilePage(driver);

        // Log in before the profile test
        driver.get("http://localhost:3000/login");
        loginPage.enterEmail("exesilvestre@gmail.com");
        loginPage.enterPassword("Exequiel99.");
        loginPage.clickLogin();

        // Wait for the Dashboard to load
        waitForPageLoad();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testModifyAlias() {
        profilePage.navigateToProfile();

        String currentAlias = profilePage.getAlias();
        System.out.println("Alias actual: " + currentAlias);

        String newAlias = "comida.bosque.luna";
        profilePage.clickEditAlias();

        profilePage.changeAlias(newAlias);

        String updatedAlias = profilePage.getAlias();
        System.out.println("Alias actualizado: " + updatedAlias);

        assertTrue("El alias no se ha actualizado correctamente.", updatedAlias.equals(newAlias));
    }

    private void waitForPageLoad() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
