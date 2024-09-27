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


        // Check that the content matches what was supposed to be written
        String expectedContent = "haii\n"; // Make sure this matches what you expect
        assertEquals(expectedContent, fileContent("kumar.txt"));

        // Clean up: delete the test file if needed

    }
    @Test
    public void testPipedCommands() throws IOException {
        // Simulating piped commands
        String inputCommand = "LIST\nLIST\nPUT sivatest.txt 2\nhalist\n";

        // Set up input and output streams
        InputStream in = new ByteArrayInputStream(inputCommand.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream out = new ByteArrayOutputStream(); // to capture command output

        // Loop to simulate continuous parsing of multiple commands in a stream
        while (in.available() > 0) {
            // Execute parsing for each command
            Command command = parsing.parsingMethod(in, out);

            // Ensure that command is not null
            assertNotNull(command, "Command parsing returned null");

            // Handle the parsed command
            command.handle();

            // Get the output after handling each command
            String actualOutput = out.toString(StandardCharsets.UTF_8);

            // Assert the expected output and command type for each command
            if (command instanceof ListFiles) {
                // Example expected output for LIST command
                assertTrue(actualOutput.contains("testfile.txt"), "Expected 'testfile.txt' not found in output.");
                assertTrue(actualOutput.contains("sivatest.txt"), "Expected 'sivatest.txt' not found in output.");
            } else if (command instanceof ReceiveFiles) {
                // Check if the command is an instance of ReceiveFiles (for PUT command)
                assertTrue(true, "Expected ReceiveFiles command");

                // Example expected output for PUT command
                String expectedPutOutput = "ha\n";
                assertEquals(expectedPutOutput, fileContent("sivatest.txt"));
            } else if (command instanceof HandleError) {
                // Example expected output for invalid commands
                String expectedErrorOutput = "Syntax error in parameters or arguments.\n";
                assertEquals(expectedErrorOutput, actualOutput, "Invalid command handling output mismatch");
            }

            // Reset the output stream for the next command
            out.reset();
        }

        // Final assertions or validation if needed
    }

    private String fileContent(String fileName) throws FileNotFoundException {
        File fileToCheck = new File(fileName);
        // Read the content of the file
        StringBuilder fileContent = new StringBuilder();
        try (Scanner scanner = new Scanner(fileToCheck)) {
            while (scanner.hasNextLine()) {
                fileContent.append(scanner.nextLine()).append("\n"); // Append each line
            }
        }
        return fileContent.toString();
    }

}
