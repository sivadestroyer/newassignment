package com.zeetaminds.ftp;

import java.io.IOException;
import java.util.concurrent.*;
import java.net.*;


public class FtpServerThreadPool {
    public static void main(String[] args) {
        int port = 9806;
        int max_thread = 5;
        ExecutorService executor = Executors.newFixedThreadPool(max_thread);
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(port);

            while (true) {
                Socket socket = ss.accept();
                executor.submit(new Parser(socket));

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}