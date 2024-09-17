package com.zeetaminds.ftp;

import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandHandler extends Thread {
    Logger logger = Logger.getLogger(CommandHandler.class.getName());
    Socket socket;

    public CommandHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(" ");
                String operation = tokens[0].toUpperCase();
                switch (operation) {
                    case "LIST":
                        listFiles(writer);
                        break;
                    case "GET":
                        if (tokens.length > 1) {
                            sendFiles(tokens[1], writer, socket);
                        } else {
                            logger.info("filename is missing");
                        }
                        break;
                    case "PUT":
                        if (tokens.length > 1) {
                            recieveFiles(tokens[1], reader, socket);
                        } else {
                            logger.info("filename not given");
                        }
                        break;
                    default:
                        logger.info("unknown command");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error closing socket", e);
            }
        }
    }

    public void listFiles(PrintWriter writer) {
        File dir = new File(".");
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    writer.println(file.getName());
                }
            }
        } else {
            writer.println("no such file found");

        }
        writer.println("END_OF_LIST");
    }

    public void sendFiles(String fileName, PrintWriter writer, Socket socket) {

        File file = new File(fileName);
        byte[] data;
        String filedata;
        if (file.isFile()) {
            try {
                byte[] bytes = new byte[1024];
                InputStream fis = new FileInputStream(file);
                OutputStream os = socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);
                long fileSize = file.length();
                dos.writeLong(fileSize);
                int read;
                while((read = fis.read(bytes))>0) {
                    dos.write(bytes, 0, read);
                }
                os.flush();
                fis.close();
                System.out.println("file sent successfully");
            } catch (IOException e) {
                logger.log(Level.SEVERE, "error during io", e);
            }

        } else {
            writer.println("file not found");
        }
    }

    public void recieveFiles(String fileName, BufferedReader reader, Socket socket) {
        File file = new File(fileName);
        try {
            int fileSize = 4096;
            byte[] buffer = new byte[fileSize];
            InputStream is = socket.getInputStream();
            try (FileOutputStream fos = new FileOutputStream(file)) {
                int byteRead;
                while ((byteRead = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, byteRead);
                }
                fos.flush();
                System.out.println("file received successfully");
            }


        } catch (IOException e) {
            logger.log(Level.SEVERE, "error in file transfer", e);
        }
    }
}
