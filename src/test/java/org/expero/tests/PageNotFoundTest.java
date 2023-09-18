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
    public void takeALookButtonRedirectTo404Page() {
        driver.get(login_url);
        LoginPage loginPage = new LoginPage(driver);
        loginPage.clickOnTakeALook();

        // VERIFY I'M ON NOT FOUND PAGE
        NotFoundPage notFoundPage = new NotFoundPage(driver);
        assertEquals(ConfigReader.getProperty("not_found_page.header"), notFoundPage.getErrorHeader());
        // TODO: THIS DOES NOT WORK: If the user is not logged in, the "Take a look" button should redirect to a page with a preview of the tasks list.
    }


    @Test
    public void redirectsTo404PageWhenPageDoesNotExist() {
        Faker faker = new Faker();

        driver.get(baseURL + "/" + faker.cat().name());

        // VERIFY I'M ON NOT FOUND PAGE
        NotFoundPage notFoundPage = new NotFoundPage(driver);
        assertEquals(ConfigReader.getProperty("not_found_page.header"), notFoundPage.getErrorHeader());
    }

    @Test
    public void clickingGoBackHomeLinkRedirectsToHomePage() {
        driver.get(baseURL + "/invalid-url");

        NotFoundPage notFoundPage = new NotFoundPage(driver);
        notFoundPage.clickOnHGoBackHome();

        // The application should redirect the user to / when clicking on "Go back home".
        assertEquals(driver.getCurrentUrl(), baseURL + "/");
    }
}
