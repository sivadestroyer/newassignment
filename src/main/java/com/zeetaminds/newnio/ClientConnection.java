package com.zeetaminds.newnio;


import com.zeetaminds.ftp.Command;
import com.zeetaminds.newnio.exceptions.InvalidComandException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientConnection {

    private static final Logger LOG = Logger.getLogger(ClientConnection.class.getName());
    private static final int BUFFER_SIZE = 1024;

    private final SocketChannel clientChannel;
    private final Parser parser = new Parser();
    protected final SessionState sessionState;

    public ClientConnection(SocketChannel clientChannel, boolean isReadable) {
        this.clientChannel = clientChannel;
        this.sessionState = new SessionState();
        this.sessionState.isReadable = isReadable;
    }

    public void read() throws IOException {
        try{
            ByteBuffer buffer = sessionState.getBuffer();
            buffer.clear();
            while(sessionState.isReadable) {
                // Process the data (parsing and handling command)
                Command cmd = parser.parse(buffer, clientChannel,sessionState);
                if (cmd == null) {
                    LOG.info("null exception");
                    clientChannel.close();  // Close connection on "bye" command
                    break;
                } else {
                    cmd.handle();
                }
            }
            // Clear the buffer for the next read

        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error reading from socket", e);
            try {
                clientChannel.close();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "Error closing socket", ex);
            }
        } catch (InvalidComandException e) {
            throw new RuntimeException(e);
        }
    }
}

