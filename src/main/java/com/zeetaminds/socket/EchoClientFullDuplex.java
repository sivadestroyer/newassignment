package com.zeetaminds.socket;

import java.io.*;
import java.net.*;
import java.util.logging.Logger;

public class EchoClientFullDuplex {
    private static final Logger logger = Logger.getLogger(EchoClientFullDuplex.class.getName());

    public static void main(String[] args) {
        String host = "localhost";
        int port = 9806;

        try (Socket socket = new Socket(host, port)) {
            System.out.println("Connected to the server.");

            // Create a thread to send messages to the server
            Thread sendThread = new Thread(new SendHandler(socket));
            sendThread.start();

            // Create a thread to receive messages from the server
            Thread receiveThread = new Thread(new ReceiveHandler(socket));
            receiveThread.start();

            // Keep the main thread alive while the two threads work
            sendThread.join();
            receiveThread.join();
        } catch (IOException | InterruptedException e) {
            logger.severe("Error: " + e.getMessage());
        }
    }
}

class SendHandler implements Runnable {
    private final Socket socket;
    private static final Logger logger = Logger.getLogger(SendHandler.class.getName());

    public SendHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            String userInput;
            while ((userInput = in.readLine()) != null) {
                out.println(userInput);  // Send input to server
            }
        } catch (IOException e) {
            logger.severe("Error in SendHandler: " + e.getMessage());
        } finally {
            try {
                if (!socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                logger.severe("Error closing socket in SendHandler: " + e.getMessage());
            }
        }
    }
}

class ReceiveHandler implements Runnable {
    private final Socket socket;
    private static final Logger logger = Logger.getLogger(ReceiveHandler.class.getName());

    public ReceiveHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String serverMessage;

            while ((serverMessage = in.readLine()) != null) {
                System.out.println("Server: " + serverMessage);  // Print server response
            }
        } catch (IOException e) {
            logger.severe("Error in ReceiveHandler: " + e.getMessage());
        } finally {
            try {
                if (!socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                logger.severe("Error closing socket in ReceiveHandler: " + e.getMessage());
            }
        }
    }
}
