package com.zeetaminds.socket;

import java.io.*;
import java.net.*;

public class EchoServerThread {
    public static void main(String[] args) throws IOException {
        int port = 9806;
        ServerSocket ss = new ServerSocket(port);
        while (true) {
            Socket soc = ss.accept();
            EchoThread echothread = new EchoThread(soc);
            Thread t = new Thread(echothread);
            t.start();
        }
    }
}
