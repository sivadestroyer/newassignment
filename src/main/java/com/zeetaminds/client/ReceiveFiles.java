package com.zeetaminds.client;

import com.zeetaminds.ftp.Command;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiveFiles implements Command {
    Logger logger = Logger.getLogger(ReceiveFiles.class.getName());
    private String fileName;
    private Socket socket;

    public ReceiveFiles(String fileName, Socket socket) {
        this.fileName = fileName;
        this.socket = socket;
    }
    public void handle() {
        byte[] bytes = new byte[2]; // Increase buffer size for file transfer
        try {
            InputStream is = socket.getInputStream();
            FileOutputStream fos = new FileOutputStream(fileName);
            OutputStream out = socket.getOutputStream();
            StringBuilder receivedString = new StringBuilder();
            byte[] buffer = new byte[1024]; // Buffer to hold incoming bytes
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                String part = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
                receivedString.append(part); // Append to the StringBuilder
                if (part.contains("\n")) {
                    break; // Exit loop if we reach a newline or any end condition
                }
            }

            long received = Long.parseLong(receivedString.toString().trim());
            System.out.println(received);
            int totalBytes = 0;
            int read;
            while (totalBytes < received && (read = is.read(bytes)) > 0) {
                fos.write(bytes, 0, read);
                totalBytes += read;
            }
            out.write(("you are entering mort than"+received+"characters").getBytes());
            fos.flush();
            System.out.println("File received successfully");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error during file transfer", e);
        }
    }


}
