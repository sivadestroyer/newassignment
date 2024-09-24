package com.zeetaminds.ftp;

import java.net.*;
import java.io.*;
import java.util.logging.Logger;
public class FtpServernew {
    ServerSocket ss;
    Socket soc;
    Logger logger = Logger.getLogger(FtpServernew.class.getName());
    public void doConnections(int port) {
        try {
            ss = new ServerSocket(port);
            while (true) {
                soc = ss.accept();
                Parser commandhandler = new Parser(soc);
                Thread thread = new Thread(commandhandler);
                thread.start();
            }
        } catch (Exception e) {
            logger.info("error:" + e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 9806;
        FtpServernew s = new FtpServernew();
        s.doConnections(port);
    }
}
