package org.expero.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static final Properties properties;

    static {
        properties = new Properties();
        try (FileInputStream configFile = new FileInputStream("src/test/resources/config.properties")) {
            properties.load(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        String propertyValue = properties.getProperty(key);
        if (propertyValue == null || propertyValue.trim().isEmpty()) {
            throw new IllegalArgumentException("Property " + key + " not found or empty in config.properties.");
        }
        return propertyValue;
    }
}
