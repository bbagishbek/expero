package org.expero.tests;

import org.apache.logging.log4j.Logger;
import org.expero.utilities.ConfigReader;
import org.expero.utilities.DriverManager;
import org.expero.utilities.LogUtil;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;


public class BaseTest {
    protected WebDriver driver;
    protected final String login_url = ConfigReader.getProperty("baseURL");
    private static final Logger logger = LogUtil.getLogger(DriverManager.class);

    @BeforeMethod
    public void setUp() {
        driver = DriverManager.getInstance().getDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        logger.info("DRIVER INITIALIZED");
    }

    @AfterMethod
    public void tearDown() {
        DriverManager.getInstance().close();
        logger.info("DRIVER CLOSED");
    }
}