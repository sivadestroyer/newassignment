package com.zeetaminds.ftp;


import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NioFtpServer {
    private static final int PORT = 9806;
    private static final Logger logger = Logger.getLogger(NioFtpServer.class.getName());
    public static void main(String[] args) {
        try (Selector selector = Selector.open();
             ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {

            serverSocketChannel.bind(new InetSocketAddress(PORT));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            logger.info("NIO FTP Server started on port " + PORT);
            while (true) {
                selector.select();

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();

                    if (key.isAcceptable()) {
                        handleAccept(key, selector);
                    } else if (key.isReadable()) {
                        handleRead(key);
                    }
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Server error", e);
        }
    }

    private static void handleAccept(SelectionKey key, Selector selector) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        logger.info("Client connected: " + socketChannel.getRemoteAddress());
    }

    private static void handleRead(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead = socketChannel.read(buffer);

        if (bytesRead == -1) {
            socketChannel.close();
            logger.info("Client disconnected");
            return;
        }

        buffer.flip();
        String command = new String(buffer.array(), 0, bytesRead).trim();
        String[] tokens = command.split(" ");
        String operation = tokens[0].toUpperCase();

        logger.info("Received command: " + command);

        switch (operation) {
            case "LIST":
                sendFileList(socketChannel);
                break;
            case "GET":
                if (tokens.length > 1) {
                    sendFile(socketChannel, tokens[1]);
                } else {
                    sendMessage(socketChannel, "Filename missing");
                }
                break;
            case "PUT":
                if (tokens.length > 1) {
                    receiveFile(socketChannel, tokens[1]);
                } else {
                    sendMessage(socketChannel, "Filename missing");
                }
                break;
            default:
                sendMessage(socketChannel, "Unknown command");
        }
    }

    private static void sendFileList(SocketChannel socketChannel) throws IOException {
        File dir = new File(".");
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    sendMessage(socketChannel, file.getName());
                }
            }
        }
        sendMessage(socketChannel, "END_OF_LIST");
    }

    private static void sendFile(SocketChannel socketChannel, String fileName) throws IOException {
        Path filePath = Paths.get(fileName);
        if (Files.exists(filePath)) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            try (FileChannel fileChannel = FileChannel.open(filePath, StandardOpenOption.READ)) {
                buffer.putLong(Files.size(filePath)); // Send the file size
                buffer.flip();
                socketChannel.write(buffer);
                buffer.clear();

                while (fileChannel.read(buffer) > 0) {
                    buffer.flip();
                    socketChannel.write(buffer);
                    buffer.clear();
                }
                logger.info("File sent: " + fileName);
            }
        } else {
            sendMessage(socketChannel, "File not found: " + fileName);
        }
    }

    private static void receiveFile(SocketChannel socketChannel, String fileName) throws IOException {
        Path filePath = Paths.get( fileName);
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        try (FileChannel fileChannel = FileChannel.open(filePath, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
            long fileSize = 0;
            boolean firstRead = true;

            while (socketChannel.read(buffer) > 0) {
                buffer.flip();
                if (firstRead) {
                    fileSize = buffer.getLong(); // Read the file size on the first read
                    firstRead = false;
                }
                fileChannel.write(buffer);
                buffer.clear();
            }
            logger.info("File received: " + fileName);
        }
    }

    private static void sendMessage(SocketChannel socketChannel, String message) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap((message + "\n").getBytes()); // Append newline
        socketChannel.write(buffer);
    }

}
