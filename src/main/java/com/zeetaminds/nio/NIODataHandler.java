package com.zeetaminds.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class NIODataHandler implements DataHandler {

    private final SocketChannel channel;
    private final ByteBuffer buffer;

    public NIODataHandler(SocketChannel channel, ByteBuffer buffer) {
        this.channel = channel;
        this.buffer = buffer;
    }

    @Override
    public void write(byte[] data,int i,int j) throws IOException {
        buffer.clear();
        buffer.put(data,i,j);
        buffer.flip();
        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }
    }

    @Override
    public void write(String data) throws IOException {
        write(data.getBytes(StandardCharsets.UTF_8),0,data.length());
    }

    @Override
    public int read(byte[] buffer) throws IOException {
        ByteBuffer tempBuffer = ByteBuffer.wrap(buffer);
        return channel.read(tempBuffer);
    }

    @Override
    public void flush() throws IOException {
        // No need to flush for NIO as data is directly sent when writing to the channel.
    }
    public void mark(int limit) {
        // NIO doesn't support mark/reset, you can use internal buffering if needed
    }

    @Override
    public void reset() {
        // Not applicable for NIO; leave it empty or handle differently
    }

    @Override
    public void skip(long n) {
        // NIO doesn't support skip, handle as necessary for your case
    }

    @Override
    public void close() throws IOException {
        channel.close();
    }
}
