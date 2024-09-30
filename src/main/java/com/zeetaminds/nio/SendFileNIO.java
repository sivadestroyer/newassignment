package com.zeetaminds.nio;

import com.zeetaminds.ftp.Command;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;

public class SendFileNIO implements Command {

    private static final Logger LOG = Logger.getLogger(SendFileNIO.class.getName());

    private final String fileName;
    private final DataHandler dataHandler;

    public SendFileNIO(String fileName, DataHandler dataHandler) {
        this.fileName = fileName;
        this.dataHandler = dataHandler;
    }

    public void handle() throws IOException {
        File file = new File(fileName);

        if (file.exists() && file.isFile()) {
            long fileSize = file.length();
            dataHandler.write(Long.toString(fileSize) + "\n");
            dataHandler.flush();

            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    dataHandler.write(buffer, 0, bytesRead);
                }
                dataHandler.flush();
            }
        } else {
            dataHandler.write("ERROR: File not found\n");
            dataHandler.flush();
        }
    }
}
