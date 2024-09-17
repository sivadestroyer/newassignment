package com.zeetaminds.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public  class EchoThread implements Runnable {
    private Socket socket;

    public EchoThread(Socket soc) {
        this.socket = soc;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            String str;
            while((str= in.readLine()) != null) {
                System.out.println("received: " + str);
                out.println("server say: " + str);
            }
        } catch (Exception e) {
            System.out.println(e);
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
