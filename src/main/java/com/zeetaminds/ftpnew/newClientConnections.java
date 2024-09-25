package com.zeetaminds.ftpnew;

import com.zeetaminds.ftp.Command;

import java.io.IOException;
import java.io.InputStream;
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
        try (InputStream in = socket.getInputStream();
             OutputStream out = socket.getOutputStream()) {

            byte[] buffer = new byte[1024];
            parser.ParsingMethod(in, out);
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
