package com.zeetaminds.socket;
import java.net.Socket;
import java.io.*;
public class EchoClient {
    public static void main(String[] args) throws Exception {
        try{
            System.out.println("Client started");
            Socket soc= new Socket("localhost",9806);
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("enter the string");
            String str = in.readLine();
            PrintWriter out = new PrintWriter(soc.getOutputStream(),true);
            out.println(str);
            BufferedReader input = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            System.out.println(input.readLine());
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
