package org.expero.tests;

import com.github.javafaker.Faker;
import org.expero.pages.LoginPage;
import org.expero.pages.NotFoundPage;
import org.expero.utilities.ConfigReader;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class PageNotFoundTest extends BaseTest {
    private final String baseURL = ConfigReader.getProperty("baseURL");

    @Test
    public void takeALookButtonRedirectToPageNotFound() {
        driver.get(login_url);
        LoginPage loginPage = new LoginPage(driver);
        loginPage.clickOnTakeALook();

        // VERIFY I'M ON NOT FOUND PAGE
        NotFoundPage notFoundPage = new NotFoundPage(driver);
        assertEquals(ConfigReader.getProperty("not_found_page.header"), notFoundPage.getErrorHeader());
    }


    @Test
    public void visitNonExistingPages() {
        Faker faker = new Faker();

        driver.get(baseURL + "/" + faker.cat().name());

        // VERIFY I'M ON NOT FOUND PAGE
        NotFoundPage notFoundPage = new NotFoundPage(driver);
        assertEquals(ConfigReader.getProperty("not_found_page.header"), notFoundPage.getErrorHeader());
    }

    @Test
    public void navigateBackToHomePageFrom404() {
        driver.get(baseURL + "/invalid-url");

        NotFoundPage notFoundPage = new NotFoundPage(driver);
        notFoundPage.clickOnHGoBackHome();

        LoginPage loginPage = new LoginPage(driver);

        // VERIFY I'M ON LOGIN PAGE
        assertEquals("Log in to your account", loginPage.getHeader());
    }
}
