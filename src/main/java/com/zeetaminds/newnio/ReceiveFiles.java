package com.zeetaminds.newnio;


import com.zeetaminds.ftp.Command;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

public class ReceiveFiles implements Command {

    private static final Logger LOG = Logger.getLogger(ReceiveFiles.class.getName());

    private final String fileName;
    private final SocketChannel clientChannel;
    private final long fileLen;

    public ReceiveFiles(String fileName, SocketChannel clientChannel, long fileLen) {
        this.fileName = fileName;
        this.clientChannel = clientChannel;
        this.fileLen = fileLen;
    }

    public void handle() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024); // Buffer for file transfer
        long totalBytes = 0;

        try (FileOutputStream fos = new FileOutputStream(fileName);
             FileChannel fileChannel = fos.getChannel()) {

            // Read from the clientChannel and write to the file
            while (totalBytes < fileLen) {
                int bytesRead = clientChannel.read(buffer);
                if (bytesRead == -1) {
                    break;  // End of stream
                }

                // Flip buffer before writing to file
                buffer.flip();
                fileChannel.write(buffer);
                buffer.clear();

                totalBytes += bytesRead;
            }
        }
    }
}
