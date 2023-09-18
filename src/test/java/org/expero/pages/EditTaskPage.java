package org.expero.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class EditTaskPage {
    public EditTaskPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//button[text()='Save']")
    private WebElement saveTaskButton;
    @FindBy(id = "task")
    private WebElement taskInputField;

    public void enterTaskName(String task) {
        taskInputField.clear();
        taskInputField.sendKeys(task);
    }

    public void saveTask() {
        saveTaskButton.click();
    }
}
