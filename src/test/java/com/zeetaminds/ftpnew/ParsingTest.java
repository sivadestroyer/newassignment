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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParsingTest {

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
        Command command = parsing.parsingMethod(in, out);

        // Validate that we received the correct command object
        assertTrue(command instanceof ListFiles);
    }

    @Test
    public void testGetCommandWithValidFile() throws IOException {
        String inputCommand = "GET myfile.txt\n";
        InputStream in = new ByteArrayInputStream(inputCommand.getBytes(StandardCharsets.UTF_8));
        OutputStream out = new ByteArrayOutputStream();

        // Execute parsing
        Command command = parsing.parsingMethod(in, out);

        // Validate that the correct command is invoked (SendFiles)
        assertTrue(command instanceof SendFiles);
    }

    @Test
    public void testGetCommandWithoutFile() throws IOException {
        String inputCommand = "GET\n";
        InputStream in = new ByteArrayInputStream(inputCommand.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // Execute parsing
        Command command = parsing.parsingMethod(in, out);
        // Validate that the error handler is invoked
        assertInstanceOf(HandleError.class, command);
        command.handle();
        // Check the error message
        String errorMessage = new String(out.toByteArray(), StandardCharsets.UTF_8);
        System.out.println(errorMessage);
        assertEquals("Syntax error in parameters or arguments.\n", errorMessage);
    }

    @Test
    public void testPutCommandWithValidFile() throws IOException {
        String inputCommand = "PUT myfile.txt 25\n";
        InputStream in = new ByteArrayInputStream(inputCommand.getBytes(StandardCharsets.UTF_8));
        OutputStream out = new ByteArrayOutputStream();

        // Execute parsing
        Command command = parsing.parsingMethod(in, out);

        // Validate that the correct command is invoked (ReceiveFiles)
        assertTrue(command instanceof ReceiveFiles);
    }

    @Test
    public void testPutCommandWithoutFile() throws IOException {
        String inputCommand = "PUT\n";
        InputStream in = new ByteArrayInputStream(inputCommand.getBytes(StandardCharsets.UTF_8));
        OutputStream out = new ByteArrayOutputStream();

        // Execute parsing
        Command command = parsing.parsingMethod(in, out);
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
        Command command = parsing.parsingMethod(in, out);

        // Validate that the error handler is invoked for unknown commands
        assertTrue(command instanceof HandleError);
        command.handle();
        // Check the error message for unknown command
        String errorMessage = out.toString();
        assertEquals("unknown command", errorMessage.trim());
    }
    @Test
    public void testPutCommandWithText() throws IOException {
        String inputCommand="PUT kumar.txt 4\nhaii\n";
        InputStream in =new ByteArrayInputStream(inputCommand.getBytes(StandardCharsets.UTF_8));
        OutputStream out = new ByteArrayOutputStream();
        Command command= parsing.parsingMethod(in,out);
        command.handle();
        File fileToCheck = new File("kumar.txt");

        // Read the content of the file
        StringBuilder fileContent = new StringBuilder();
        try (Scanner scanner = new Scanner(fileToCheck)) {
            while (scanner.hasNextLine()) {
                fileContent.append(scanner.nextLine()).append("\n"); // Append each line
            }
        }

        // Check that the content matches what was supposed to be written
        String expectedContent = "haii\n"; // Make sure this matches what you expect
        assertEquals(expectedContent, fileContent.toString());

        // Clean up: delete the test file if needed

    }
    @Test
    public void testPipedCommands() throws IOException {
        String inputCommand = "LIST\nLIST\nput sivatest.txt 2\nhalist\n"; // Simulating piped commands
        InputStream in = new ByteArrayInputStream(inputCommand.getBytes(StandardCharsets.UTF_8));
        OutputStream out = new ByteArrayOutputStream();

        // Loop to simulate continuous parsing of multiple commands in a stream
        while (in.available() > 0) {
            // Execute parsing for each command
            Command command = parsing.parsingMethod(in, out);
            command.handle(); // Handle the parsed command

            // Check the output for each command (you can adapt this to your expected output)
            String commandOutput = out.toString();
            System.out.println("Command Output: " + commandOutput);

            // Reset the output stream for the next command
            ((ByteArrayOutputStream) out).reset();
        }

        // Final assertions or validation if needed
        // For example, if you expect each LIST command to produce specific output
        // assertEquals("Expected output for LIST", actualOutput);
    }

}
