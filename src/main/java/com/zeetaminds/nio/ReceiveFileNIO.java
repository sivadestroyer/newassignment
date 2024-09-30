package com.zeetaminds.nio;

import com.zeetaminds.ftp.Command;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.FileOutputStream;
import java.io.IOException;

public class ReceiveFileNIO implements Command {

    private final String fileName;
    private final long fileLen;
    private final DataHandler dataHandler;

    public ReceiveFileNIO(String fileName, DataHandler dataHandler, long fileLen) {
        this.fileName = fileName;
        this.dataHandler = dataHandler;
        this.fileLen = fileLen;
    }

    public void handle() throws IOException {
        byte[] buffer = new byte[1024];
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            int totalBytes = 0;
            int bytesRead;
            while (totalBytes < fileLen && (bytesRead = dataHandler.read(buffer)) > 0) {
                fos.write(buffer, 0, bytesRead);
                totalBytes += bytesRead;
            }
            dataHandler.flush();
        }
    }
}
