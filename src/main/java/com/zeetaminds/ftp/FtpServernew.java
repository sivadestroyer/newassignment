package com.zeetaminds.ftp;

import java.net.*;
import java.io.*;

public class FtpServernew {
    ServerSocket ss;
    Socket soc;

    public void doConnections(int port) {
        try {
            ss = new ServerSocket(port);
            while (true) {
                soc = ss.accept();
                CommandHandler commandhandler = new CommandHandler(soc);
                Thread thread = new Thread(commandhandler);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 9806;
        FtpServernew s = new FtpServernew();
        s.doConnections(port);
    }
}
