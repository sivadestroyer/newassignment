package com.zeetaminds.client;

import com.zeetaminds.ftp.Command;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.logging.Logger;
import java.util.logging.Level;

public class SendFiles implements Command {

    private static final Logger LOG = Logger.getLogger(SendFiles.class.getName());

    private final String fileName;
    private final OutputStream out;

    public SendFiles(String fileName,OutputStream out) {
        this.fileName = fileName;
        this.out = out;
    }

    public void handle() throws IOException{
            File file = new File(fileName);

            if (file.exists() && file.isFile()) {
                // Write the file size to the output stream first
                long fileSize = file.length();
                out.write((fileSize +"\n").getBytes(StandardCharsets.UTF_8));
                // Open a FileInputStream to read the file in chunks
                try (FileInputStream fis = new FileInputStream(file)) {
                    byte[] buffer = new byte[4096]; // 4 KB buffer
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead); // Send file data
                    }
                }
            } else {

                // Optionally, send an error response to the client
                out.write("ERROR: File not found\n".getBytes(StandardCharsets.UTF_8));
            }

    }

}
