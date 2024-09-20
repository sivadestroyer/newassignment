package com.zeetaminds.socket;
import java.io.*;
import java.net.*;
import java.util.logging.Logger;
public class SendMessageHandler implements Runnable{
    Logger logger = Logger.getLogger(SendMessageHandler.class.getName());
    private Socket socket;
    public SendMessageHandler(Socket socket){
        this.socket=socket;
    }
    @Override
    public  void run(){
        try {
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while((line = in.readLine()) != null){
                out.println(line);
            }

        }catch (IOException e){
            logger.info(e.getMessage());
        }finally{
            try{
                if(!socket.isClosed() && socket!=null){
                    socket.close();
                }
            }catch(IOException e){
                logger.info("error"+e.getMessage());
            }
        }
    }
}
