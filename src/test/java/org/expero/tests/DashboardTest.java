package org.expero.tests;

import com.github.javafaker.Faker;
import org.expero.pages.Dashboard;
import org.expero.pages.LoginPage;
import org.expero.utilities.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.Random;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class DashboardTest extends BaseTest {

    @BeforeMethod
    public void setup() {
        driver.get(login_url);

        String valid_user = ConfigReader.getProperty("test_user.email");
        String valid_password = ConfigReader.getProperty("test_user.password");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(valid_user, valid_password);
    }

    @Test
    public void createNewTask() {
        // GENERATE A RANDOM TASK NAME
        Faker faker = new Faker();
        String taskName = faker.lordOfTheRings().character();

        Dashboard dashboardPage = new Dashboard(driver);
        dashboardPage.clickOnNewTask();

        dashboardPage.enterTaskName(taskName);
        dashboardPage.clickOnCreateTask();

        // VERIFY TASK IS DISPLAYED
        assertTrue(dashboardPage.getTask(taskName).isDisplayed());

        dashboardPage.clickOnDeleteTask(taskName);
    }

    @Test
    public void editExistingTask() {
        Faker faker = new Faker();
        String taskName = faker.backToTheFuture().character();

        Dashboard dashboardPage = new Dashboard(driver);

        List<String> allTasks = dashboardPage.getListOfTasks();

        String randomTask = allTasks.get(new Random().nextInt(allTasks.size()));

        dashboardPage.clickOnEditTask(randomTask);
        dashboardPage.enterTaskName(taskName);
        dashboardPage.clickOnSaveTask();

        assertTrue(dashboardPage.getTask(taskName).isDisplayed());
    }

    @Test
    public void deleteAllTasks() {
        List<WebElement> deleteButtons = driver.findElements(By.xpath("//button[text()='Delete']"));

        for (WebElement button : deleteButtons) {
            button.click();
        }

        // SET TIME OUT TO 1 SECOND, SO WE DON'T HAVE TO WAIT ALL DAY
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));

        List<WebElement> allTasks = driver.findElements(By.xpath("//td//span"));

        //LIST OF WEB ELEMENTS HAS TO BE EMPTY
        assertEquals(0, allTasks.size());
    }

    @Test
    public void dontSaveTheTaskCreation() {
        Dashboard dashboardPage = new Dashboard(driver);

        List<String> beforeTasks = dashboardPage.getListOfTasks();

        dashboardPage.clickOnNewTask();
        dashboardPage.enterTaskName("DONT SAVE");

        dashboardPage.clickOnDashboardButton();

        List<String> afterTasks = dashboardPage.getListOfTasks();

        assertEquals(beforeTasks, afterTasks);
    }

    @Test
    public void dontSaveTheTaskEdit() {
        Dashboard dashboardPage = new Dashboard(driver);

        List<String> beforeTasks = dashboardPage.getListOfTasks();
        String randomTask = beforeTasks.get(new Random().nextInt(beforeTasks.size()));

        dashboardPage.clickOnEditTask(randomTask);
        dashboardPage.enterTaskName("DONT SAVE");

        dashboardPage.clickOnDashboardButton();

        List<String> afterTasks = dashboardPage.getListOfTasks();

        assertEquals(beforeTasks, afterTasks);
    }
}
