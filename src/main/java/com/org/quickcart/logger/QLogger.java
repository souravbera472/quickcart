package com.org.quickcart.logger;

public class QLogger {

    private static final MyLogger logger = new ParentLogger();

    public static void info(Object message) {
        logger.info(message);
    }

    public static void error(Object error) {
        logger.error(error);
    }

    public static void error(Object error, Throwable throwable) {
        logger.error(error, throwable);
    }

    public static void warn(Object warning) {
        logger.warn(warning);
    }
}
