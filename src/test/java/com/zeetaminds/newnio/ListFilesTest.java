package com.zeetaminds.newnio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ListFilesTest {

    private SocketChannel mockClientChannel;
    private ListFiles listFiles;

    @BeforeEach
    void setUp() {
        mockClientChannel = mock(SocketChannel.class); // Mock SocketChannel
        listFiles = new ListFiles(mockClientChannel);   // Create an instance of ListFiles with the mock channel
    }

    @Test
    void handleWritesCorrectDataToChannel() throws Exception {
        // Set up a mock directory with test files
        File mockDir = mock(File.class);
        File file1 = mock(File.class);
        File file2 = mock(File.class);

        when(mockDir.listFiles()).thenReturn(new File[]{file1, file2});
        when(file1.isFile()).thenReturn(true);
        when(file2.isFile()).thenReturn(true);
        when(file1.getName()).thenReturn("testFile1.txt");
        when(file2.getName()).thenReturn("testFile2.txt");

        // Set the mock directory in the ListFiles instance
        listFiles.setDirectory(mockDir);

        // Call the handle method
        listFiles.handle();

        // Capture the ByteBuffer that was written to the mock SocketChannel
        ArgumentCaptor<ByteBuffer> bufferCaptor = ArgumentCaptor.forClass(ByteBuffer.class);
        verify(mockClientChannel).write(bufferCaptor.capture());

        ByteBuffer buffer = bufferCaptor.getValue();

        // Convert ByteBuffer to a string for verification
        String writtenData = StandardCharsets.UTF_8.decode(buffer).toString();

        // Verify the content written to the channel
        assertEquals("2\ntestFile1.txt\ntestFile2.txt\n", writtenData);
    }

    @Test
    void handleWithEmptyDirectory() throws Exception {
        // Set up an empty mock directory
        File mockDir = mock(File.class);
        when(mockDir.listFiles()).thenReturn(new File[0]);

        // Set the mock directory in the ListFiles instance
        listFiles.setDirectory(mockDir);

        // Call the handle method
        listFiles.handle();

        // Capture the ByteBuffer that was written to the mock SocketChannel
        ArgumentCaptor<ByteBuffer> bufferCaptor = ArgumentCaptor.forClass(ByteBuffer.class);
        verify(mockClientChannel).write(bufferCaptor.capture());

        ByteBuffer buffer = bufferCaptor.getValue();

        // Convert ByteBuffer to a string for verification
        String writtenData = StandardCharsets.UTF_8.decode(buffer).toString();

        // Verify the content written to the channel (empty directory scenario)
        assertEquals("0\n", writtenData);
    }
}
