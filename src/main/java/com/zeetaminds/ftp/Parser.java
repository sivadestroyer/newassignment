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

    public Command processCommand(String[] tokens, PrintWriter writer, Socket socket) {

        String operation = tokens[0].toUpperCase();

        if (Objects.equals(operation, "LIST")) {
            return new ListFiles(writer);
        } else if (Objects.equals(operation, "GET")) {
            if (tokens.length > 1) {
                return new SendFiles(tokens[1], socket);
            } else {
                writer.println("Syntax error in parameters or arguments.");
            }

        } else if (Objects.equals(operation, "PUT")) {
            if (tokens.length > 1) {
                System.out.println("entering put command");
                return new ReceiveFiles(tokens[1], socket);

            } else {
                writer.println("Syntax error in parameters or arguments.");
            }
        } else {
            LOG.info("unknown command" + operation);

        }
        return new HandleError();
    }

    public void run() {
        Parser process = new Parser(socket);

        try (InputStream reader = socket.getInputStream();
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            byte[] buffer = new byte[1024];
            ByteArrayOutputStream commandBuffer = new ByteArrayOutputStream();
            int bytesRead;

            while ((bytesRead = reader.read(buffer)) != -1) {
                commandBuffer.write(buffer, 0, bytesRead);


                int newlineIndex;
                while ((newlineIndex = indexOf(commandBuffer.toByteArray(), (byte) '\n')) >= 0) {
                    // Extract the command up to the newline
                    byte[] commandBytes = commandBuffer.toByteArray(); // Get the current bytes from commandBuffer
                    byte[] commandToProcess = new byte[newlineIndex]; // Create a new byte array to hold the command
                    System.arraycopy(commandBytes, 0, commandToProcess, 0, newlineIndex); // Copy valid bytes into commandToProcess

                    // Convert commandToProcess to String and process the command
                    String command = new String(commandToProcess, StandardCharsets.UTF_8).trim();
                    String[] tokens = command.split(" ");
                    System.out.println("Received command: " + command);

                    Command obj = process.processCommand(tokens, writer, socket);
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
