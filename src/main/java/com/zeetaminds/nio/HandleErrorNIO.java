package com.zeetaminds.nio;

import com.zeetaminds.ftp.Command;

import java.io.IOException;

public class HandleErrorNIO implements Command {
    private final String message;
    private final DataHandler dataHandler;
    public HandleErrorNIO(DataHandler dataHandler,String message){
        this.message = message;
        this.dataHandler = dataHandler;
    }
    public void handle() throws IOException {
        dataHandler.write(message);
        dataHandler.flush();
    }
}
