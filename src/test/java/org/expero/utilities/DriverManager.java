package org.expero.utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.units.qual.C;
import org.expero.enums.SeleniumEnvironment;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.expero.enums.DriverType;

public class DriverManager {
    private final DriverType driverType;
    private final SeleniumEnvironment envType;
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private static DriverManager instance = null;
    private static final Logger logger = LogUtil.getLogger(DriverManager.class);

    private DriverManager() {
        String driverTypeProperty = ConfigReader.getProperty("driverType").toUpperCase();
        String environmentProperty = ConfigReader.getProperty("selenium_environment").toUpperCase();

        this.driverType = DriverType.valueOf(driverTypeProperty);
        this.envType = SeleniumEnvironment.valueOf(environmentProperty);

    }

    public static synchronized DriverManager getInstance() {
        if (instance == null) {
            instance = new DriverManager();
        }
        return instance;
    }

    public WebDriver getDriver() {
        if (driver.get() == null) {
            driver.set((envType == SeleniumEnvironment.LOCAL) ? getLocalDriver() : getRemoteDriver());
        }
        return driver.get();
    }

    private WebDriver getLocalDriver() {
        switch (driverType) {
            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                return new FirefoxDriver();

            case SAFARI:
                WebDriverManager.safaridriver().setup();
                return new SafariDriver();

            case CHROME:
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--headless");
                return new ChromeDriver();

            default:
                logger.fatal("Unsupported DriverType: " + driverType);
                throw new IllegalArgumentException("Unsupported DriverType: " + driverType);
        }
    }

    private WebDriver getRemoteDriver() {
        logger.fatal("REMOTE DRIVER IS NOT YET IMPLEMENTED");
        throw new UnsupportedOperationException("REMOTE DRIVER IS NOT YET IMPLEMENTED");
    }

    public void close() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}