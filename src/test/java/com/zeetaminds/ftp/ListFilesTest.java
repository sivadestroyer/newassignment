package com.zeetaminds.ftp;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;

class ListFilesTest {

    @Test
    void handle() throws IOException {
        // Mock the output stream where the results will be written
        OutputStream mockOutputStream = mock(OutputStream.class);

        // Create an instance of ListFiles with the mocked output stream
        ListFiles listFiles = new ListFiles(mockOutputStream);

        // Mock an array of files to simulate the directory contents
        File[] mockFiles = new File[3];  // Simulating 3 files in the directory

        // Mock individual file objects with specific file names
        mockFiles[0] = mock(File.class);
        when(mockFiles[0].getName()).thenReturn("file1.txt");
        when(mockFiles[0].isFile()).thenReturn(true);

        mockFiles[1] = mock(File.class);
        when(mockFiles[1].getName()).thenReturn("file2.txt");
        when(mockFiles[1].isFile()).thenReturn(true);

        mockFiles[2] = mock(File.class);
        when(mockFiles[2].getName()).thenReturn("file3.txt");
        when(mockFiles[2].isFile()).thenReturn(true);

        // Mock the directory object to return the mock files
        File mockDirectory = mock(File.class);
        when(mockDirectory.listFiles()).thenReturn(mockFiles);

        // Now mock the directory listing functionality
        listFiles.setDirectory(mockDirectory); // Assuming you have a way to set the directory

        // Call the method that lists the files
        listFiles.handle();

        // Verify the correct number of files and names are written to the output stream
        verify(mockOutputStream).write("3\n".getBytes(StandardCharsets.UTF_8));  // Expecting 3 files

        // Verify that the file names are written correctly
        verify(mockOutputStream).write("file1.txt\n".getBytes(StandardCharsets.UTF_8));
        verify(mockOutputStream).write("file2.txt\n".getBytes(StandardCharsets.UTF_8));
        verify(mockOutputStream).write("file3.txt\n".getBytes(StandardCharsets.UTF_8));
    }
}
