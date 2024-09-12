package com.zeetaminds.io.Logger;

import java.util.Date;
import java.text.SimpleDateFormat;

public class TimeStampedFileLogger extends FileLogger {
    public TimeStampedFileLogger(String filename) {
        super(filename);
    }


    public void log(String message) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        super.log(timestamp + " - " + message);

    }
}
