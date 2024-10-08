package com.zeetaminds.newnio;

import com.zeetaminds.ftp.Command;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

public class ReceiveFiles implements Command {

    private static final Logger LOG = Logger.getLogger(ReceiveFiles.class.getName());

    private final String fileName;
    private final SocketChannel clientChannel;
    private long fileLen;
    private final SessionState sessionState;
    long totalBytes = 0;

    public ReceiveFiles(String fileName, SocketChannel clientChannel, long fileLen, SessionState sessionState) {
        this.fileName = fileName;
        this.clientChannel = clientChannel;
        this.fileLen = fileLen;
        this.sessionState = sessionState;
    }

    public void handle() throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName, true);
        ByteBuffer buffer = sessionState.getBuffer();
        int size = (int) Math.min(buffer.limit() - buffer.position(), fileLen - totalBytes);

        if (buffer.remaining() > 0 && buffer.remaining() < 1024) {
            fos.write(buffer.array(), buffer.position(), size);
            totalBytes += size;
        }
        if (totalBytes < fileLen) sessionState.reset(this);
        else sessionState.clear(size);
    }

}



       /*
        buffSize
        toWrite
        tBWritten =+ file.write(buff(pos, Math.min(buffSize, toWrite)
        if(tBWritten == fLenght) {
        clear();
        }

        if(buffSize <= toWrite)
        bytesToRead()
         */

