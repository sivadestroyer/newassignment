package com.zeetaminds.ftp;

import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandHandler implements Runnable {
    Logger LOG = Logger.getLogger(CommandHandler.class.getName());
    Socket socket;

    public CommandHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (InputStream reader = socket.getInputStream();
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            StringBuilder commandBuffer = new StringBuilder();
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = reader.read(buffer)) != -1) {

                String receivedData = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);

                // Append received string to command buffer
                commandBuffer.append(receivedData);


                int newlineIndex;
                while ((newlineIndex = commandBuffer.indexOf("\n")) != -1) {
                    String command = commandBuffer.substring(0, newlineIndex).trim();
                    ProcessCommand obj = new ProcessCommand();
                    obj.processCommand(command, writer, socket);
                    commandBuffer.delete(0, newlineIndex + 1);
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
}