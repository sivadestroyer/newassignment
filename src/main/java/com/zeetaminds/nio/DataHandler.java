package com.zeetaminds.nio;

import java.io.IOException;

public interface DataHandler {

    int read(byte[] buffer) throws IOException;

    void write(byte[] buffer, int offset, int length) throws IOException;

    void write(String data) throws IOException;

    void mark(int limit);

    void reset() throws IOException;

    void skip(long n) throws IOException;

    void close() throws IOException;;

    void flush() throws IOException;
}
