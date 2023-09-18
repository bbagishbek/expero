package org.expero.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "email-address")
    private WebElement emailField;

    @FindBy(id = "password")
    private WebElement passwordField;
    @FindBy(xpath = "//button[text()='Log in']")
    private WebElement loginButton;

    @FindBy(xpath = "//a[text()='Or take a look']")
    private WebElement quickLookButton;
    @FindBy(xpath = "//h2[text()='Log in to your account']")
    private WebElement header;


    public void clickOnTakeALook() {
        quickLookButton.click();
    }

    public void clickOnLogin() {
        loginButton.click();
    }

    public void enterEmail(String email) {
        emailField.clear();
        emailField.sendKeys(email);
    }

    public void enterPassword(String password) {
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public String getHeader() {
        return header.getText();
    }

    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickOnLogin();
    }
}
