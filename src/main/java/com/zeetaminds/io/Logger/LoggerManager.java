package com.zeetaminds.io.Logger;

public class LoggerManager {
    private Logger logger;
    public LoggerManager(Logger logger) {
        this.logger = logger;

    }
    public void setLogger(Logger logger) {
        this.logger = logger;
    }
    public void logMessage(String message) {
        logger.log(message);
    }


}
