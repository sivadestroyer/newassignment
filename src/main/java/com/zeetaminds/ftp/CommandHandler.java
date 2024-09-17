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
                            receiveFiles(tokens[1], socket);
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

    public void receiveFiles(String fileName, Socket socket) {

        File file = new File(fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            long fileSize = dis.readLong(); // Read the file size first

            byte[] buffer = new byte[4096];
            int totalBytesRead = 0;
            int bytesRead;

            while (totalBytesRead < fileSize && (bytesRead = dis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
            }
            fos.flush();
            System.out.println("File received successfully");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error during file transfer", e);
        }
    }

}
