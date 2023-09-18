package org.expero.tests;

import com.github.javafaker.Faker;
import org.expero.pages.Dashboard;
import org.expero.pages.LoginPage;
import org.expero.utilities.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class LoginTest extends BaseTest {

    private final String dashboard_url = ConfigReader.getProperty("baseURL") + "/dashboard";


    @Test
    public void successfulLoginAndLogout() {
        driver.get(login_url);

        String valid_user = ConfigReader.getProperty("test_user.email");
        String valid_password = ConfigReader.getProperty("test_user.password");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(valid_user, valid_password);

        Dashboard dashboard = new Dashboard(driver);
        // When entering valid credentials, the user should be redirected to the dashboard page.
        // When the user logs in to the Dashboard, it should show its username, twitter name, and profile image.
        assertEquals(ConfigReader.getProperty("test_user.name"), dashboard.getUserName());
        assertEquals(ConfigReader.getProperty("test_user.twittername"), dashboard.getTwitterName());
        assertTrue(dashboard.isProfilePictureDisplayed());

        // The user should be able to log out from the Dashboard.
        dashboard.clickOnLogout();
        assertEquals("Log in to your account", loginPage.getHeader());
    }


    @DataProvider(name = "invalidCredentials", parallel=true)
    public Object[][] provideInvalidCredentials() {
        return new Object[][]{
                {"test@gmail.com", "invalidPassword1"},
                {"expero@gmail.com", "SpiderMan99"},
                {"", "Password123"},
                {"admin@expero.com", ""}
        };
    }

    @Test(dataProvider = "invalidCredentials")
    public void loginWithInvalidCredentials(String email, String password) {
        driver.get(login_url);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(email, password);

        //WAIT 1 SECOND BEFORE CHECKING IF I AM STILL ON LOGIN PAGE
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // When entering invalid credentials, the user should not be able to login.
        assertEquals("Log in to your account", loginPage.getHeader());
    }

    @Test
    public void goToDashboardWithoutLogin() {
        driver.get(dashboard_url);

        LoginPage loginPage = new LoginPage(driver);
        assertEquals("Log in to your account", loginPage.getHeader());
    }

    // TODO: THERE IS A BUG
    @Test
    public void wrongPasswordFirstAndCorrect() {
        String email = ConfigReader.getProperty("test_user.email");
        String password = ConfigReader.getProperty("test_user.password");
        String name = ConfigReader.getProperty("test_user.name");

        driver.get(login_url);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(email, "123");

        // VERIFY I AM NOT LOGGED IN
        assertEquals("Log in to your account", loginPage.getHeader());

        loginPage.login(email, password);

        Dashboard dashboard = new Dashboard(driver);
        assertEquals(name, dashboard.getUserName());
    }
    @Test
    public void testErrorMessageWhenEmailIsBlank(){
        driver.get(login_url);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.clickOnLogin();

        String actualMessage = loginPage.getEmailValidationMessage();

        assertEquals(actualMessage, "Please fill out this field.");
;
    }
    @Test
    public void errorMessageWhenEmailHasInvalidFormat(){
        driver.get(login_url);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterEmail("test");
        loginPage.clickOnLogin();

        String actualMessage = loginPage.getEmailValidationMessage();

        // Email address format must be valid
        assertEquals(actualMessage, "Please include an '@' in the email address. 'test' is missing an '@'.");
    }
    @Test
    public void errorMessageWhenPasswordLessThen4Chars(){
        driver.get(login_url);

        Faker faker = new Faker();
        String randomEmail = faker.internet().emailAddress();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterEmail(randomEmail);
        loginPage.enterPassword("123");
        loginPage.clickOnLogin();

        String actualMessage = loginPage.getPasswordValidationMessage();

        // Password must have more than 3 characters length
        assertEquals(actualMessage, "Password must be at least 4 characters");
    }
}