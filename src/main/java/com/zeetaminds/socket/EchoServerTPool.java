package com.zeetaminds.socket;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class EchoServerTPool {
    private static final int PORT = 9806;
    private static final int MAX_THREADS = 10;

    public static void main(String[] args) throws IOException {
        ExecutorService threadPool = Executors.newFixedThreadPool(MAX_THREADS);
        ServerSocket ss = new ServerSocket(PORT);
        try {
            while (true) {
                Socket soc = ss.accept();
                ClientHandler handler = new ClientHandler(soc);
                threadPool.submit(handler);

            }
        }catch(IOException e) {
            e.printStackTrace();
        }finally
         {
            try{
            threadPool.shutdown();
            ss.close();
            } catch (IOException e) {
                e.printStackTrace();
     }

        }
    }

    static class ClientHandler implements Runnable {
        private Socket soc;

        public ClientHandler(Socket soc) {
            this.soc = soc;
        }

        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
                PrintWriter out = new PrintWriter(soc.getOutputStream(), true);
                String str;
                while ((str = in.readLine()) != null) {
                    System.out.println("Received: " + str);
                    out.println("server say: " + str);
                }
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                try {
                    soc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
