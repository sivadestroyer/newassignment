package com.zeetaminds.client;

import com.zeetaminds.ftp.Command;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class ReceiveFiles implements Command {

    Logger logger = Logger.getLogger(ReceiveFiles.class.getName());

    private final String fileName;
    private final OutputStream out;
    private final InputStream in;
    private final String receivedString;

    public ReceiveFiles(String fileName, InputStream in, OutputStream out, String receivedString) {
        this.fileName = fileName;
        this.in = in;
        this.out = out;
        this.receivedString = receivedString;
    }

    public void handle() throws IOException {
        byte[] bytes = new byte[1024]; // Increase buffer size for file transfer
        FileOutputStream fos = new FileOutputStream(fileName);
        long received = Long.parseLong(receivedString);
        System.out.println(received);
        int totalBytes = 0;
        int read;
        while (totalBytes < received && (read = in.read(bytes, 0, (int) Math.min(bytes.length, (received - totalBytes)))) > 0) {
            fos.write(bytes, 0, read);
            totalBytes += read;
        }
        out.write(("you are entering more than " + received + " characters").getBytes());
        fos.flush();
        out.flush();
    }


}
