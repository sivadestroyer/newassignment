package com.zeetaminds.nio;

import com.zeetaminds.ftp.Command;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiveFileNIO implements Command {
    private static final Logger logger = Logger.getLogger(ReceiveFileNIO.class.getName());
    private String fileName;
    private SocketChannel socketChannel;

    public ReceiveFileNIO(String fileName, SocketChannel socketChannel) {
        this.fileName = fileName;
        this.socketChannel = socketChannel;
    }

    @Override
    public void handle() {
        try (FileOutputStream fos = new FileOutputStream(fileName);
             FileChannel fileChannel = fos.getChannel()) {

            // Buffer to read file size first
            ByteBuffer sizeBuffer = ByteBuffer.allocate(64);
            socketChannel.read(sizeBuffer);
            sizeBuffer.flip();
            long fileSize = Long.parseLong(new String(sizeBuffer.array(), 0, sizeBuffer.limit()).trim());

            // Now read the actual file content
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
            long totalBytesReceived = 0;
            int bytesRead;

            while (totalBytesReceived < fileSize && (bytesRead = socketChannel.read(buffer)) > 0) {
                buffer.flip();  // Switch to read mode
                totalBytesReceived += bytesRead;
                fileChannel.write(buffer);  // Write buffer to file
                buffer.clear();  // Clear buffer for next read
            }

            if (totalBytesReceived >= fileSize) {
                logger.info("File received successfully: " + fileName);
            } else {
                logger.warning("File transfer incomplete. Expected: " + fileSize + ", received: " + totalBytesReceived);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error during file reception", e);
        }
    }
}
