package org.expero.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtil {

    // Private constructor to prevent instantiation
    private LogUtil() {
    }

    // Inner static class that holds an instance of the Logger
    private static class LoggerHolder {
        private static final Logger LOGGER = LogManager.getLogger();
    }

    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }
}