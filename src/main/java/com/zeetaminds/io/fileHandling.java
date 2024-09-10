package com.zeetaminds.io;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class fileHandling {
    public static void printKeyValue(String path) {
        Properties prop = new Properties();
        try (FileInputStream input = new FileInputStream(path)) {
            prop.load(input);
            input.close();
            for (String key : prop.stringPropertyNames()) {
                String value = prop.getProperty(key);
                System.out.println("key: " + key + " value: " + value);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updateProperty(String path, String key, String value) {

        Properties prop = new Properties();
        try (FileInputStream input = new FileInputStream(path)) {
            prop.load(input);
            prop.setProperty(key, value);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try (FileOutputStream output = new FileOutputStream(path)) {
            prop.store(output, null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        String propertiesPath = "/home/sivabala/IdeaProjects/newassignment/src/main/java/com/zeetaminds/io/config.properties";
        printKeyValue(propertiesPath);
        updateProperty(propertiesPath, "newKey", "changeingvalue");
        printKeyValue(propertiesPath);
    }
}
