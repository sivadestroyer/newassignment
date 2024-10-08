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
    int bytesRead = 0;
    public ClientConnection(SocketChannel clientChannel, boolean isReadable) {
        this.clientChannel = clientChannel;
        this.sessionState = new SessionState();
        this.sessionState.isReadable = isReadable;
    }

    public void read() throws IOException {
        try{
            ByteBuffer buffer = sessionState.getBuffer();
            buffer.clear();
            while(sessionState.isReadable && buffer.remaining()>0) {
                // Process the data (parsing and handling command)
                if(sessionState.isReceive){
                    bytesRead=clientChannel.read(buffer);
                    if(bytesRead==-1) LOG.info("null exception");
                    buffer.flip();
                    ReceiveFiles obj = (ReceiveFiles) sessionState.getSharedObject();
                    obj.handle();
                }
                else if(buffer.position()==0&&buffer.limit()==buffer.capacity() ) {
                    bytesRead = clientChannel.read(buffer);
                    if (bytesRead == -1) LOG.info("null exception");
                    buffer.flip();

                }
                if(sessionState.iscommand){
                    Command cmd = parser.parse(buffer, clientChannel, sessionState);
                    if (cmd == null) {
                        LOG.info("null exception");
                        clientChannel.close();  // Close connection on "bye" command
                        break;
                    } else {
                        cmd.handle();
                    }

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

