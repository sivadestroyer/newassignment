package com.zeetaminds.client;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SendFilesTest {

    @Test
    void handle() throws IOException {
        // Prepare a temporary file for testing
        Path tempFile = Files.createTempFile("testfile", ".txt");
        Files.write(tempFile, "Hello, World!".getBytes(), StandardOpenOption.WRITE);

        // Prepare the output stream to capture the sent data
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Create an instance of SendFiles
        SendFiles sendFiles = new SendFiles(tempFile.toString(), outputStream);

        // Call the handle method to simulate file sending
        sendFiles.handle();

        // Verify the output
        String output = outputStream.toString();
        long expectedSize = Files.size(tempFile);
        assertEquals("Hello, World!", output.trim());

        // Clean up the temporary file
        Files.deleteIfExists(tempFile);
    }
}
