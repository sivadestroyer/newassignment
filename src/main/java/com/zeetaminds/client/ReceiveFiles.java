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

    void receiveFiles() {
        byte[] bytes = new byte[1024];
        try {
            // Construct the file path using the current directory

            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            FileOutputStream fos = new FileOutputStream(fileName);
            int read;
            int totalBytes = 0;
            long filesize = dis.readLong(); // Receive file size
            while (totalBytes < filesize && (read = dis.read(bytes)) > 0) {
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
