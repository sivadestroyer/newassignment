package com.zeetaminds.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileCopy {


    public static void main(String[] args) {
        // Your implementation here
        String SourceFile = "/home/sivabala/Documents/sample.txt";
        String DestinationFile = "/home/sivabala/Documents/sample5_copy.txt";

        long startTime = System.currentTimeMillis();
        try (FileInputStream fis = new FileInputStream(SourceFile);
             FileOutputStream fos = new FileOutputStream(DestinationFile)) {
                int i;
                while ((i=fis.read())!= -1) {
                    fos.write(i);
                }

            System.out.println("File copied successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime =System.currentTimeMillis();
        long time_taken = endTime - startTime;
        System.out.println(time_taken + " ms");
    try {
        FileOutputStream fout = new FileOutputStream("/home/sivabala/Documents/sample2_copy.txt");
        String s = "welcome to file input and output";
        byte b[] = s.getBytes();
        fout.write(b);
        fout.close();
    }catch(Exception e){
        System.out.println(e);
    }
        }
    }


