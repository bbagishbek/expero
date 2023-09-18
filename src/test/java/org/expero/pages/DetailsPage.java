package org.expero.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.devtools.v85.page.Page;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DetailsPage {
    public DetailsPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "p.text-orange-500.mt-4")
    private WebElement taskName;
    @FindBy(css = "h1.text-xl.bold")
    private WebElement taskID;

    public String getTaskName(){
        return taskName.getText();
    }
    public String getTaskId(){
        return taskID.getText();
    }
}

