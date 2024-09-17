package com.zeetaminds.socket;
import java.net.*;
import java.io.*;
import java.util.logging.Logger;
public class EchoHandlerDuplex implements Runnable {
    private Socket socket;
    Logger logger = Logger.getLogger(EchoHandlerDuplex.class.getName());
    public EchoHandlerDuplex(Socket socket) {
        this.socket = socket;
    }
    public void run() {
        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out =new PrintWriter(socket.getOutputStream());
            String str;
            while((str=in.readLine()) != null){
                logger.info("Client : " + str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try{
                if(!socket.isClosed() && socket !=null){
                    socket.close();
                }
            }catch(IOException e){
                logger.info("error"+e.getMessage());
            }
        }

    }
}
