package com.zeetaminds.ftp;

import java.io.IOException;
import java.io.OutputStream;

public class HandleError implements Command {
    private final OutputStream out;
    private final String message;
    public HandleError(OutputStream out, String message) {
        this.out = out;
        this.message = message;
    }

    public void handle() throws IOException {
        out.write(message.getBytes());
    }


}
