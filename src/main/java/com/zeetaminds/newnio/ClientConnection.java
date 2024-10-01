package com.zeetaminds.newnio;


import com.zeetaminds.ftp.Command;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientConnection {

    private static final Logger LOG = Logger.getLogger(ClientConnection.class.getName());
    private static final int BUFFER_SIZE = 1024;

    private final SocketChannel clientChannel;
    private final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
    private final Parser parser = new Parser();

    public ClientConnection(SocketChannel clientChannel) {
        this.clientChannel = clientChannel;
    }

    public void read() {
        try {
            int bytesRead = clientChannel.read(buffer);
            if (bytesRead == -1) {
                clientChannel.close();
                return;
            }

            // Flip buffer for reading the data
            buffer.flip();

            // Process the data (parsing and handling command)
            Command cmd = parser.parse(buffer, clientChannel);
            if (cmd == null) {
                LOG.info("null exception");
                clientChannel.close();  // Close connection on "bye" command
            } else {
                cmd.handle();
            }

            // Clear the buffer for the next read
            buffer.clear();

        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error reading from socket", e);
            try {
                clientChannel.close();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "Error closing socket", ex);
            }
        }
    }
}

