package com.zeetaminds.client;

import com.zeetaminds.ftp.Command;
import java.io.*;
import java.nio.file.Files;
import java.util.logging.Logger;
import java.util.logging.Level;

public class SendFiles implements Command {
    Logger logger = Logger.getLogger(SendFiles.class.getName());
    private String fileName;

    private OutputStream out;
    public SendFiles(String fileName,OutputStream out) {

        this.fileName = fileName;

        this.out = out;
    }

    public void handle() {
        try {

            File file = new File(fileName);

            if (file.exists() && file.isFile()) {
                byte[] fileBytes = Files.readAllBytes(file.toPath()); // Use file.toPath()
                out.write(fileBytes); // Send file data
                out.flush();
            } else {
                // Send error message to the client if the file is not found
                logger.log(Level.SEVERE, "File doesnot exist: " + fileName);

            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error during file transfer", e);
        }
    }

}
