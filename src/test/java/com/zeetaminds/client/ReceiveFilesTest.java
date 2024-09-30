package com.zeetaminds.client;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReceiveFilesTest {

    private Path tempFilePath;

    @BeforeEach
    void setUp() throws IOException {
        // Create a temporary file for testing
        tempFilePath = Files.createTempFile("receivedFile", ".txt");
    }

    @AfterEach
    void tearDown() throws IOException {
        // Delete the temporary file after each test
        Files.deleteIfExists(tempFilePath);
    }

    @Test
    void handle_ReceivesFileSuccessfully() throws IOException {
        // Simulate the content of the file to be sent
        String fileContent = "This is a test file content.";
        byte[] inputData = fileContent.getBytes(StandardCharsets.UTF_8);

        // Create an InputStream to simulate receiving the file
        InputStream mockInputStream = new ByteArrayInputStream(inputData);

        // Create an OutputStream that points to the temporary file
        OutputStream mockOutputStream = new FileOutputStream(tempFilePath.toFile());

        // Create an instance of ReceiveFiles with the mocked input stream, output stream, and file length
        ReceiveFiles receiveFiles = new ReceiveFiles(tempFilePath.toString(), mockInputStream, mockOutputStream, (long) inputData.length);

        // Call the handle method
        receiveFiles.handle();

        // Verify that the file has been created and contains the correct content
        String actualContent = Files.readString(tempFilePath);
        assertEquals(fileContent, actualContent, "The received file content should match the expected content.");
    }

    @Test
    void handle_ReceivesPartialData() throws IOException {
        // Simulate partial file content
        String partialContent = "Partial content.";
        byte[] inputData = partialContent.getBytes(StandardCharsets.UTF_8);

        // Create an InputStream to simulate receiving the partial file
        InputStream mockInputStream = new ByteArrayInputStream(inputData);

        // Create an OutputStream that points to the temporary file
        OutputStream mockOutputStream = new FileOutputStream(tempFilePath.toFile());

        // Create an instance of ReceiveFiles with the mocked input stream, output stream, and file length
        ReceiveFiles receiveFiles = new ReceiveFiles(tempFilePath.toString(), mockInputStream, mockOutputStream, (long) inputData.length);

        // Call the handle method
        receiveFiles.handle();

        // Verify that the file has been created and contains the correct content
        String actualContent = Files.readString(tempFilePath);
        assertEquals(partialContent, actualContent, "The received file content should match the expected content.");
    }
}

