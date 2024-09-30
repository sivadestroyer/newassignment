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
    private static final int BUFFER_SIZE = 1024;
    private static final int MARK_LIMIT = 1024;
    private String message = "Syntax error in parameters or arguments.\n";

    private Command processCommand(String[] tokens, DataHandler handler) throws IOException {
        String operation = tokens[0].toUpperCase();

        switch (operation) {
            case "LIST":
                return new ListFilesNIO(handler);
            case "GET":
                return tokens.length > 1 ? new SendFileNIO(tokens[1], handler)
                        : new HandleErrorNIO(handler, message);
            case "PUT":
                return tokens.length > 2 ? new ReceiveFileNIO(tokens[1], handler, Long.parseLong(tokens[2]))
                        : new HandleErrorNIO(handler, message);
            default:
                return new HandleErrorNIO(handler, "unknown command");
        }
    }

    public Command parsingMethod(DataHandler handler) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        StringBuilder commandLine = new StringBuilder();

        handler.mark(MARK_LIMIT);

        int bytesRead;
        while ((bytesRead = handler.read(buffer)) != -1) {
            int newlineIndex = -1;
            for (int i = 0; i < bytesRead; i++) {
                if (buffer[i] == '\n') {
                    newlineIndex = i;
                    break;
                }
            }

            if (newlineIndex != -1) {
                String chunk = new String(buffer, 0, newlineIndex, StandardCharsets.UTF_8);
                commandLine.append(chunk);

                String fullCommand = commandLine.toString().trim();
                String[] tokens = fullCommand.split(" ");

                handler.reset();
                handler.skip(newlineIndex + 1);

                return processCommand(tokens, handler);
            } else {
                String chunk = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
                commandLine.append(chunk);

                if (commandLine.length() > MARK_LIMIT) {
                    handler.reset();
                    throw new IOException("Command exceeds maximum length.");
                }
            }

            handler.mark(MARK_LIMIT);
        }

        return null;
    }
}

