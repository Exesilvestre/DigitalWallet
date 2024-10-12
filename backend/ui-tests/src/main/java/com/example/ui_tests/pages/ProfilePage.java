package com.example.ui_tests.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProfilePage {
    private WebDriver driver;

    // Elementos
    private By aliasField = By.id("outlined-adornment-alias"); // Campo de entrada del alias
    private By confirmButton = By.xpath("//button[contains(text(), 'Confirmar')]"); // Botón para confirmar cambios
    private By aliasText = By.id("user-alias"); // Elemento que muestra el alias actual
    private By editIcon = By.xpath("//a[contains(@href, 'profile?edit')]"); // Icono o enlace para editar el perfil

    public ProfilePage(WebDriver driver) {
        this.driver = driver;
    }

    // Método para navegar a la página de perfil
    public void navigateToProfile() {
        driver.get("http://localhost:3000/profile");
        waitForPageLoad();
    }

    // Método para iniciar la edición del alias
    public void clickEditAlias() {
        WebElement editButton = driver.findElement(editIcon);
        editButton.click();
        waitForPageLoad();
    }

    private WebElement waitForVisibility(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void changeAlias(String newAlias) {
        // Acceder al campo de entrada del alias
        WebElement aliasInput = waitForVisibility(aliasField); // Esperar hasta que el campo sea visible

        ((JavascriptExecutor) driver).executeScript("arguments[0].select();", aliasInput);

        // Cortar el texto seleccionado
        aliasInput.sendKeys(Keys.chord(Keys.CONTROL, "x"));

        // Escribir el nuevo alias
        aliasInput.sendKeys(newAlias);

        // Hacer clic fuera del campo de entrada para habilitar el botón de confirmación
        WebElement someOtherElement = driver.findElement(By.xpath("//body"));
        someOtherElement.click();

        // Confirmar el cambio
        WebElement confirmBtn = waitForVisibility(confirmButton);
        confirmBtn.click();
        waitForPageLoad();
    }

    public String getAlias() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        WebElement aliasElement = wait.until(ExpectedConditions.presenceOfElementLocated(aliasText)); // Espera hasta que el elemento esté presente

        // Usar JavaScriptExecutor para asegurarse de que el elemento sea visible
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", aliasElement);

        return aliasElement.getText();
    }

    // Método para esperar el tiempo de carga de la página
    private void waitForPageLoad() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
