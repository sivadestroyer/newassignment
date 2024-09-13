package com.zeetaminds.io.Logger;

import java.io.FileWriter;

public class FileLogger extends Logger {

    private final String filename;

    public FileLogger(String filename) {
        this.filename = filename;
    }

    public void log(String message) {
        try (FileWriter writer = new FileWriter(filename, true)) {
            writer.write(message + "\n");
            System.out.println("Message logged to file: " + filename);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
