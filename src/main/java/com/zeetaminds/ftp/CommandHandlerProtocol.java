package com.zeetaminds.ftp;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandHandlerProtocol implements Runnable {
    Logger logger = Logger.getLogger(CommandHandlerProtocol.class.getName());
    Socket socket;

    public CommandHandlerProtocol(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

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
                            writer.println("501 Syntax error in parameters or arguments.");
                            logger.info("Filename is missing for GET command");
                        }
                        break;
                    case "PUT":
                        if (tokens.length > 1) {
                            receiveFiles(tokens[1], socket);
                        } else {
                            writer.println("501 Syntax error in parameters or arguments.");
                            logger.info("Filename is missing for PUT command");
                        }
                        break;
                    default:
                        writer.println("502 Command not implemented.");
                        logger.info("Unknown command: " + operation);
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

    /**
     * Handle LIST command: Sends a list of files in the current directory.
     */
    public void listFiles(PrintWriter writer) {
        File dir = new File(".");
        File[] files = dir.listFiles();

        if (files != null) {
            // Signal that the list is being prepared (equivalent to FTP's "150" response)
            writer.println("150 Here comes the directory listing.");

            for (File file : files) {
                if (file.isFile()) {
                    writer.println(file.getName());
                }
            }
            // Signal the end of the list (equivalent to FTP's "226" response)
            writer.println("226 Directory listing completed.");
        } else {
            writer.println("550 Failed to open directory.");
        }
    }

    /**
     * Handle GET command: Sends a file to the client.
     */
    public void sendFiles(String fileName, PrintWriter writer, Socket socket) {
        File file = new File(fileName);
        if (file.isFile()) {
            try {
                byte[] bytes = new byte[1024];
                InputStream fis = new FileInputStream(file);
                OutputStream os = socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);
                long fileSize = file.length();
                dos.writeLong(fileSize); // Send file size first
                int read;
                while ((read = fis.read(bytes)) > 0) {
                    dos.write(bytes, 0, read);
                }
                dos.flush();
                fis.close();
                System.out.println("File sent successfully");
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error during IO", e);
                writer.println("550 Error during file transfer.");
            }
        } else {
            writer.println("550 File not found.");
        }
    }


    /**
     * Handle PUT command: Receives a file from the client.
     */
    void receiveFiles(String fileName, Socket socket) {
        byte[] bytes = new byte[1024];
        try {
            // Construct the file path using the current directory
            System.out.println("inside receive of server");
            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            FileOutputStream fos = new FileOutputStream(fileName);
            long filesize = dis.readLong();
            System.out.println(filesize);// Receive file size
            int read;
            int totalBytes = 0;
            while (totalBytes < filesize && (read = dis.read(bytes)) > 0) {
                System.out.println("writing to file");
                fos.write(bytes, 0, read);
                System.out.println(read);
                totalBytes += read;
            }
            fos.flush();
            System.out.println("File received successfully");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error during file transfer", e);
        }
    }

}
