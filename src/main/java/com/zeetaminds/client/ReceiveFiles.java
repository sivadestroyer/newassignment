package com.zeetaminds.client;

import com.zeetaminds.ftp.Command;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class ReceiveFiles implements Command {

    private final String fileName;
    private final OutputStream out;
    private final InputStream in;
    private final Long fileLen;

    public ReceiveFiles(String fileName, InputStream in,
                        OutputStream out, Long fileLen) {
        this.fileName = fileName;
        this.in = in;
        this.out = out;
        this.fileLen = fileLen;
    }

    public void handle() throws IOException {
        byte[] bytes = new byte[1024]; // Increase buffer size for file transfer
        FileOutputStream fos = new FileOutputStream(fileName);

        int totalBytes = 0;
        int read;

        while (totalBytes < fileLen &&
                (read = in.read(bytes, 0, (int) Math.min(bytes.length, (fileLen - totalBytes)))) > 0) {
            fos.write(bytes, 0, read);
            totalBytes += read;
        }
    }


}
