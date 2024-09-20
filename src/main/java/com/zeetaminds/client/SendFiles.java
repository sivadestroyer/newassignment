package com.zeetaminds.client;

import java.io.*;
import java.nio.file.Files;
import java.net.Socket;
import java.util.logging.Logger;
import java.util.logging.Level;

public class SendFiles {
    Logger logger = Logger.getLogger(SendFiles.class.getName());
    private Socket socket;
    private String fileName;

    public SendFiles(String fileName, Socket socket) {
        this.socket = socket;
        this.fileName = fileName;
    }

    public void sendFiles() {
        File file = new File(fileName);

        if (file.exists() && file.isFile()) {
            try {
                byte[] fileBytes = Files.readAllBytes(file.toPath()); // Use file.toPath()
                OutputStream os = socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);
                dos.writeLong(fileBytes.length); // Send file size first
                dos.write(fileBytes); // Send file data
                dos.flush();
                System.out.println("File sent successfully");
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error during file transfer", e);
            }
        } else {
            System.out.println("File not found: " + fileName);
        }
    }
}