package com.zeetaminds.newnio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class SendFilesTest {

    private SocketChannel mockClientChannel;
    private FileInputStream mockFileInputStream;
    private FileChannel mockFileChannel;

    @BeforeEach
    public void setUp() throws IOException {
        // Mock dependencies
        mockClientChannel = mock(SocketChannel.class);
        mockFileInputStream = mock(FileInputStream.class);
        mockFileChannel = mock(FileChannel.class);
    }

    @Test
    public void testHandle_FileExistsAndIsSent() throws IOException {
        // Given
        String testFileName = "test.txt";
        File mockFile = mock(File.class);
        byte[] mockFileContent = "FileContentHere".getBytes(StandardCharsets.UTF_8);

        // Simulate that the file exists and is a file
        when(mockFile.exists()).thenReturn(true);
        when(mockFile.isFile()).thenReturn(true);
        when(mockFile.length()).thenReturn((long) mockFileContent.length); // Set file size to length of content

        // Mock the input stream and file channel
        when(mockFileInputStream.getChannel()).thenReturn(mockFileChannel);
        when(mockFileChannel.size()).thenReturn((long) mockFileContent.length); // Ensure size matches

        // Create an instance of SendFiles with mock dependencies
        SendFiles sendFiles = Mockito.spy(new SendFiles(testFileName, mockClientChannel));

        // Mock methods in SendFiles to use the mocks
        doReturn(mockFile).when(sendFiles).getFile(testFileName);
        doReturn(mockFileInputStream).when(sendFiles).getFileInputStream(mockFile);

        // Simulate file transfer using transferTo
        when(mockFileChannel.transferTo(anyLong(), anyLong(), eq(mockClientChannel))).thenAnswer(invocation -> {
            long position = invocation.getArgument(0);
            long count = invocation.getArgument(1);

            // Here we are simulating that we transferred `count` bytes of data.
            ByteBuffer buffer = ByteBuffer.wrap(mockFileContent);
            mockClientChannel.write(buffer);

            return count; // Return the count of bytes transferred
        });

        // When: handle is called
        sendFiles.handle();

        // Then: Verify file size is sent first
        ByteBuffer expectedSizeBuffer = ByteBuffer.wrap((mockFile.length() + "\n").getBytes(StandardCharsets.UTF_8));
        verify(mockClientChannel).write(expectedSizeBuffer);

        // Verify the file was sent
        verify(mockFileChannel, times(1)).transferTo(0, mockFile.length(), mockClientChannel);
    }

    @Test
    public void testHandle_FileNotFound() throws IOException {
        // Given
        String testFileName = "nonexistent.txt";
        File mockFile = mock(File.class);

        // Simulate that the file does not exist
        when(mockFile.exists()).thenReturn(false);

        // Create an instance of SendFiles with mock dependencies
        SendFiles sendFiles = Mockito.spy(new SendFiles(testFileName, mockClientChannel));

        // Mock methods in SendFiles to use the mocks
        doReturn(mockFile).when(sendFiles).getFile(testFileName);

        // When: handle is called
        sendFiles.handle();

        // Then: Verify error message is sent to the client
        ByteBuffer expectedErrorBuffer = ByteBuffer.wrap("ERROR: File not found\n".getBytes(StandardCharsets.UTF_8));
        verify(mockClientChannel).write(expectedErrorBuffer);
    }
}
