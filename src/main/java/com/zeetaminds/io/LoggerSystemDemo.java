package com.zeetaminds.io;
public class LoggerSystemDemo {
    public static void main(String[] args) {
        // FileLogger logs to "log.txt"
        Logger fileLogger = new FileLogger("/home/sivabala/IdeaProjects/newassignment/src/main/java/com/zeetaminds/io/log.txt");

        // TimestampedFileLogger logs to "timestamped_log.txt"
        Logger timestampedLogger = new TimeStampedFileLogger("/home/sivabala/IdeaProjects/newassignment/src/main/java/com/zeetaminds/io/timestamped_log.txt");

        // ConsoleLogger logs to the console
        Logger consoleLogger = new ConsoleLogger();

        // LoggerManager to manage the current logger
        LoggerManager loggerManager = new LoggerManager(fileLogger);

        // Log a series of messages using FileLogger
        System.out.println("Logging using FileLogger...");
        loggerManager.logMessage("FileLogger: First log message");
        loggerManager.logMessage("FileLogger: Second log message");

        // Switch to TimestampedFileLogger
        loggerManager.setLogger(timestampedLogger);
        System.out.println("Logging using TimestampedFileLogger...");
        loggerManager.logMessage("TimestampedFileLogger: First log message");
        loggerManager.logMessage("TimestampedFileLogger: Second log message");

        // Switch to ConsoleLogger
        loggerManager.setLogger(consoleLogger);
        System.out.println("Logging using ConsoleLogger...");
        loggerManager.logMessage("ConsoleLogger: First log message");
        loggerManager.logMessage("ConsoleLogger: Second log message");
    }
}
