package com.zeetaminds.ftp;

import com.zeetaminds.client.ReceiveFiles;
import com.zeetaminds.client.SendFiles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ParserTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void processCommand() throws IOException {
        Socket mockSocket = mock(Socket.class);
        OutputStream mockOutputStream = new ByteArrayOutputStream();
        Parser parser = new Parser(mockSocket);
        InputStream mockInputStream = new ByteArrayInputStream("LIST\n".getBytes(StandardCharsets.UTF_8));
        // Test LIST command
        Command listCommand = parser.processCommand(new String[]{"LIST"}, mockOutputStream, mockInputStream);
        assertInstanceOf(ListFiles.class, listCommand);

        // Test GET command with filename
        Command getCommand = parser.processCommand(new String[]{"GET", "testfile.txt"}, mockOutputStream, mockInputStream);
        assertInstanceOf(SendFiles.class, getCommand);

        // Test PUT command with filename
        Command putCommand = parser.processCommand(new String[]{"PUT", "newfile.txt"}, mockOutputStream, mockInputStream);
        assertInstanceOf(ReceiveFiles.class, putCommand);

        // Test invalid command
        Command invalidCommand = parser.processCommand(new String[]{"INVALID"}, mockOutputStream, mockInputStream);
        assertInstanceOf(HandleError.class, invalidCommand);
    }

    @Test
    void run() throws IOException {
        Socket mockSocket = mock(Socket.class);

        InputStream mockInputStream = new ByteArrayInputStream("LIST\n".getBytes(StandardCharsets.UTF_8));
        OutputStream mockOutputStream = new ByteArrayOutputStream();

        when(mockSocket.getInputStream()).thenReturn(mockInputStream);
        when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);

        Parser parser = new Parser(mockSocket);
        parser.run();
        String output = mockOutputStream.toString();
        assertNotNull(output); // Verify some output was generated
        assertFalse(output.isEmpty()); // Verify that output is not empty


    }
}