package com.zeetaminds.ftpnew;

import com.zeetaminds.client.ReceiveFiles;
import com.zeetaminds.client.SendFiles;
import com.zeetaminds.ftp.Command;
import com.zeetaminds.ftp.HandleError;
import com.zeetaminds.ftp.ListFiles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParsingTest {

    private static final long DELAY_MS = 10;
    private Parsing parsing;

    @BeforeEach
    public void setUp() {
        parsing = new Parsing();
    }

    @Test
    public void testListCommand() throws IOException {
        String inputCommand = "LIST\n";
        InputStream in = new ByteArrayInputStream(inputCommand.getBytes(StandardCharsets.UTF_8));
        OutputStream out = new ByteArrayOutputStream();

        // Execute parsing
        Command command = parsing.parse(in, out);

        // Validate that we received the correct command object
        assertTrue(command instanceof ListFiles);
    }

    @Test
    public void testGetCommandWithValidFile() throws IOException {
        String inputCommand = "GET myfile.txt\n";
        InputStream in = new ByteArrayInputStream(inputCommand.getBytes(StandardCharsets.UTF_8));
        OutputStream out = new ByteArrayOutputStream();

        // Execute parsing
        Command command = parsing.parse(in, out);

        // Validate that the correct command is invoked (SendFiles)
        assertTrue(command instanceof SendFiles);
    }

    @Test
    public void testGetCommandWithoutFile() throws IOException {
        String inputCommand = "GET\n";
        InputStream in = new ByteArrayInputStream(inputCommand.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // Execute parsing
        Command command = parsing.parse(in, out);
        // Validate that the error handler is invoked
        assertInstanceOf(HandleError.class, command);
        command.handle();
        // Check the error message
        String errorMessage = new String(out.toByteArray(), StandardCharsets.UTF_8);
        assertEquals("Syntax error in parameters or arguments.\n", errorMessage);
    }

    @Test
    public void testPutCommandWithValidFile() throws IOException {
        String inputCommand = "PUT myfile.txt 25\n";
        InputStream in = new ByteArrayInputStream(inputCommand.getBytes(StandardCharsets.UTF_8));
        OutputStream out = new ByteArrayOutputStream();

        // Execute parsing
        Command command = parsing.parse(in, out);

        // Validate that the correct command is invoked (ReceiveFiles)
        assertTrue(command instanceof ReceiveFiles);
    }

    @Test
    public void testPutCommandWithoutFile() throws IOException {
        String inputCommand = "PUT\n";
        InputStream in = new ByteArrayInputStream(inputCommand.getBytes(StandardCharsets.UTF_8));
        OutputStream out = new ByteArrayOutputStream();

        // Execute parsing
        Command command = parsing.parse(in, out);
        command.handle();
        // Validate that the error handler is invoked
        assertInstanceOf(HandleError.class, command);

        // Check the error message
        String errorMessage = out.toString();
        assertEquals("Syntax error in parameters or arguments.\n", errorMessage);
    }

    @Test
    public void testUnknownCommand() throws IOException {
        String inputCommand = "UNKNOWNCOMMAND\n";
        InputStream in = new ByteArrayInputStream(inputCommand.getBytes(StandardCharsets.UTF_8));
        OutputStream out = new ByteArrayOutputStream();
        // Execute parsing
        Command command = parsing.parse(in, out);

        // Validate that the error handler is invoked for unknown commands
        assertTrue(command instanceof HandleError);
        command.handle();
        // Check the error message for unknown command
        String errorMessage = out.toString();
        assertEquals("unknown command", errorMessage.trim());
    }


    @Test
    public void testPipedCommands() throws IOException {
        // Simulating piped commands
        String inputCommand = "LIST\nLIST\nPUT sivatest.txt 2\nhalist\n";

        // Set up input and output streams
        InputStream in = new ByteArrayInputStream(inputCommand.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream out = new ByteArrayOutputStream(); // to capture command output

        // Variables to keep track of command execution order
        int commandIndex = 0;
        String[] expectedCommands = {"LIST", "LIST", "PUT", "LIST"};

        // Loop to simulate continuous parsing of multiple commands in a stream
        while (in.available() > 0) {
            // Parse each command
            Command command = parsing.parse(in, out);

            // Ensure that the parsed command is not null
            assertNotNull(command, "Command parsing returned null");

            // Handle the parsed command
            command.handle();

            // Check if the correct command was executed in the right order
            if (commandIndex < expectedCommands.length) {
                if (command instanceof ListFiles) {
                    assertEquals("LIST", expectedCommands[commandIndex], "Expected LIST command");
                } else if (command instanceof ReceiveFiles) {
                    assertEquals("PUT", expectedCommands[commandIndex], "Expected PUT command");
                } else if (command instanceof HandleError) {
                    assertEquals("halist", expectedCommands[commandIndex], "Expected invalid command");
                }
            }

            // Increment the command index to check the next command in the pipeline
            commandIndex++;

            // Reset the output stream for the next command
            out.reset();
        }

        // Verify that all expected commands were processed
        assertEquals(expectedCommands.length, commandIndex, "Not all commands were executed.");
    }

    @Test
    public void testSendCommandWithDelay() throws IOException {
        // Command to test
        String inputCommand = "LIST\n"; // Change this as needed for other commands

        // Create output stream to capture the response
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);

        // Use the method to send the command with a delay
        sendCommandWithDelay(inputCommand, writer);

        // Now we need to simulate the parsing of the output
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

        // Parse the command
        Command command = parsing.parse(in, out);

        // Check if a command instance has been created
        assertNotNull(command, "Expected command instance to be created.");
        assertTrue(command instanceof ListFiles, "Expected command to be an instance of ListFiles.");

        // Optionally, validate the output for the LIST command
        String output = out.toString(StandardCharsets.UTF_8);
        assertFalse(output.isEmpty(), "Expected output for LIST command should not be empty.");
    }

    // Method to send command with delay
    private static void sendCommandWithDelay(String command, PrintWriter out) {
        char[] commandChars = command.toCharArray();
        for (char ch : commandChars) {
            out.print(ch); // Send one character at a time
            out.flush();
            try {
                // Introduce a delay between each character
                TimeUnit.MILLISECONDS.sleep(DELAY_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Send newline after the full command
        out.print("\n");
        out.flush();
    }




    // Final assertions or validation if needed
    }




