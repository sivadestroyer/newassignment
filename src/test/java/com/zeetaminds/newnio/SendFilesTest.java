package com.zeetaminds.newnio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SendFilesTest {

    private SocketChannel mockChannel;
    private SendFiles sendFiles;
    private File mockFile;

    @BeforeEach
    public void setup() {
        mockChannel = mock(SocketChannel.class);
        sendFiles = spy(new SendFiles("testfile.txt", mockChannel)); // Spy to mock protected methods

        // Mocking file for existence check
        mockFile = mock(File.class);
    }

    @Test
    public void testSendFiles_FileExists() throws IOException {
        // Mock the file to exist and have content
        when(mockFile.exists()).thenReturn(true);
        when(mockFile.isFile()).thenReturn(true);
        when(mockFile.length()).thenReturn(12L); // Mock file size

        // Mock FileInputStream and FileChannel for reading the file
        FileInputStream mockInputStream = mock(FileInputStream.class);
        FileChannel mockFileChannel = mock(FileChannel.class);
        when(mockInputStream.getChannel()).thenReturn(mockFileChannel);
        when(mockFileChannel.transferTo(anyLong(), anyLong(), any(SocketChannel.class))).thenReturn(12L);

        // Mock the getFile and getFileInputStream methods to return the mock file and stream
        doReturn(mockFile).when(sendFiles).getFile("testfile.txt");
        doReturn(mockInputStream).when(sendFiles).getFileInputStream(mockFile);

        // Execute the handle method
        sendFiles.handle();

        // Verify the file size was sent first
        ArgumentCaptor<ByteBuffer> bufferCaptor = ArgumentCaptor.forClass(ByteBuffer.class);
        verify(mockChannel, times(2)).write(bufferCaptor.capture());
        ByteBuffer writtenBuffer = bufferCaptor.getAllValues().get(0);
        writtenBuffer.flip(); // Prepare the buffer for reading
        byte[] sizeBytes = new byte[writtenBuffer.remaining()];
        writtenBuffer.get(sizeBytes);
        assertEquals("12\n", new String(sizeBytes, StandardCharsets.UTF_8));

        // Verify that the file was transferred over the channel
        verify(mockFileChannel, times(1)).transferTo(0, 12, mockChannel);
    }

    @Test
    public void testSendFiles_FileNotFound() throws IOException {
        // Mock the file to not exist
        when(mockFile.exists()).thenReturn(false);
        when(mockFile.isFile()).thenReturn(false);

        // Mock the getFile method to return the mock file
        doReturn(mockFile).when(sendFiles).getFile("testfile.txt");

        // Execute the handle method
        sendFiles.handle();

        // Verify that the error message was sent
        ArgumentCaptor<ByteBuffer> bufferCaptor = ArgumentCaptor.forClass(ByteBuffer.class);
        verify(mockChannel, times(1)).write(bufferCaptor.capture());
        ByteBuffer writtenBuffer = bufferCaptor.getValue();
        writtenBuffer.flip(); // Prepare the buffer for reading
        byte[] errorBytes = new byte[writtenBuffer.remaining()];
        writtenBuffer.get(errorBytes);
        assertEquals("ERROR: File not found\n", new String(errorBytes, StandardCharsets.UTF_8));
    }
}
