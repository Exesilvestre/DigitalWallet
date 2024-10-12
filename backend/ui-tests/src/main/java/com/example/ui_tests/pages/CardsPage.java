package com.example.ui_tests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CardsPage {
    private WebDriver driver;

    private By addCardButton = By.id("add-card-button");
    private By cardForm = By.id("PaymentForm"); // Cambiado el ID a "PaymentForm"
    private By numberField = By.id("outlined-adornment-number"); // Campo de número
    private By nameField = By.id("outlined-adornment-name"); // Campo de nombre
    private By expiryField = By.id("outlined-adornment-expiry"); // Campo de expiración
    private By cvcField = By.id("outlined-adornment-cvc"); // Campo de CVC
    private By submitButton = By.xpath("//button[contains(text(), 'Agregar')]"); // Botón para agregar la tarjeta

    public CardsPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isAddCardButtonDisplayed() {
        return driver.findElement(addCardButton).isDisplayed();
    }

    public void clickAddCardButton() {
        driver.findElement(addCardButton).click();
    }

    public boolean isCardFormDisplayed() {
        return driver.findElement(cardForm).isDisplayed();
    }

    public void enterCardNumber(String number) {
        driver.findElement(numberField).sendKeys(number);
    }

    public void enterCardName(String name) {
        driver.findElement(nameField).sendKeys(name);
    }

    public void enterCardExpiry(String expiry) {
        driver.findElement(expiryField).sendKeys(expiry);
    }

    public void enterCardCVC(String cvc) {
        driver.findElement(cvcField).sendKeys(cvc);
    }

    public void submitCard() {
        driver.findElement(submitButton).click();
    }
}
