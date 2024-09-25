package com.zeetaminds.ftpnew;

import com.zeetaminds.ftp.FtpServernew;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class ftpconnections {
    ServerSocket ss;
    Socket soc;
    Logger logger = Logger.getLogger(ftpconnections.class.getName());
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
        ftpconnections s = new ftpconnections();
        s.doConnections(port);
    }
}
