package com.example.ui_tests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class DepositPage {
    private WebDriver driver;

    // Selectores para los elementos de la página de depósito
    private By selectCardLink = By.id("select-card-link"); // Selector para el enlace "Seleccionar tarjeta"
    private By cardSelectButtons = By.cssSelector("button.tw-text-primary"); // Selector para los botones "Seleccionar" de las tarjetas
    private By moneyInput = By.id("outlined-adornment-money"); // Selector para el campo de entrada de monto
    private By confirmButton = By.id("confirm-button"); // Selector para el botón "Confirmar"
    private By availableBalance = By.id("available-balance"); // Selector para el saldo disponible

    public DepositPage(WebDriver driver) {
        this.driver = driver;
    }

    // Método para hacer clic en el enlace "Seleccionar tarjeta"
    public void clickSelectCardLink() {
        driver.findElement(selectCardLink).click();
    }

    public void selectCardEndingWith(String lastFourDigits) {
        By cardSelector = By.id("card-last-four-" + lastFourDigits);
        List<WebElement> elements = driver.findElements(cardSelector);

        if (!elements.isEmpty()) {
            WebElement cardElement = elements.get(0);
            WebElement selectButton = cardElement.findElement(By.xpath("following::button[contains(text(), 'Seleccionar')]"));

            // Hacer clic en el botón "Seleccionar"
            selectButton.click();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            wait.until(ExpectedConditions.visibilityOfElementLocated(confirmButton));
        } else {
            System.out.println("No se encontró la tarjeta con los últimos cuatro dígitos: " + lastFourDigits);
        }
    }

    public void clickConfirmButton() {
        // Asegúrate de que el campo de entrada de monto pierda el foco
        WebElement amountField = driver.findElement(moneyInput);
        ((JavascriptExecutor) driver).executeScript("arguments[0].blur();", amountField);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(confirmButton));
        button.click(); // Hacer clic en el botón "Confirmar"
    }

    // Método para ingresar el monto en el campo de entrada
    public void enterAmount(String amount) {
        WebElement amountField = driver.findElement(moneyInput);
        amountField.clear(); // Limpiar el campo antes de ingresar el monto
        amountField.sendKeys(amount); // Ingresar el monto
    }


    // Método para capturar el saldo disponible antes del depósito
    public String getAvailableBalance() {
        return driver.findElement(availableBalance).getText(); // Obtener el saldo disponible
    }
}