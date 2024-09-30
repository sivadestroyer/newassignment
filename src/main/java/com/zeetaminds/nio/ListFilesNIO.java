package com.zeetaminds.nio;


import com.zeetaminds.ftp.Command;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListFilesNIO implements Command {

    private final DataHandler dataHandler;

    public ListFilesNIO(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    public void handle() throws IOException {
        File dir = new File(".");
        File[] files = dir.listFiles();
        files = files == null ? new File[0] : files;

        List<File> fileList = Arrays.stream(files)
                .filter(File::isFile)
                .collect(Collectors.toList());

        dataHandler.write(fileList.size() + "\n");
        for (File file : fileList) {
            dataHandler.write(file.getName() + "\n");
        }
        dataHandler.flush();
    }
}

