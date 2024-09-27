package com.zeetaminds.client;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReceiveFilesTest {

    @Test
    void handle() throws IOException {
        // Simulate the file size to be received
        String simulatedFileSize = "1024\n"; // 1 KB
        byte[] fileContent = new byte[1024]; // Mock file content of 1 KB
        ByteArrayInputStream inputStream = new ByteArrayInputStream((simulatedFileSize + new String(fileContent)).getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Create an instance of ReceiveFiles with the mock input and output streams
        ReceiveFiles receiveFiles = new ReceiveFiles("testfile.txt", inputStream, outputStream,25L);

        // Call the handle method to simulate file receiving
        receiveFiles.handle();

        // Verify the output
        String output = outputStream.toString();
        System.out.println(output);
        assertEquals("you are entering more than 1024 characters", output.trim());
    }
}
