package com.zeetaminds.newnio;
import java.nio.ByteBuffer;
public class SessionState {
    private final ByteBuffer buffer;
    boolean isReadable=true;
    public SessionState(){
        this.buffer=ByteBuffer.allocate(1024);
    }
    public ByteBuffer getBuffer(){
        return buffer;
    }
    public void resetBuffer(){
        buffer.clear();
    }

}
