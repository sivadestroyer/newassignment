package com.zeetaminds.socket;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
public class EchoServer {
    public  static void main(String[] args) {
        try {
            System.out.println("waiting for clients");
            ServerSocket ss = new ServerSocket(9806);
            Socket soc =ss.accept();
            System.out.println("connection established");
            BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            String str = in.readLine();
            PrintWriter out = new PrintWriter(soc.getOutputStream(),true);
            out.println("sever says : " + str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
