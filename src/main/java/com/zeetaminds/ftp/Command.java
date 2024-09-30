package com.zeetaminds.ftp;

import java.io.IOException;
import java.io.OutputStream;

public interface Command {
    void handle() throws IOException;
}
