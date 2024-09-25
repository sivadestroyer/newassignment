package com.zeetaminds.nio;


import com.zeetaminds.ftp.Command;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class ListFilesNIO implements Command {
    private SocketChannel socketChannel;

    public ListFilesNIO(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void handle() {
        File dir = new File(".");
        File[] files = dir.listFiles();

        try {
            if (files != null) {
                int numberOfFiles = 0;

                // First count the number of files
                for (File file : files) {
                    if (file.isFile()) {
                        numberOfFiles++;
                    }
                }

                // Write the number of files to the socket channel
                socketChannel.write(ByteBuffer.wrap((numberOfFiles + "\n").getBytes(StandardCharsets.UTF_8)));

                // Then write the file names to the socket channel
                for (File file : files) {
                    if (file.isFile()) {
                        socketChannel.write(ByteBuffer.wrap((file.getName() + "\n").getBytes(StandardCharsets.UTF_8)));
                    }
                }

            } else {
                // If no files, write 0 to the socket channel
                socketChannel.write(ByteBuffer.wrap("0\n".getBytes(StandardCharsets.UTF_8)));
            }

            System.out.println("List sent successfully");

        } catch (IOException e) {
            System.err.println("Error writing file list to socket channel: " + e.getMessage());
        }
    }
}
