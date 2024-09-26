package com.zeetaminds.ftp;

import com.zeetaminds.client.ReceiveFiles;
import com.zeetaminds.client.SendFiles;

import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Parser implements Runnable {
    private static final Logger LOG = Logger.getLogger(Parser.class.getName());
    private final Socket socket;

    public Parser(Socket socket) {
        this.socket = socket;
    }

    public Command processCommand(String[] tokens, OutputStream out, InputStream in) throws IOException {

        String operation = tokens[0].toUpperCase();

        switch (operation) {
            case "LIST":
                return new ListFiles(out);
            case "GET":
                if (tokens.length > 1) {
                    return new SendFiles(tokens[1], out);
                } else {
                    out.write(("Syntax error in parameters or arguments.\n").getBytes(StandardCharsets.UTF_8));
                }

                break;
            case "PUT":
                if (tokens.length > 2) {
                    return new ReceiveFiles(tokens[1], in, out,tokens[2]);

                } else {
                    out.write(("Syntax error in parameters or arguments.\n").getBytes(StandardCharsets.UTF_8));
                }
                break;
            default:
                LOG.info("unknown command" + operation);

                break;
        }
        return null;
    }

    public void run() {
        Parser process = new Parser(socket);

        try (InputStream reader = socket.getInputStream();
             OutputStream out = socket.getOutputStream()) {

            byte[] buffer = new byte[1024];
            ByteArrayOutputStream commandBuffer = new ByteArrayOutputStream();
            int bytesRead;

            while ((bytesRead = reader.read(buffer)) != -1) {
                commandBuffer.write(buffer, 0, bytesRead);


                int newlineIndex;
                while ((newlineIndex = indexOf(commandBuffer.toByteArray(), (byte) '\n')) >= 0) {
                    byte[] commandBytes = commandBuffer.toByteArray();
                    byte[] commandToProcess = new byte[newlineIndex];
                    System.arraycopy(commandBytes, 0, commandToProcess, 0, newlineIndex);

                    // Convert commandToProcess to String and process the command
                    String command = new String(commandToProcess, StandardCharsets.UTF_8).trim();
                    String[] tokens = command.split(" ");
                    System.out.println("Received command: " + command);

                    Command obj = process.processCommand(tokens, out, reader);
                    obj.handle();
                    // Remove the processed command from the buffer
                    byte[] remainingBytes = commandBuffer.toByteArray();
                    byte[] newBuffer = new byte[remainingBytes.length - newlineIndex - 1]; // -1 to skip the newline character
                    System.arraycopy(remainingBytes, newlineIndex + 1, newBuffer, 0, newBuffer.length);
                    commandBuffer.reset(); // Clear the buffer
                    commandBuffer.write(newBuffer);
                }
            }

        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error reading from socket", e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                LOG.log(Level.SEVERE, "Error closing socket", e);
            }
        }
    }

    // Helper method to find the index of a byte in a byte array
    private int indexOf(byte[] array, byte value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i; // Return index of the first occurrence
            }
        }
        return -1; // Not found
    }

}
