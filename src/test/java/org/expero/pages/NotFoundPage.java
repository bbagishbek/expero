package org.expero.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class NotFoundPage {
    public NotFoundPage(WebDriver driver){
        PageFactory.initElements(driver, this);
    }

    @FindBy(tagName = "h1")
    private WebElement errorHeader;
    @FindBy(xpath = "//a[text()='Go back home']")
    private WebElement goBackHomeButton;

    public void clickOnHGoBackHome(){
        goBackHomeButton.click();
    }
    public String getErrorHeader(){
        return errorHeader.getText();
    }
}
