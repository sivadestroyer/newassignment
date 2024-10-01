package com.zeetaminds.newnio;

import com.zeetaminds.ftp.Command;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class Parser {

    private static final Logger LOG = Logger.getLogger(Parser.class.getName());
    private static final int MARK_LIMIT = 1024;
    String message = "Syntax error in parameters or arguments.\n";

    private Command processCommand(String[] tokens, SocketChannel clientChannel) throws IOException {
        String operation = tokens[0].toUpperCase();

        switch (operation) {
            case "LIST":
                return new ListFiles(clientChannel);
            case "GET":
                return tokens.length > 1 ? new SendFiles(tokens[1], clientChannel) : new HandleError(clientChannel, message);
            case "PUT":
                return tokens.length > 2 ? new ReceiveFiles(tokens[1], clientChannel, Long.parseLong(tokens[2]))
                        : new HandleError(clientChannel, message);
            default:
                return new HandleError(clientChannel, "unknown command");
        }
    }

    public Command parse(ByteBuffer buffer, SocketChannel clientChannel) throws IOException {
        byte[] byteArray = new byte[buffer.remaining()];
        buffer.get(byteArray);

        String commandLine = new String(byteArray, StandardCharsets.UTF_8).trim();
        if (commandLine.isEmpty()) {
            return null;  // No command
        }

        String[] tokens = commandLine.split(" ");
        return processCommand(tokens, clientChannel);
    }
}
