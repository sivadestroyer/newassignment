package com.zeetaminds.client;

import com.zeetaminds.ftp.Command;

import java.io.*;
import java.nio.file.Files;
import java.net.Socket;
import java.util.logging.Logger;
import java.util.logging.Level;

public class SendFiles implements Command {
    Logger logger = Logger.getLogger(SendFiles.class.getName());
    private Socket socket;
    private String fileName;

    public SendFiles(String fileName, Socket socket) {
        this.socket = socket;
        this.fileName = fileName;
    }

    public void handle() {
        try {
            OutputStream os = socket.getOutputStream();
            File file = new File(fileName);

            if (file.exists() && file.isFile()) {
                byte[] fileBytes = Files.readAllBytes(file.toPath()); // Use file.toPath()
                os.write(fileBytes); // Send file data
                os.flush();
                System.out.println("File sent successfully");
            } else {
                // Send error message to the client if the file is not found
                System.out.println("File not found: " + fileName);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error during file transfer", e);
        }
    }

}
