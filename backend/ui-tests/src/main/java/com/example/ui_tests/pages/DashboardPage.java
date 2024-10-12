package com.example.ui_tests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DashboardPage {
    private WebDriver driver;

    private By greetingButton = By.xpath("//button[contains(text(), 'Hola,')]"); // Selector para el botón del saludo

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
    }


    // Nuevo método para obtener el saludo del Navbar
    public String getGreeting() {
        return driver.findElement(greetingButton).getText();
    }

    public boolean isGreetingDisplayed() {
        return driver.findElement(greetingButton).isDisplayed();
    }
}
