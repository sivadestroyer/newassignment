package com.zeetaminds.io.Logger;

public class ConsoleLogger extends Logger {
    public void log(String message) {
        System.out.println("Message logged to console: "+message);
    }

}