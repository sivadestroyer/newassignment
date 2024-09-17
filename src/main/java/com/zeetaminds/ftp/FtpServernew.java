package com.zeetaminds.ftp;
import java.net.*;
import java.io.*;
public class FtpServernew {
    ServerSocket ss;
    Socket soc;
    public void doConnections(int port){
        try {
            ss = new ServerSocket(port);
            while (true) {
                soc = ss.accept();
                new CommandHandler(soc).start();
            }
        }catch(Exception e ){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception {
        int port = 9806;
        FtpServernew s = new FtpServernew();
        s.doConnections(port);
    }
}
