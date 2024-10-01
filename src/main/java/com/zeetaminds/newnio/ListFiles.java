package com.zeetaminds.newnio;


import com.zeetaminds.ftp.Command;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ListFiles implements Command {

    private static final Logger LOG  = Logger.getLogger(ListFiles.class.getName());

    private final SocketChannel clientChannel;
    private File directory = new File(".");

    public ListFiles(SocketChannel clientChannel) {
        this.clientChannel = clientChannel;
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

        // Prepare the ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // Write the number of files to the buffer
        buffer.put((fileList.size() + "\n").getBytes(StandardCharsets.UTF_8));

        // Write the file names to the buffer
        for (File file : fileList) {
            buffer.put((file.getName() + "\n").getBytes(StandardCharsets.UTF_8));
        }

        // Flip the buffer before writing to the channel
        buffer.flip();
        clientChannel.write(buffer);
    }
}
