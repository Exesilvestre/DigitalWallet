package com.example.ui_tests.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class TransferPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public TransferPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Espera explícita
    }

    public void goToSendMoneyPage() {
        driver.get("http://localhost:3000/send-money");
    }

    public void clickNewAccount() throws InterruptedException {
        Thread.sleep(2000); // Retraso de 2 segundos
        WebElement newAccountButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("new-account")));
        newAccountButton.click();
    }

    public void enterAlias(String alias) throws InterruptedException {
        WebElement aliasInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[text()='CVU ó Alias']/following::input[1]")));
        aliasInput.clear();
        aliasInput.sendKeys(alias);
        aliasInput.sendKeys(Keys.TAB); // Presionamos TAB para mover el foco

        // Espera antes de hacer clic en el botón "Continuar"
        waitUntilClickableAndClick(By.xpath("//button[contains(text(), 'Continuar')]"));
    }

    public void enterAmount(String amount) throws InterruptedException {
        // Esperamos a que el campo de monto sea visible
        WebElement amountInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[text()='Monto']/following::input[1]")));
        amountInput.clear(); // Limpiamos el campo antes de ingresar el monto
        amountInput.sendKeys(amount); // Ingresamos el monto
        amountInput.sendKeys(Keys.TAB); // Presionamos TAB para mover el foco

        // Espera y clic en el botón "Continuar"
        waitUntilClickableAndClick(By.xpath("//button[contains(text(), 'Continuar')]"));
    }

    private void waitUntilClickableAndClick(By locator) throws InterruptedException {
        Thread.sleep(2000);
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(locator));
        button.click();
    }

    public void submitAmount() throws InterruptedException {
        Thread.sleep(2000);
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("submit-amount")));
        submitButton.click();
    }

    public boolean confirmTransfer() {
        WebElement transferButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Transferir')]")));


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        transferButton.click();

        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("confirm-transfer")));

        // Verificar que el mensaje de éxito esté visible
        boolean isSuccessDisplayed = successMessage.isDisplayed();

        if (isSuccessDisplayed) {
            WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Continuar')]")));


            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            continueButton.click();
        }

        return isSuccessDisplayed;
    }
}
