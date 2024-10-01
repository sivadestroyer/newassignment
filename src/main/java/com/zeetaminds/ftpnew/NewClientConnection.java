package com.zeetaminds.ftpnew;

import com.zeetaminds.ftp.Command;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NewClientConnection implements Runnable {

    private static final Logger LOG = Logger.getLogger(NewClientConnection.class.getName());

    private final Socket socket;

    public NewClientConnection(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        Parser parser = new Parser();
        try (BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
             OutputStream out = socket.getOutputStream()) {

            while (!socket.isClosed()) {
                // Parsing and obtaining the command object
                Command cmd = parser.parse(in, out);
                // Handling the command and writing the response back to the client
                if(cmd==null) {
                    LOG.info("null exception");
                    break;  // Close the connection when the client sends a "bye" command
                }
                cmd.handle();

            }

        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error reading from socket", e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                LOG.log(Level.SEVERE, "Error closing socket", e);
            }
        }
    }
}
