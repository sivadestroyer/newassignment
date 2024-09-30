
package com.zeetaminds.ftp;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ListFiles implements Command {

    private static final Logger LOG  = Logger.getLogger(ListFiles.class.getName());

    private final OutputStream outputStream;
    private File directory=new File(".");
    public ListFiles(OutputStream outputStream) {
        this.outputStream = outputStream;
    }
    public void setDirectory(File directory) {
        this.directory = directory;
    }

    public void handle() throws IOException {
        File[] files = directory.listFiles();
        files = files == null ? new File[0] : files;

        List<File> fileList = Arrays.stream(files)
                .filter(File::isFile)
                .collect(Collectors.toList());


            // Write the number of files to the output stream
            outputStream.write((fileList.size() + "\n").getBytes(StandardCharsets.UTF_8));

            // Then write the file names to the output stream
            for (File file : fileList) {
                outputStream.write((file.getName() + "\n").getBytes(StandardCharsets.UTF_8));
            }
    }
}
