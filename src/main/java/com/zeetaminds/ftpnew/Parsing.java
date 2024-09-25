package com.zeetaminds.ftpnew;

import com.zeetaminds.client.ReceiveFiles;
import com.zeetaminds.client.SendFiles;
import com.zeetaminds.ftp.Command;
import com.zeetaminds.ftp.HandleError;
import com.zeetaminds.ftp.ListFiles;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.logging.Logger;

public class Parsing {
    private static final Logger LOG = Logger.getLogger(Parsing.class.getName());

    public Command processCommand(String[] tokens,InputStream in, OutputStream out) throws IOException {
        String operation = tokens[0].toUpperCase();

        if (Objects.equals(operation, "LIST")) {
            return new ListFiles(out);
        } else if (Objects.equals(operation, "GET")) {
            if (tokens.length > 1) {
                return new SendFiles(tokens[1],out);
            } else {
                out.write("Syntax error in parameters or arguments.\n".getBytes(StandardCharsets.UTF_8));
            }
        } else if (Objects.equals(operation, "PUT")) {
            if (tokens.length > 1) {
                return new ReceiveFiles(tokens[1],in, out);
            } else {
                out.write("Syntax error in parameters or arguments.\n".getBytes(StandardCharsets.UTF_8));
            }
        } else {
            LOG.info("unknown command: " + operation);
        }
        return new HandleError();
    }

    public void ParsingMethod(InputStream in, OutputStream out) throws IOException {
        ByteArrayOutputStream commandBuffer = new ByteArrayOutputStream();
        int bytesRead;
        byte[] buffer = new byte[1024];

        while ((bytesRead = in.read(buffer)) != -1) {
            commandBuffer.write(buffer, 0, bytesRead);
            int newlineIndex;
            // Process all complete commands in the buffer
            while ((newlineIndex = indexOf(commandBuffer.toByteArray(), (byte) '\n')) >= 0) {
                byte[] commandBytes = commandBuffer.toByteArray();
                byte[] commandToProcess = new byte[newlineIndex];
                System.arraycopy(commandBytes, 0, commandToProcess, 0, newlineIndex);

                // Convert commandToProcess to String and process the command
                String command = new String(commandToProcess, StandardCharsets.UTF_8).trim();
                String[] tokens = command.split(" ");
                System.out.println("Received command: " + command);

                // Process the command and handle it
                Command resultCommand = processCommand(tokens,in, out);

                    resultCommand.handle();


                // Remove the processed command from the buffer
                byte[] remainingBytes = commandBuffer.toByteArray();
                byte[] newBuffer = new byte[remainingBytes.length - newlineIndex - 1]; // -1 to skip the newline character
                System.arraycopy(remainingBytes, newlineIndex + 1, newBuffer, 0, newBuffer.length);
                commandBuffer.reset(); // Clear the buffer
                commandBuffer.write(newBuffer); // Write the remaining bytes back to buffer
            }
        }
    }

    private int indexOf(byte[] array, byte value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }
}
