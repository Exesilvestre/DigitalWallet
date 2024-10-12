package com.example.ui_tests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private final WebDriver driver;

    // Selectores de elementos
    private By emailField = By.id("login-email"); // ID del campo de correo
    private By passwordField = By.id("login-password"); // ID del campo de contraseña
    private By loginButton = By.xpath("//button[@type='submit']"); // Selector para el botón de inicio de sesión
    private By errorMessage = By.id("error-message"); // ID de mensaje de error si existe

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterEmail(String email) {
        driver.findElement(emailField).sendKeys(email);
        waitFor(1000);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
        waitFor(1000);
    }

    public void clickLogin() {
        driver.findElement(loginButton).click();
        waitFor(2000);
    }

    public String getErrorMessage() {
        return driver.findElement(errorMessage).getText();
    }

    // Método para manejar las esperas
    private void waitFor(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}