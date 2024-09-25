package com.zeetaminds.ftpnew;

import com.zeetaminds.ftp.Command;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class newClientConnections implements Runnable {
    private Socket socket;
    private static Logger LOG = Logger.getLogger(newClientConnections.class.getName());

    public newClientConnections(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        Parsing parser = new Parsing();
        try (BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
             OutputStream out = socket.getOutputStream()) {

            boolean connectionActive = true;

            while (connectionActive) {
                // Parsing and obtaining the command object
                Command cmd = parser.ParsingMethod(in, out);
                // Handling the command and writing the response back to the client
                cmd.handle();
                out.flush();
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
