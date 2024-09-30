package com.zeetaminds.nio;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class IODataHandler implements DataHandler {

    private final InputStream in;
    private final OutputStream out;

    public IODataHandler(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public void write(byte[] data,int i,int j) throws IOException {
        out.write(data,i,j);
    }

    @Override
    public void write(String data) throws IOException {
        out.write(data.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public int read(byte[] buffer) throws IOException {
        return in.read(buffer);
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }
    public void mark(int limit) {
        in.mark(limit);
    }

    @Override
    public void reset() throws IOException {
        in.reset();
    }

    @Override
    public void skip(long n) throws IOException {
        in.skip(n);
    }

    @Override
    public void close() throws IOException {
        in.close();
        out.close();
    }
}
