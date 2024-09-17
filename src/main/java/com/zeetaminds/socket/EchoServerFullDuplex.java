package com.zeetaminds.socket;

import java.io.IOException;
import java.util.logging.Logger;
import java.net.*;
public class EchoServerFullDuplex {
    private static final Logger logger = Logger.getLogger(EchoServerFullDuplex.class.getName());
    public static void main(String[] args)  {
        int port=9806;
        ServerSocket ss;
        try {
            ss = new ServerSocket(port);
            while(true) {
                Socket socket = ss.accept();
                new Thread(new EchoHandlerDuplex(socket)).start();
                Thread sendThread = new Thread(new SendMessageHandler(socket));
                sendThread.start();
            }
        }catch(Exception e) {
            logger.info(e.getMessage());
        }

    }
}
