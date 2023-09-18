package org.expero.tests;

import com.github.javafaker.Faker;
import org.expero.pages.*;
import org.expero.utilities.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Arrays;
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
    public void verifyDefaultTaskList() {
        Dashboard dashboardPage = new Dashboard(driver);
        List<String> actualList = dashboardPage.getListOfTasks();
        List<String> expectedList = Arrays.asList("Go to the store", "Wash the dishes", "Read a book");
        //The dashboard page should show a list with the default tasks.
        assertEquals(expectedList, actualList);
    }

    @Test
    public void createNewTask() {
        // GENERATE A RANDOM TASK NAME
        Faker faker = new Faker();
        String taskName = faker.lordOfTheRings().character();

        Dashboard dashboardPage = new Dashboard(driver);
        dashboardPage.clickOnNewTask();

        NewTaskPage newTaskPage = new NewTaskPage(driver);
        newTaskPage.enterTaskName(taskName);
        newTaskPage.clickOnCreate();

        // VERIFY NOTIFICATION IS DISPLAYED
        assertTrue(dashboardPage.getNotification().contains("Task created"));

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

        EditTaskPage editTaskPage = new EditTaskPage(driver);
        editTaskPage.enterTaskName(taskName);
        editTaskPage.saveTask();

        // VERIFY NOTIFICATION IS DISPLAYED
        assertTrue(dashboardPage.getNotification().contains("Task updated"));

        assertTrue(dashboardPage.getTask(taskName).isDisplayed());
    }

    @Test
    public void deleteAllTasks() {
        List<WebElement> deleteButtons = driver.findElements(By.xpath("//button[text()='Delete']"));

        Dashboard dashboardPage = new Dashboard(driver);

        for (WebElement button : deleteButtons) {
            button.click();
            // VERIFY NOTIFICATION IS DISPLAYED
            assertTrue(dashboardPage.getNotification().contains("Task deleted"));
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

    @Test
    public void checkTaskDetails() {
        Dashboard dashboardPage = new Dashboard(driver);

        List<WebElement> allTasks = dashboardPage.getTasks();

        WebElement task = allTasks.get(0);
        String taskName = task.getText();

        task.click();

        DetailsPage detailsPage = new DetailsPage(driver);
        int actual_id = Integer.parseInt(detailsPage.getTaskId().split(" ")[1]);

        assertEquals(detailsPage.getTaskName(), taskName);

        assertEquals(actual_id, 1);
    }

    @Test
    public void errorWhenSubmittingEmptyTask() {
        Dashboard dashboardPage = new Dashboard(driver);
        dashboardPage.clickOnNewTask();

        NewTaskPage newTaskPage = new NewTaskPage(driver);
        newTaskPage.clickOnCreate();

        String actual_message = dashboardPage.getTaskInputValidationMsg();

        assertEquals(actual_message, "Please fill out this field.");
    }
}