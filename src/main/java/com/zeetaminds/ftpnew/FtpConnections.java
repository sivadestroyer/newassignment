package com.zeetaminds.ftpnew;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class FtpConnections {
    ServerSocket ss;
    Socket soc;
    Logger logger = Logger.getLogger(FtpConnections.class.getName());
    public void doConnections(int port) {
        try {
            ss = new ServerSocket(port);
            while (true) {
                soc = ss.accept();
//                Parser parser = new Parser(soc);
                newClientConnections client = new newClientConnections(soc);
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
