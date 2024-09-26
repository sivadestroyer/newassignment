package com.zeetaminds.ftp;

import java.io.IOException;

public interface Command {
    void handle() throws IOException;
}
