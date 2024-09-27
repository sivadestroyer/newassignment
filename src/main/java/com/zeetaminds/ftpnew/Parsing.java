package com.zeetaminds.ftpnew;

import com.zeetaminds.client.ReceiveFiles;
import com.zeetaminds.client.SendFiles;
import com.zeetaminds.ftp.Command;
import com.zeetaminds.ftp.HandleError;
import com.zeetaminds.ftp.ListFiles;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.logging.Logger;

public class Parsing {

    private static final Logger LOG = Logger.getLogger(Parsing.class.getName());

    private static final int BUFFER_SIZE = 1024;
    private static final int MARK_LIMIT = 1024;
    String message = "Syntax error in parameters or arguments.\n";

    private Command processCommand(String[] tokens, InputStream in, OutputStream out) throws IOException {
        String operation = tokens[0].toUpperCase();

        switch (operation) {
            case "LIST":
                return new ListFiles(out);
            case "GET":
                return tokens.length > 1 ? new SendFiles(tokens[1], out) : new HandleError(out, message);
            case "PUT":
                return tokens.length > 2 ? new ReceiveFiles(tokens[1], in, out, Long.parseLong(tokens[2]))
                        : new HandleError(out, message);
            default:
                return new HandleError(out, "unknown command");
        }
        // Default error handler
    }

    public Command parsingMethod(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        StringBuilder commandLine = new StringBuilder();

        // Mark the stream to reset in case of errors or long commands
        in.mark(MARK_LIMIT);

        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            // Search for the newline character in the byte buffer
            int newlineIndex = -1;
            for (int i = 0; i < bytesRead; i++) {
                if (buffer[i] == '\n') {
                    newlineIndex = i;
                    break;
                }
            }

            if (newlineIndex != -1) {
                // We found a newline in the current buffer
                String chunk = new String(buffer, 0, newlineIndex, StandardCharsets.UTF_8);
                commandLine.append(chunk);

                // Process the full command
                String fullCommand = commandLine.toString().trim();
                String[] tokens = fullCommand.split(" ");

                // Reset input stream to handle remaining bytes after newline
                in.reset();
                //noinspection ResultOfMethodCallIgnored
                in.skip(newlineIndex + 1);  // Skip the newline character

                return processCommand(tokens, in, out);
            } else {
                // No newline found, convert the whole buffer to a string and append to the command
                String chunk = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
                commandLine.append(chunk);

                // Re-mark the stream in case more chunks need to be processed
                if (commandLine.length() > MARK_LIMIT) {
                    in.reset();  // Reset the stream to the marked position
                    throw new IOException("Command exceeds maximum length.");
                }
            }

            // Mark the stream position after reading the current buffer
            in.mark(MARK_LIMIT);
        }


        // Return null if the connection is closed and no valid command is received
        return null;
    }

}
