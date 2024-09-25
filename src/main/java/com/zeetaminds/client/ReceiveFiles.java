package com.zeetaminds.client;

import com.zeetaminds.ftp.Command;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiveFiles implements Command {
    Logger logger = Logger.getLogger(ReceiveFiles.class.getName());
    private String fileName;
    private OutputStream out;
    private InputStream in;
    public ReceiveFiles(String fileName, InputStream in,OutputStream out) {
        this.fileName = fileName;
        this.in = in;
        this.out = out;
    }
    public void handle() {
        byte[] bytes = new byte[2]; // Increase buffer size for file transfer
        try {

            FileOutputStream fos = new FileOutputStream(fileName);

            StringBuilder receivedString = new StringBuilder();
            byte[] buffer = new byte[1024]; // Buffer to hold incoming bytes
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
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
            while (totalBytes < received && (read = in.read(bytes)) > 0) {
                fos.write(bytes, 0, read);
                totalBytes += read;
            }
            out.write(("you are entering more than "+received+" characters").getBytes());
            fos.flush();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error during file transfer", e);
        }
    }


}
