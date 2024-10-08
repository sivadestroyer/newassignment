package com.zeetaminds.newnio;

import com.zeetaminds.newnio.ReceiveFiles;
import com.zeetaminds.newnio.SessionState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReceiveFilesTest {

    private SocketChannel mockChannel;
    private SessionState mockSessionState;
    private ByteBuffer buffer;

    private final String fileName = "testfile.txt";
    private final long fileLen = 12L; // length of the incoming file in bytes
    private byte[] fileContent = "Hello World!".getBytes(); // content of the file

    @BeforeEach
    public void setup() {
        mockChannel = mock(SocketChannel.class);
        mockSessionState = mock(SessionState.class);
        buffer = ByteBuffer.allocate(1024);
        when(mockSessionState.getBuffer()).thenReturn(buffer);
    }

    @Test
    public void testReceiveFiles() throws IOException {
        // Prepare the file receiver
        ReceiveFiles receiveFiles = new ReceiveFiles(fileName, mockChannel, fileLen, mockSessionState);

        // Simulate SocketChannel returning file data
        when(mockChannel.read(any(ByteBuffer.class))).thenAnswer(invocation -> {
            ByteBuffer byteBuffer = invocation.getArgument(0);
            byteBuffer.put(fileContent); // Simulate writing the file content to the buffer
            return fileContent.length;   // Return the number of bytes read
        });

        // Call the handle() method which should process the file
        receiveFiles.handle();

        // Verify the file has been written correctly by reading the file back
        File receivedFile = new File(fileName);
        assert receivedFile.exists() : "File should exist after receiving";

        try (FileInputStream fis = new FileInputStream(receivedFile)) {
            byte[] receivedBytes = new byte[(int) fileLen];
            int bytesRead = fis.read(receivedBytes);
            assertArrayEquals(fileContent, receivedBytes, "File content should match expected content");
            assert bytesRead == fileLen : "The number of bytes read should match the file length";
        }

        // Clean up the test file
        receivedFile.delete();
    }
}
