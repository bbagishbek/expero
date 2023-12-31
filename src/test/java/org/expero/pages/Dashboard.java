package org.expero.pages;

import org.apache.logging.log4j.Logger;
import org.expero.utilities.DriverManager;
import org.expero.utilities.LogUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Dashboard {
    private final WebDriver driver;
    private static final Logger logger = LogUtil.getLogger(DriverManager.class);

    public Dashboard(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "span[data-testid='username']")
    private WebElement username;
    @FindBy(css = "span[data-testid='twitterName']")
    private WebElement twitterName;
    @FindBy(xpath = "//a[text()='New task']")
    private WebElement newTaskButton;
    @FindBy(xpath = "//a[text()='Logout']")
    private WebElement logoutButton;
    @FindBy(xpath = "//span[text()='Dashboard']")
    private WebElement dashboardButton;
    @FindBy(id = "task")
    private WebElement taskInputField;
    @FindBy(xpath = "//table[@class='min-w-full']")
    private WebElement table;
    @FindBy(css = "img[data-testid='profileImage']:first-child")
    private WebElement profilePicture;

    public void clickOnNewTask() {
        newTaskButton.click();
    }

    public void clickOnLogout() {
        logoutButton.click();
    }

    public void clickOnDashboardButton() {
        dashboardButton.click();
    }

    public void clickOnEditTask(String task) {
        int index = getListOfTasks().indexOf(task) + 1;
        WebElement editTaskButton = driver.findElement(By.cssSelector("a[href='/dashboard/edit/" + index + "']"));
        editTaskButton.click();
    }

    public void clickOnDeleteTask(String task) {
        int index = getListOfTasks().indexOf(task) + 1;
        WebElement deleteTaskButton = driver.findElement(By.xpath("//form[@action='/dashboard/" + index + "/delete']/button"));
        deleteTaskButton.click();
    }

    public void enterTaskName(String task) {
        taskInputField.clear();
        taskInputField.sendKeys(task);
    }

    public String getTaskInputValidationMsg() {
        return taskInputField.getAttribute("validationMessage");
    }

    public String getNotification() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='root']/div/p")));
        return element.getText();
    }

    public String getUserName() {
        return username.getText();
    }

    public String getTwitterName() {
        return twitterName.getText();
    }

    public List<String> getListOfTasks() {
        List<String> listOfTasks = new ArrayList<>();
        List<WebElement> rows = table.findElements(By.xpath("//tbody//tr"));

        for (WebElement row : rows) {
            WebElement taskElement = row.findElement(By.tagName("span"));
            listOfTasks.add(taskElement.getText());
        }
        return listOfTasks;
    }

    public List<WebElement> getTasks() {
        return driver.findElements(By.xpath("//td//span"));
    }

    public WebElement getTask(String task) {
        try {
            return driver.findElement(By.xpath("//td//span[text()='" + task + "']"));
        } catch (NoSuchElementException e) {
            logger.fatal("Task with name '" + task + "' was not found.");
            throw new RuntimeException("Task with name '" + task + "' was not found.", e);
        }
    }

    public boolean isProfilePictureDisplayed() {
        try {
            return profilePicture.isDisplayed();
        } catch (Exception e) {
            logger.debug("PROFILE PICTURE WAS NOT FOUND");
        }
        return false;
    }
}
