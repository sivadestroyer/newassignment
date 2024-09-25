package com.zeetaminds.nio;



import com.zeetaminds.ftp.Command;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientConnectionsNIO {
    private static final Logger LOG = Logger.getLogger(ClientConnectionsNIO.class.getName());
    private final SocketChannel socketChannel;
    private final ByteBuffer buffer = ByteBuffer.allocate(1024); // Buffer to store data

    public ClientConnectionsNIO(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public void run(SelectionKey key) {
        ParsingNIO parser = new ParsingNIO();
        try {
            if (socketChannel.read(buffer) == -1) {
                socketChannel.close(); // Close the connection if the channel is closed
                key.cancel(); // Cancel the key if the client has disconnected
                return;
            }

            // Switch buffer to read mode
            buffer.flip();

            // Parse commands and handle the result
            Command cmd = parser.ParsingMethod(buffer, socketChannel);
            if (cmd != null) {
                cmd.handle();
            }

            buffer.clear(); // Clear the buffer for the next read

        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error reading from socket", e);
            try {
                socketChannel.close();
                key.cancel();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "Error closing socket", ex);
            }
        }
    }
}

