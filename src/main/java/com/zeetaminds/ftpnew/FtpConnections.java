package com.zeetaminds.ftpnew;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class FtpConnections {

    private static final Logger logger = Logger.getLogger(FtpConnections.class.getName());

    public void doConnections(int port)  {
        try {
            ServerSocket ss = new ServerSocket(port);
            while (true) {
                Socket soc = ss.accept();
//                Parser parser = new Parser(soc);
                NewClientConnection client = new NewClientConnection(soc);
                Thread thread = new Thread(client);
                thread.start();
            }
        } catch (Exception e) {
            logger.info("error:" + e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 9806;
        FtpConnections s = new FtpConnections();
        s.doConnections(port);
    }
}
