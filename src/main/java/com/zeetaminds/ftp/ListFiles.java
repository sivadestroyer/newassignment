package com.zeetaminds.ftp;

import java.io.File;
import java.io.PrintWriter;

public class ListFiles {
    public void listFiles(PrintWriter writer) {
        File dir = new File(".");
        File[] files = dir.listFiles();

        if (files != null) {
            int numberOfFiles = 0;
            for (File file : files) {
                if (file.isFile()) {
                    numberOfFiles++;
                }
            }
            writer.println(numberOfFiles);
            for (File file : files) {
                if (file.isFile()) {
                    writer.println(file.getName());
                }
            }
        } else {
            writer.println("0");
        }

        System.out.println("List sent successfully");
    }
}
