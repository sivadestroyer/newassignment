package com.zeetaminds.newnio;


import com.zeetaminds.ftp.Command;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

public class ReceiveFiles implements Command {

    private static final Logger LOG = Logger.getLogger(ReceiveFiles.class.getName());

    private final String fileName;
    private final SocketChannel clientChannel;
    private long fileLen;
    private final SessionState sessionState;
    long totalBytes = 0;

    public ReceiveFiles(String fileName, SocketChannel clientChannel, long fileLen,SessionState sessionState) {
        this.fileName = fileName;
        this.clientChannel = clientChannel;
        this.fileLen = fileLen;
        this.sessionState = sessionState;
    }

    public void handle() throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
            ByteBuffer buffer1 = ByteBuffer.allocate(1024); // Buffer for file transfer
            ByteBuffer buffer = sessionState.getBuffer();
        while(totalBytes<fileLen)

            {
                if (buffer.hasRemaining() && (buffer.limit() - buffer.position()) > fileLen
                        && (buffer.position() != 0)) {
                    byte[] subsetArray = new byte[(int) fileLen];
                    buffer.get(subsetArray, 0, (int) fileLen);
                    buffer1 = ByteBuffer.wrap(subsetArray);
                    if (buffer.limit() == buffer.position()) {
                        buffer.clear();
                    }
                }
                if (buffer.hasRemaining() && (buffer.limit() - buffer.position()) < (int) fileLen) {
                    buffer1 = ByteBuffer.allocate(buffer.remaining());
                    buffer.clear();
                }
                if (buffer.position() == 0) {
                    int bytesRead = clientChannel.read(buffer1);
                    buffer1.flip();
                    if (bytesRead == -1) break;
                }
                while (buffer1.hasRemaining() && totalBytes < fileLen) {

                    fos.write(buffer1.get());
                    totalBytes++;

                }
                boolean flag=false;
                while(buffer1.hasRemaining()){
                    byte b=buffer1.get();
                    buffer.put(b);
                    flag=true;
                }
                if(flag) {
                    buffer.flip();
                    sessionState.isReadable=true;
                }

                buffer1.clear();

            }

    }
}
