package com.zeetaminds.client;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SendFilesTest {

    @Test
    void handle_SendsFileSuccessfully() throws IOException {
        // Create a temporary file for testing
        File tempFile = File.createTempFile("testfile", ".txt");
        String fileContent = "This is a test file.";
        Files.write(tempFile.toPath(), fileContent.getBytes());

        // Create a ByteArrayOutputStream to capture the output
        ByteArrayOutputStream mockOutputStream = new ByteArrayOutputStream();

        // Create an instance of SendFiles with the temporary file and mock output stream
        SendFiles sendFiles = new SendFiles(tempFile.getAbsolutePath(), mockOutputStream);

        // Call the handle method
        sendFiles.handle();

        // Verify the output
        String expectedOutput = tempFile.length() + "\n" + fileContent;
        String actualOutput = mockOutputStream.toString();

        // Check that the output contains the file size and the content
        assertTrue(actualOutput.startsWith(tempFile.length() + "\n"), "Output should start with file size.");
        assertTrue(actualOutput.endsWith(fileContent), "Output should end with file content.");

        // Clean up the temporary file
        tempFile.delete();
    }

    @Test
    void handle_FileNotFound() throws IOException {
        // Create a ByteArrayOutputStream to capture the output
        ByteArrayOutputStream mockOutputStream = new ByteArrayOutputStream();

        // Create an instance of SendFiles with a non-existent file
        SendFiles sendFiles = new SendFiles("non_existent_file.txt", mockOutputStream);

        // Call the handle method
        sendFiles.handle();

        // Verify that the output contains the error message
        String expectedError = "ERROR: File not found\n";
        String actualOutput = mockOutputStream.toString();

        assertEquals(expectedError, actualOutput, "Output should indicate that the file was not found.");
    }
}
