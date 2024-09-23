package com.zeetaminds.ftp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

import static org.mockito.Mockito.*;

class CommandHandlerTest {

    private Socket mockSocket;
    private BufferedReader mockReader;
    private PrintWriter mockWriter;
    private CommandHandler commandHandler;

    @BeforeEach
    void setUp() throws IOException {
        // Mocking Socket and its input/output streams
        mockSocket = mock(Socket.class);
        mockReader = mock(BufferedReader.class);
        mockWriter = mock(PrintWriter.class);

        when(mockSocket.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
        when(mockSocket.getOutputStream()).thenReturn(new ByteArrayOutputStream());

        // Using a constructor for CommandHandler that accepts the mock writer
        commandHandler = new CommandHandler(mockSocket);
    }

    @Test
    void testProcessFragmentedLISTCommand() throws IOException {
        // Mocking the input as fragmented LIST command
        when(mockReader.readLine())
                .thenReturn("L")   // First fragment
                .thenReturn("IS")  // Second fragment
                .thenReturn("T\n"); // Final fragment to complete the command

        // Mock the output stream
        when(mockSocket.getOutputStream()).thenReturn(new ByteArrayOutputStream());

        // Inject the reader into the command handler
        commandHandler = new CommandHandler(mockSocket);
        commandHandler.run();

        // Verify that processCommand was called with "LIST"
        ProcessCommand mockProcessCommand = mock(ProcessCommand.class);
        verify(mockProcessCommand, times(1)).processCommand("LIST", mockWriter, mockSocket);
    }

    @Test
    void testProcessFragmentedGETCommand() throws IOException {
        // Mocking the input as fragmented GET command
        String filename = "example.txt";
        when(mockReader.readLine())
                .thenReturn("G")   // First fragment
                .thenReturn("ET ") // Second fragment
                .thenReturn(filename + "\n"); // Complete command

        // Mock the output stream
        when(mockSocket.getOutputStream()).thenReturn(new ByteArrayOutputStream());

        // Inject the reader into the command handler
        commandHandler = new CommandHandler(mockSocket);
        commandHandler.run();

        // Verify that processCommand was called with "GET example.txt"
        ProcessCommand mockProcessCommand = mock(ProcessCommand.class);
        verify(mockProcessCommand, times(1)).processCommand("GET " + filename, mockWriter, mockSocket);
    }
}
