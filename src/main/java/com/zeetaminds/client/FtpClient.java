//package com.zeetaminds.client;
//
//import java.io.*;
//import java.net.Socket;
//import java.util.logging.Logger;
//
//public class FtpClient {
//    public static void main(String[] a) {
//        Logger logger = Logger.getLogger(FtpClient.class.getName());
//        String filePath = "/home/sivabala/IdeaProjects/newassignment/src/main/java/com/zeetaminds/client/";
//        Socket socket =null;
//        try {
//            FtpClient client = new FtpClient();
//            socket = new Socket("localhost", 9806);
//            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            logger.info("connected to ftp server");
//
//            StringBuilder commandBuffer = new StringBuilder();
//            char[] buffer = new char[1024];
//            int bytesRead;
//
//            while (true) {
//                String commandLine = in.readLine();
//                if (commandLine == null) {
//                    logger.info("Server closed the connection.");
//                    break; // Exit the loop if the server closes the connection
//                }
//
//                commandBuffer.append(commandLine).append("\n");
//
//                // Process commands in the buffer
//                while ((bytesRead = commandBuffer.indexOf("\n")) != -1) {
//                    String command = commandBuffer.substring(0, bytesRead).trim();
//                    commandBuffer.delete(0, bytesRead + 1); // Remove processed command
//
//                    out.println(command);
//                    String[] tokens = command.split(" ",2);
//                    String operation = tokens[0].toUpperCase();
//
//                    try {
//                        switch (operation) {
//                            case "LIST":
//                                // Read the total number of files expected
//                                String line = reader.readLine();
//                                int expectedFiles = Integer.parseInt(line);
//                                int receivedFiles = 0;
//
//                                // Loop to receive file names
//                                while (receivedFiles < expectedFiles) {
//                                    String fileName = reader.readLine();
//                                    if (fileName != null && !fileName.isEmpty()) {
//                                        System.out.println(fileName);
//                                        receivedFiles++;
//                                    }
//                                }
//                                System.out.println("All files received successfully.");
//                                break;
//
//                            case "GET":
//                                if (tokens.length > 1) {
//                                    System.out.println("inside get method");
//                                    ReceiveFiles obj = new ReceiveFiles(filePath + tokens[1], socket);
//                                    obj.receiveFiles();
//                                } else {
//                                    logger.info("filename is missing");
//                                }
//                                break;
//
//                            case "PUT":
//                                if (tokens.length > 1) {
//                                    SendFiles obj = new SendFiles(filePath + tokens[1], socket);
//                                    obj.sendFiles();
//                                } else {
//                                    logger.info("filename not given");
//                                }
//                                break;
//
//                            default:
//                                logger.info("unknown command: " + "cmd:"+command);
//                        }
//                    } catch (Exception e) {
//                        logger.severe("Error processing command: " + command + " " + e.getMessage());
//                    }
//                }
//            }
//        } catch (IOException e) {
//            logger.severe("IOException: " + e.getMessage());
//        } finally {
//            // Ensure socket is closed properly
//            try {
//                socket.close();
//            } catch (IOException e) {
//                logger.severe("Error closing socket: " + e.getMessage());
//            }
//        }
//    }
//}
