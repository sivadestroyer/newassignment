package com.zeetaminds.io;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;

public class FileTraversal {

    public static void listFiles(File directory, String extension) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(extension)) {
                    System.out.println("File: " + file.getName());
                    System.out.println(file.getAbsolutePath());
                }
            }
        }
    }

    public static void searchFilesBYkeyword(File file, String keyword) {
        File[] files = file.listFiles();
        if (files != null) {
            for (File file2 : files) {
                if (file2.isFile() && file.canRead()) {
                    if (fileContainskey(file2, keyword)) {
                        System.out.println("File: " + file2.getName());
                        System.out.println(file2.getAbsolutePath());
                    }
                }
            }
        }
    }

    public static boolean fileContainskey(File file, String key) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(key)) {
                    return true;
                }
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static void main(String[] args) {
        String directoryPath = "/home/sivabala/Documents";
        String extension = ".txt";
        String keyword = "sivabalamurugan";
        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            //listFiles(directory, extension);
            searchFilesBYkeyword(directory, keyword);
        } else {
            System.out.println("the pathe is not valid");
        }
    }

}
