package com.zeetaminds.socket;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
public class NIOEechoServer {
    private Selector selector;
    private ByteBuffer buffer = ByteBuffer.allocate(256);
    public static void main(String[] args) throws IOException {
        NIOEechoServer server = new NIOEechoServer();
        server.startServer();
    }
    public void startServer() throws IOException {
        selector = Selector.open();
        ServerSocketChannel ssc =ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(9806));
        ssc.configureBlocking(false);
        ssc.register(selector,SelectionKey.OP_ACCEPT);
        System.out.println("sever started on 9806");
        while (true) {
            selector.select();
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                if (key.isAcceptable()) {
                    accept(key);
                } else if (key.isReadable()) {
                    read(key);
                }
            }
        }
    }
    private void accept(SelectionKey key) throws IOException {
         ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
         SocketChannel sc = ssc.accept();
         sc.configureBlocking(false);
         sc.register(selector, SelectionKey.OP_READ);
         System.out.println("new client connected");
    }
    private void read(SelectionKey key) throws IOException {
        SocketChannel sc = (SocketChannel) key.channel();
        buffer.clear();
        int readCount = sc.read(buffer);
        if (readCount > 0) {
            buffer.flip();
            while (buffer.hasRemaining()) {
                sc.write(buffer);
            }
        } else {
            key.cancel();
            sc.close();
        }
    }
}
