package com.zeetaminds.nio;

import com.zeetaminds.client.ReceiveFiles;
import com.zeetaminds.client.SendFiles;
import com.zeetaminds.ftp.Command;
import com.zeetaminds.ftp.HandleError;
import com.zeetaminds.ftp.ListFiles;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.logging.Logger;

public class ParsingNIO {
    private static final Logger LOG = Logger.getLogger(ParsingNIO.class.getName());

    public Command processCommand(String[] tokens, SocketChannel socketChannel) throws IOException {
        String operation = tokens[0].toUpperCase();

        if (Objects.equals(operation, "LIST")) {
            return new ListFilesNIO(socketChannel);
        } else if (Objects.equals(operation, "GET")) {
            if (tokens.length > 1) {
                return new SendFileNIO(tokens[1], socketChannel);
            } else {
                writeResponse(socketChannel, "Syntax error in parameters or arguments.\n");
            }
        } else if (Objects.equals(operation, "PUT")) {
            if (tokens.length > 1) {
                System.out.println("inside");
                return new ReceiveFileNIO(tokens[1], socketChannel);
            } else {
                writeResponse(socketChannel, "Syntax error in parameters or arguments.\n");
            }
        } else {
            LOG.info("Unknown command: " + operation);
        }
        return null; // Default error handler
    }

    public Command ParsingMethod(ByteBuffer buffer, SocketChannel socketChannel) throws IOException {
        StringBuilder commandLine = new StringBuilder();

        // Read the buffer and build a command string
        while (buffer.hasRemaining()) {
            commandLine.append((char) buffer.get());
        }

        String command = commandLine.toString().trim();
        String[] tokens = command.split(" ");

        return processCommand(tokens, socketChannel);
    }

    private void writeResponse(SocketChannel socketChannel, String response) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(response.getBytes(StandardCharsets.UTF_8));
        socketChannel.write(buffer);
    }
}
