package com.zeetaminds.ftp;

import java.net.*;
import java.io.*;

public class FtpServerProtocol{
    ServerSocket ss;
    Socket soc;

    public void doConnections(int port) {
        try {
            ss = new ServerSocket(port);
            while (true) {
                soc = ss.accept();
                CommandHandlerProtocol commandhandlerprotocol = new CommandHandlerProtocol(soc);
                Thread thread = new Thread(commandhandlerprotocol);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 9806;
        FtpServerProtocol s = new FtpServerProtocol();
        s.doConnections(port);
    }
}
