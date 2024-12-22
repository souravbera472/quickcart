package com.org.quickcart.logger;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParentLogger implements MyLogger {
    private static final Logger logger = LogManager.getLogger(ParentLogger.class.getName());

    /*static {
        System.setProperty("log4j.configurationFile", "log4j2-A.xml");
        System.setProperty("Log4jContextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
    }*/

    private int line = 0;
    @Override
    public void info(Object message) {
        Logger logger = getLogger();
        if (logger.isInfoEnabled()) {
            logger.log(Level.INFO, (message == null) ? "null" : "("+line+") " +message.toString());
        }

    }

    @Override
    public void error(Object message) {
        Logger logger = getLogger();
        if (logger.isErrorEnabled()) {
            logger.error((message == null) ? "null" : "("+line+") " +message.toString(), Level.ERROR);
        }
    }

    @Override
    public void error(Object message, Throwable object) {
        Logger logger = getLogger();
        if (logger.isInfoEnabled()) {
            logger.error((message == null) ? "null" : "("+line+") " +message.toString(), Level.ERROR, object);
        }
    }

    @Override
    public void warn(Object message) {
        Logger logger = getLogger();
        if (logger.isInfoEnabled()) {
            logger.warn((message == null) ? "null" : "("+line+") " +message.toString(), Level.WARN);
        }
    }

    private Logger getLogger() {
        StackTraceElement[] elements = new Throwable().getStackTrace();
        if (elements.length >= 3 && elements[3].getClassName() != null) {
            line = elements[3].getLineNumber();
            return LogManager.getLogger(elements[3].getClassName()+"."+elements[3].getMethodName());
        } else {
            return logger;
        }
    }
}
