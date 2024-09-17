package com.zeetaminds.socket;
import java.net.*;
import java.io.*;

public class EchoServerSingle {
    public static void main(String[] args) throws IOException {
        int port = 9806;
        ServerSocket serverSocket = new ServerSocket(port);

        while (true) {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();  // Accept the connection
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                String str;

                // Read input from client and echo it back
                while ((str = in.readLine()) != null) {
                    System.out.println("Received: " + str);

                    // Accept server input to send back to the client
                    BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
                    System.out.println("Enter the message: ");
                    String response = consoleInput.readLine();

                    out.println("Server says: " + response);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Ensure that the client socket is closed even if an exception occurs
                if (clientSocket != null && !clientSocket.isClosed()) {
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
