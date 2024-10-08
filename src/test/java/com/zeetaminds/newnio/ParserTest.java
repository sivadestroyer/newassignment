package com.zeetaminds.newnio;

import com.zeetaminds.ftp.Command;
import com.zeetaminds.newnio.exceptions.InvalidComandException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ParserTest {

    private Parser parser;
    private SocketChannel mockChannel;
    private ByteBuffer buffer;
    private SessionState sessionState;

    @BeforeEach
    public void setup() {
        parser = new Parser();
        mockChannel = mock(SocketChannel.class);
        buffer = ByteBuffer.allocate(1024);

        sessionState = new SessionState();
    }

    @Test
    public void testListCommand() throws IOException, InvalidComandException {
        // Prepare mock data

        String command = "LIST\n";
        buffer.put(command.getBytes());
        buffer.flip();
        parser.bytesRead=command.length();

        when(mockChannel.read(any(ByteBuffer.class))).thenReturn(command.length());

        // Parse and assert
        Command result = parser.parse(buffer, mockChannel, sessionState);
        assertTrue(result instanceof ListFiles, "Expected ListFiles command");
    }

    @Test
    public void testGetCommand() throws IOException, InvalidComandException {
        // Prepare mock data
        String command = "GET file1.txt\n";
        buffer.put(command.getBytes());
        buffer.flip();
        parser.bytesRead=command.length();

        when(mockChannel.read(any(ByteBuffer.class))).thenReturn(command.length());

        // Parse and assert
        Command result = parser.parse(buffer, mockChannel, sessionState);
        assertTrue(result instanceof SendFiles, "Expected SendFiles command");
    }

    @Test
    public void testPutCommand() throws IOException, InvalidComandException {
        // Prepare mock data
        String command = "PUT file2.txt 1024\n";
        buffer.put(command.getBytes());
        buffer.flip();
        parser.bytesRead=command.length();

        when(mockChannel.read(any(ByteBuffer.class))).thenReturn(command.length());

        // Parse and assert
        Command result = parser.parse(buffer, mockChannel, sessionState);
        assertTrue(result instanceof ReceiveFiles, "Expected ReceiveFiles command");
    }

    @Test
    public void testInvalidCommand() throws IOException, InvalidComandException {
        // Prepare mock data
        String command = "INVALID\n";
        buffer.put(command.getBytes());
        buffer.flip();
        parser.bytesRead=command.length();

        when(mockChannel.read(any(ByteBuffer.class))).thenReturn(command.length());

        // Parse and assert
        Command result = parser.parse(buffer, mockChannel, sessionState);
        assertTrue(result instanceof HandleError, "Expected HandleError for unknown command");
    }
    @Test
    public void testPipedCommands() throws IOException, InvalidComandException {
        // Prepare mock data with piped commands
        String commands = "LIST\nPUT file2.txt 1024\n";
        buffer.put(commands.getBytes(StandardCharsets.UTF_8));
        buffer.flip(); // Prepare the buffer for reading
        parser.bytesRead=commands.length();
        // Mock SocketChannel to return the total length of the piped commands
        when(mockChannel.read(any(ByteBuffer.class))).thenReturn(commands.length());
        Command result1 = parser.parse(buffer, mockChannel, sessionState);
        assertTrue(result1 instanceof ListFiles, "Expected ListFiles command");
        Command result2 = parser.parse(buffer, mockChannel, sessionState);
        assertTrue(result2 instanceof ReceiveFiles, "Expected ReceiveFiles command");

    }


}
