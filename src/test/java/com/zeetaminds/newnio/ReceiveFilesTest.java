package com.zeetaminds.newnio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReceiveFilesTest {

    private SocketChannel mockClientChannel;
    private SessionState mockSessionState;
    private ReceiveFiles receiveFiles;
    private String testFileName = "testFile.txt";
    private long fileLen = 1024; // Simulating a file length of 1024 bytes
    private FileOutputStream mockFileOutputStream;

    @BeforeEach
    public void setUp() throws IOException {
        // Mock dependencies
        mockClientChannel = mock(SocketChannel.class);
        mockSessionState = mock(SessionState.class);
        mockFileOutputStream = mock(FileOutputStream.class);

        // Create the ReceiveFiles instance with mocked dependencies
        receiveFiles = new ReceiveFiles(testFileName, mockClientChannel, fileLen, mockSessionState);
    }

    @Test
    public void testHandle_SuccessfulFileTransfer() throws IOException {
        // Given
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        when(mockSessionState.getBuffer()).thenReturn(buffer);

        // Simulate reading data from the SocketChannel
        byte[] data = "Sample data for testing.".getBytes();
        ByteBuffer inputBuffer = ByteBuffer.wrap(data);
        when(mockClientChannel.read(any(ByteBuffer.class))).thenAnswer(invocation -> {
            ByteBuffer buf = invocation.getArgument(0);
            int bytesToRead = Math.min(buf.remaining(), inputBuffer.remaining());
            buf.put(inputBuffer.array(), 0, bytesToRead);
            return bytesToRead; // Simulate number of bytes read
        });

        // When: handle is called
        receiveFiles.handle();

        // Then: Verify that data is written to the file
        // Assuming we don't have the actual FileOutputStream, we can verify interactions
        verify(mockFileOutputStream, times(data.length)).write(any(byte[].class), anyInt(), anyInt());
    }

    @Test
    public void testHandle_NoDataRead() throws IOException {
        // Given
        when(mockClientChannel.read(any(ByteBuffer.class))).thenReturn(-1); // Simulate end of stream

        // Set buffer and file length
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        when(mockSessionState.getBuffer()).thenReturn(buffer);

        // When: handle is called
        receiveFiles.handle();

        // Then: Ensure that no data is written when no bytes are read
        verify(mockFileOutputStream, never()).write(any(byte[].class), anyInt(), anyInt());
    }

    @Test
    public void testHandle_PartialDataRead() throws IOException {
        // Given
        when(mockClientChannel.read(any(ByteBuffer.class))).thenReturn(512); // Simulate reading 512 bytes
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        when(mockSessionState.getBuffer()).thenReturn(buffer);
        byte[] data = new byte[512];
        buffer.put(data); // Put data into the buffer
        buffer.flip();

        // When: handle is called
        receiveFiles.handle();

        // Then: Verify the correct amount of data is written
        verify(mockFileOutputStream, times(512)).write(any(byte[].class), anyInt(), anyInt());
    }
}
