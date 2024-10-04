package com.zeetaminds.newnio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

public class FtpConnection {

    private static final Logger logger = Logger.getLogger(FtpConnection.class.getName());

    public void doConnections(int port) {
        try (Selector selector = Selector.open();
             ServerSocketChannel serverChannel = ServerSocketChannel.open()) {

            serverChannel.bind(new InetSocketAddress(port));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                selector.select();  // Blocking until at least one channel is ready
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    if (key.isAcceptable()) {
                        // Accept the new connection
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel clientChannel = server.accept();
                        clientChannel.configureBlocking(false);
                        boolean isReadable=true;
                        // Register the client channel for reading
                        clientChannel.register(selector, SelectionKey.OP_READ,
                                new ClientConnection(clientChannel, isReadable));
                    } else if (key.isReadable()) {
                        // Read data from the client
                        boolean isReadable=true;
                        ClientConnection client = (ClientConnection) key.attachment();
                        client.sessionState.isReadable=true;
                        client.read();
                    }
                }
            }

        } catch (IOException e) {
            logger.info("error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        int port = 9806;
        FtpConnection s = new FtpConnection();
        s.doConnections(port);
    }
}
