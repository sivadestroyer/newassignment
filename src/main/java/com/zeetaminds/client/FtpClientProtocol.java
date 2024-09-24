//package com.zeetaminds.client;
//
//import java.io.*;
//import java.net.*;
//import java.nio.file.Files;
//import java.util.concurrent.TimeUnit;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//public class FtpClientProtocol {
//    private static final long DELAY_MS =1 ;
//    static Logger logger = Logger.getLogger(FtpClient.class.getName());
//
//    void sendFiles(String fileName, Socket socket) {
//        String filePath = "/home/sivabala/IdeaProjects/newassignment/src/main/java/com/zeetaminds/client/" + fileName;
//        File file = new File(filePath);
//
//        if (file.exists() && file.isFile()) {
//            try {
//                byte[] fileBytes = Files.readAllBytes(file.toPath()); // Use file.toPath()
//                OutputStream os = socket.getOutputStream();
//                DataOutputStream dos = new DataOutputStream(os);
//                dos.writeLong(fileBytes.length); // Send file size first
//                dos.write(fileBytes); // Send file data
//                dos.flush();
//                System.out.println("File sent successfully");
//            } catch (IOException e) {
//                logger.log(Level.SEVERE, "Error during file transfer", e);
//            }
//        } else {
//            System.out.println("File not found: " + filePath);
//        }
//    }
//
//    public static void main(String[] args) {
//        String filePath = "/home/sivabala/IdeaProjects/newassignment/src/main/java/com/zeetaminds/client/";
//        try {
//            FtpClientProtocol client = new FtpClientProtocol();
//            Socket socket = new Socket("localhost", 9806);
//            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
//            PrintWriter serverOutput = new PrintWriter(socket.getOutputStream(), true);
//            BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            logger.info("Connected to FTP server");
//
//            while (true) {
//                String command = consoleInput.readLine();
//                sendCommandWithDelay(command,serverOutput);
//                String[] tokens = command.split(" ");
//                String operation = tokens[0].toUpperCase();
//
//                switch (operation) {
//                    case "LIST":
//                        handleListCommand(serverInput);
//                        break;
//                    case "GET":
//                        if (tokens.length > 1) {
//                            ReceiveFiles obj = new ReceiveFiles(filePath+tokens[1],socket);
//                            obj.receiveFiles();
//                        } else {
//                            logger.info("Filename is missing for GET command");
//                        }
//                        break;
//                    case "PUT":
//                        if (tokens.length > 1) {
//                            client.sendFiles(tokens[1], socket);
//                        } else {
//                            logger.info("Filename not given for PUT command");
//                        }
//                        break;
//                    default:
//                        logger.info("Unknown command");
//                        break;
//                }
//            }
//        } catch (IOException e) {
//            logger.log(Level.SEVERE, "Connection error", e);
//        }
//    }
//
//    private static void sendCommandWithDelay(String command, PrintWriter out) {
//        char[] commandChars = command.toCharArray();
//        for (char ch : commandChars) {
//            out.print(ch); // Send one character at a time
//            out.flush();
//            System.out.println("Sent: " + ch);  // For debugging purposes, to show each character sent
//            try {
//                // Introduce a delay between each character
//                TimeUnit.MILLISECONDS.sleep(DELAY_MS);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        // Send newline after the full command
//        out.print("\r\n");
//        out.flush();
//    }
//    private static void handleListCommand(BufferedReader serverInput) throws IOException {
//        String responseLine;
//
//        // Expecting the server's 150 response for starting directory listing
//        responseLine = serverInput.readLine();
//        System.out.println(responseLine); // Should be: "150 Here comes the directory listing."
//
//        // Read the directory listing until we get the "226 Directory listing completed."
//        while ((responseLine = serverInput.readLine()) != null) {
//            System.out.println(responseLine); // Print each file/directory name
//
//            // Check for the end of the list
//            if (responseLine.startsWith("226")) {
//                System.out.println("End of directory listing received."); // Log end of list
//                break; // Exit the loop when the end-of-list message is received
//            }
//        }
//    }
//
//}
