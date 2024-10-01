package com.zeetaminds.newnio;
import com.zeetaminds.ftp.Command;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public class HandleError implements Command {
    private final WritableByteChannel channel;
    private final String message;

    public HandleError(WritableByteChannel channel, String message) {
        this.channel = channel;
        this.message = message;
    }

    public void handle() throws IOException {
        // Convert the message string to a ByteBuffer
        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());

        // Write the buffer to the channel
        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }
    }
}

