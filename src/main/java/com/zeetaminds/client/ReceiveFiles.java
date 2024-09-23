package com.zeetaminds.client;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiveFiles {
    Logger logger = Logger.getLogger(ReceiveFiles.class.getName());
    private String fileName;
    private Socket socket;

    public ReceiveFiles(String fileName, Socket socket) {
        this.fileName = fileName;
        this.socket = socket;
    }

    public void receiveFiles() {
        byte[] bytes = new byte[1024];
        try {
            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);

            // First, check for the status message from the server
            String status = dis.readUTF(); // Read the server's response
            if ("ERROR: File not found".equals(status)) {
                System.out.println("Server error: " + status);
                return; // Exit the method if the file was not found
            }

            // If the file exists, proceed with file reception
            FileOutputStream fos = new FileOutputStream(fileName);
            int read;
            int totalBytes = 0;
            long fileSize = dis.readLong(); // Receive file size
            while (totalBytes < fileSize && (read = dis.read(bytes)) > 0) {
                fos.write(bytes, 0, read);
                totalBytes += read;
            }
            fos.flush();
            System.out.println("File received successfully");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error during file transfer", e);
        }
    }
}
