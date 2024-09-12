package com.zeetaminds.socket;
import java.io.*;
import java.net.*;
public class Server {
    public static void main(String[] args) {
        try {
            System.out.println("waiting for client");
            ServerSocket ss = new ServerSocket(9086);
            while (true) {
                Socket soc = ss.accept();
                System.out.println("client connected");
                clienthandler client = new clienthandler(soc);
                new Thread(client).start();

            }

        }catch (Exception e) {
            System.out.println(e);
        }
    }
}

class clienthandler implements Runnable{
    private Socket soc;
    public clienthandler(Socket soc){
        this.soc = soc;
    }
    public void run(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            PrintWriter out = new PrintWriter(soc.getOutputStream(),true);
            String str = in.readLine();
            System.out.println("received"+ str);;
            out.println("message from server"+str);
            soc.close();
        }catch(Exception e){
            System.out.println(e);
        }

    }


}