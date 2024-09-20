package com.zeetaminds.client;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FtpClient {
    static Logger logger = Logger.getLogger(FtpClient.class.getName());

    void receiveFiles(String fileName, Socket socket) {
        byte[] bytes = new byte[1024];
        ReceiveFiles obj = new ReceiveFiles(fileName, socket);
        obj.receiveFiles();
    }

    void sendFiles(String fileName, Socket socket) {
        SendFiles obj = new SendFiles(fileName, socket);
        obj.sendFiles();
    }

    public static void main(String[] a) {
        String filePath = "/home/sivabala/IdeaProjects/newassignment/src/main/java/com/zeetaminds/client/";
        try {
            FtpClient client = new FtpClient();
            Socket socket = new Socket("localhost", 9806);
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            logger.info("connected to ftp server");
            while (true) {
                String command = in.readLine();
                out.println(command);
                String[] tokens = command.split(" ");
                String operation = tokens[0].toUpperCase();
                switch (operation) {
                    case "LIST":
                        while (!((command = reader.readLine()).equals("266"))) {
                            logger.log(Level.SEVERE, command);
                        }
                        break;
                    case "GET":
                        if (tokens.length > 1) {
                            ReceiveFiles obj = new ReceiveFiles(filePath+tokens[1], socket);
                            obj.receiveFiles();
                        } else {
                            logger.info("filename is missing");
                        }
                        break;
                    case "PUT":
                        if (tokens.length > 1) {
                            client.sendFiles(filePath+tokens[1], socket);
                        } else {
                            logger.info("filename not given");
                        }
                        break;
                    default:
                        logger.info("unknown command");
                }
            }
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
    }
}
