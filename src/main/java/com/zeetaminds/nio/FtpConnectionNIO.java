package com.zeetaminds.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.logging.Logger;

public class FtpConnectionNIO {
    private static final Logger logger = Logger.getLogger(FtpConnectionNIO.class.getName());
    private Selector selector;

    public void doConnections(int port) {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            // Configure non-blocking mode
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(port));
            selector = Selector.open();

            // Register the server socket channel with the selector for accept operations
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                // Block until there's an event
                selector.select();

                // Iterate over the selected keys
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove(); // Remove the key to avoid processing it again

                    if (key.isAcceptable()) {
                        handleAccept(serverSocketChannel);
                    } else if (key.isReadable()) {
                        handleRead(key);
                    }
                }
            }
        } catch (IOException e) {
            logger.severe("Error: " + e.getMessage());
        }
    }

    private void handleAccept(ServerSocketChannel serverSocketChannel) throws IOException {
        SocketChannel socketChannel = serverSocketChannel.accept();
        if (socketChannel != null) {
            socketChannel.configureBlocking(false); // Configure non-blocking mode
            // Register the new connection for read operations
            socketChannel.register(selector, SelectionKey.OP_READ, new ClientConnectionsNIO(socketChannel));
        }
    }

    private void handleRead(SelectionKey key) throws IOException {
        ClientConnectionsNIO client = (ClientConnectionsNIO) key.attachment();
        client.run(key); // Process the client connection
    }

    public static void main(String[] args) throws Exception {
        int port = 9806;
        FtpConnectionNIO server = new FtpConnectionNIO();
        server.doConnections(port);
    }
}
