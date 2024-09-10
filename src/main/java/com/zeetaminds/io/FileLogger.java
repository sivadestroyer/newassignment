package com.zeetaminds.io;
import java.io.FileWriter;
import java.io.IOException;
public class FileLogger extends Logger {
    private String filename;
    public FileLogger(String filename) {
        this.filename= filename;

    }
    public void log(String message) {
        try(FileWriter writer = new FileWriter(filename,true)){
            writer.write(message+"\n");
            System.out.println("Message logged to file: "+filename);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
