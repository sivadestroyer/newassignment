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

    public Command processCommand(String[] tokens, InputStream in, OutputStream out) throws IOException {
        String operation = tokens[0].toUpperCase();

        if (Objects.equals(operation, "LIST")) {
            return new ListFiles(out);
        } else if (Objects.equals(operation, "GET")) {
            if (tokens.length > 1) {
                return new SendFiles(tokens[1], out);
            } else {
                out.write("Syntax error in parameters or arguments.\n".getBytes(StandardCharsets.UTF_8));
            }
        } else if (Objects.equals(operation, "PUT")) {
            if (tokens.length > 1) {
                return new ReceiveFiles(tokens[1], in, out);
            } else {
                out.write("Syntax error in parameters or arguments.\n".getBytes(StandardCharsets.UTF_8));
            }
        } else {
            LOG.info("Unknown command: " + operation);
        }
        return new HandleError(); // Default error handler
    }

    public Command ParsingMethod(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        StringBuilder commandLine = new StringBuilder();

        // Mark the stream to reset in case of errors or long commands
        in.mark(MARK_LIMIT);

        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            String chunk = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
            commandLine.append(chunk);
            int newlineIndex = chunk.indexOf('\n');
            if (newlineIndex != -1) {
                // Full command received
                String fullCommand = commandLine.substring(0, commandLine.length() - (chunk.length() - newlineIndex)).trim();

                // Reset the stream to avoid reading excess data
                in.reset();
                in.skip(newlineIndex + 1);  // Skip the newline character
                String[] tokens = fullCommand.split(" ");
                return processCommand(tokens, in, out);
            }

            // If command grows too long without a newline, reset the stream
            if (commandLine.length() > MARK_LIMIT) {
                LOG.warning("Command too long. Resetting stream.");
                in.reset();  // Reset the stream to the marked position
                throw new IOException("Command exceeds maximum length.");
            }

            // Re-mark the stream in case more chunks need to be processed
            in.mark(MARK_LIMIT);
        }

        // Return null if the connection is closed and no valid command is received
        return null;
    }
}
