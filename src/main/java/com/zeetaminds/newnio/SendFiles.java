package com.zeetaminds.newnio;

import com.zeetaminds.ftp.Command;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class SendFiles implements Command {

    private static final Logger LOG = Logger.getLogger(SendFiles.class.getName());

    private final String fileName;
    private final SocketChannel clientChannel;

    public SendFiles(String fileName, SocketChannel clientChannel) {
        this.fileName = fileName;
        this.clientChannel = clientChannel;
    }

    // Protected method to retrieve the file, making it testable
    protected File getFile(String fileName) {
        return new File(fileName);
    }

    // Protected method to retrieve FileInputStream, making it testable
    protected FileInputStream getFileInputStream(File file) throws IOException {
        return new FileInputStream(file);
    }

    public void handle() throws IOException {
        File file = getFile(fileName); // Refactored to use getFile()

        if (file.exists() && file.isFile()) {
            // Write the file size to the buffer first
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put((file.length() + "\n").getBytes(StandardCharsets.UTF_8));
            buffer.flip();
            clientChannel.write(buffer);
            buffer.clear();

            // Use a FileChannel to send the file in chunks
            try (FileChannel fileChannel = getFileInputStream(file).getChannel()) { // Refactored to use getFileInputStream()
                long position = 0;
                long size = file.length();
                while (position < size) {
                    long transferred = fileChannel.transferTo(position, size - position, clientChannel);
                    if (transferred <= 0) break;
                    position += transferred;
                }
            }
        } else {
            // Send error response
            ByteBuffer errorBuffer = ByteBuffer.wrap("ERROR: File not found\n".getBytes(StandardCharsets.UTF_8));
            clientChannel.write(errorBuffer);
        }
    }
}
