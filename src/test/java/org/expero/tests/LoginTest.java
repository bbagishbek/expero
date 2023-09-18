package org.expero.tests;

import org.expero.pages.Dashboard;
import org.expero.pages.LoginPage;
import org.expero.utilities.ConfigReader;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

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

        assertEquals(ConfigReader.getProperty("test_user.name"), dashboard.getUserName());
        assertEquals(ConfigReader.getProperty("test_user.twittername"), dashboard.getTwitterName());

        dashboard.clickOnLogout();
        assertEquals("Log in to your account", loginPage.getHeader());
    }


    @DataProvider(name = "invalidCredentials")
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
}
