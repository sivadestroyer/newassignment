package com.zeetaminds.nio;

import com.zeetaminds.ftp.Command;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendFileNIO implements Command {
    private static final Logger logger = Logger.getLogger(SendFileNIO.class.getName());
    private String fileName;
    private SocketChannel socketChannel;

    public SendFileNIO(String fileName, SocketChannel socketChannel) {
        this.fileName = fileName;
        this.socketChannel = socketChannel;
    }

    @Override
    public void handle() {
        File file = new File(fileName);

        if (file.exists() && file.isFile()) {
            try (FileChannel fileChannel = FileChannel.open(Path.of(fileName), StandardOpenOption.READ)) {
                // Allocate buffer for reading file
                ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

                // Send file size first (could be useful for client to prepare for reception)
                long fileSize = file.length();
                socketChannel.write(ByteBuffer.wrap((fileSize + "\n").getBytes()));

                int bytesRead;
                while ((bytesRead = fileChannel.read(buffer)) > 0) {
                    buffer.flip();  // Switch buffer to read mode
                    socketChannel.write(buffer);  // Write file content to SocketChannel
                    buffer.clear();  // Clear the buffer for next read
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error during file transfer", e);
            }
        } else {
            try {
                logger.log(Level.SEVERE, "File does not exist: " + fileName);
                socketChannel.write(ByteBuffer.wrap("File not found.\n".getBytes()));
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error sending file not found message", e);
            }
        }
    }
}
