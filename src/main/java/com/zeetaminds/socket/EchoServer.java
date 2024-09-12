package com.zeetaminds.socket;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.concurrent.*;
public class EchoServer {
    public static boolean running = true;
    private static ServerSocket ss;
    private static final int Max_Thread=3;
    private static ExecutorService threadPool;
    private static Semaphore clientLimit;
    public  static void main(String[] args) {

        try {
            clientLimit = new Semaphore(Max_Thread);
            System.out.println("waiting for clients");
            clientLimit.acquire();
            ss = new ServerSocket(9806);
            threadPool = Executors.newFixedThreadPool(Max_Thread);

            new Thread(new ConsoleListener()).start();
            while(running) {
                Socket socket = ss.accept();
                System.out.println("connection established");
                ClientHandler clienthandler = new ClientHandler(socket,clientLimit);
                threadPool.submit(clienthandler);
            }
            threadPool.shutdown();
            ss.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void stop() {
        running = false;
        try {
            if (ss != null && !ss.isClosed()) {
                ss.close();  // Close the server socket to unblock the accept() call
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
class ConsoleListener implements Runnable {
    public void run() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (true) {
               String command = in.readLine();
                if (command.equals("1")) {
                    EchoServer.stop();
                    break;
                }
            }

        }catch (Exception e) {}
    }
}
class ClientHandler implements Runnable {
    private Socket socket;
    private Semaphore clientLimit;
    public ClientHandler(Socket socket, Semaphore clientLimit){
        this.socket = socket;
        this.clientLimit = clientLimit;
    }

    public void run(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            String clientInput;
            while((clientInput = in.readLine()) != null){
            System.out.println("Received: " + clientInput);
            out.println("server say"+clientInput);
            }
            socket.close();
            System.out.println("client disconnected");
        }catch (Exception e){
            e.printStackTrace();
        } finally{
            clientLimit.release();
        }
    }
}