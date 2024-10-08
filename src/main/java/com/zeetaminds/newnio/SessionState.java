package com.zeetaminds.newnio;
import java.nio.ByteBuffer;
public class SessionState {
    private final ByteBuffer buffer;
    boolean isReadable=true;
    boolean isReceive=false;
    boolean iscommand=true;
    public Object sharedObject;
    public SessionState(){
        this.buffer=ByteBuffer.allocate(1024);
    }
    public ByteBuffer getBuffer(){
        return buffer;
    }
    public void resetBuffer(){
        buffer.clear();
    }
    public void setSharedObject(Object obj) {
        this.sharedObject = obj;  // Store the object in session state
    }

    public Object getSharedObject() {
        return sharedObject;  // Retrieve the stored object
    }
    public void reset(Object obj) {
        this.isReceive = true;
        this.setSharedObject(obj);
        this.iscommand = false;
        this.isReadable = false;
        this.buffer.clear();  // Clear the buffer for the next read
    }
    public void clear(int size){
        this.isReceive = false;
        this.iscommand = true;
        this.buffer.position(size);
    }
}
