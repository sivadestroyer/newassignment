package com.zeetaminds.ftp;

import java.io.*;
import java.net.*;
import java.util.*;

class Server {
    ServerSocket server = null;
    Socket client = null;

    public static void main(String[] arg) {
        Server s = new Server();
        s.doConnections();
    }

    public void doConnections() {
        try {
            server = new ServerSocket(8888);
            System.out.println("Server started and waiting for clients...");

            while (true) {
                client = server.accept();
                System.out.println("Connection established with client");
                ClientThread ct = new ClientThread(client);
                ct.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ClientThread extends Thread {
    private Socket client = null;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    private FileInputStream fis = null;
    private FileOutputStream fos = null;
    private File file = null;

    public ClientThread(Socket c) {
        try {
            client = c;
            dis = new DataInputStream(c.getInputStream());
            dos = new DataOutputStream(c.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                String input = dis.readUTF();
                String filename = "", filedata = "";
                byte[] data;

                if (input.equals("FILE_SEND_FROM_CLIENT")) {
                    filename = dis.readUTF();
                    filedata = dis.readUTF();
                    fos = new FileOutputStream(filename);
                    fos.write(filedata.getBytes());
                    fos.close();
                    System.out.println("File received from client: " + filename);

                } else if (input.equals("DOWNLOAD_FILE")) {
                    filename = dis.readUTF();
                    file = new File(filename);
                    if (file.isFile()) {
                        fis = new FileInputStream(file);
                        data = new byte[fis.available()];
                        fis.read(data);
                        filedata = new String(data);
                        fis.close();
                        dos.writeUTF(filedata);
                        System.out.println("File sent to client: " + filename);
                    } else {
                        dos.writeUTF(""); // No file found
                    }

                } else if (input.equals("LIST_FILES")) {
                    listFilesOnServer();
                } else {
                    System.out.println("Unknown command from client");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Method to list files in the server directory
    public void listFilesOnServer() {
        try {
            File directory = new File(".");  // Current working directory
            File[] filesList = directory.listFiles();
            StringBuilder fileListBuilder = new StringBuilder();

            if (filesList != null && filesList.length > 0) {
                for (File file : filesList) {
                    if (file.isFile()) {
                        fileListBuilder.append(file.getName()).append("\n");
                    }
                }
            } else {
                fileListBuilder.append("No files available\n");
            }

            dos.writeUTF(fileListBuilder.toString());
            System.out.println("Sent file list to client");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
