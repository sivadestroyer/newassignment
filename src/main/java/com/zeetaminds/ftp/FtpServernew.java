package com.zeetaminds.ftp;

import java.net.*;
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
                Parsing parser = new Parsing(soc);
                Thread thread = new Thread(parser);
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
