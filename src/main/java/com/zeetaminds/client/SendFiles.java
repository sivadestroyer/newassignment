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
        try {
            OutputStream os = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            File file = new File(fileName);

            if (file.exists() && file.isFile()) {
                byte[] fileBytes = Files.readAllBytes(file.toPath()); // Use file.toPath()
                dos.writeUTF("OK"); // Send a signal that the file exists
                dos.writeLong(fileBytes.length); // Send file size
                dos.write(fileBytes); // Send file data
                dos.flush();
                System.out.println("File sent successfully");
            } else {
                // Send error message to the client if the file is not found
                dos.writeUTF("ERROR: File not found");
                System.out.println("File not found: " + fileName);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error during file transfer", e);
        }
    }
}
