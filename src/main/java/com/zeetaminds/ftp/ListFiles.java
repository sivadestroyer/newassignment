
package com.zeetaminds.ftp;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ListFiles implements Command {
    private OutputStream outputStream;

    public ListFiles(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void handle() {
        File dir = new File(".");
        File[] files = dir.listFiles();

        try {
            if (files != null) {
                int numberOfFiles = 0;

                // First count the number of files
                for (File file : files) {
                    if (file.isFile()) {
                        numberOfFiles++;
                    }
                }

                // Write the number of files to the output stream
                outputStream.write((numberOfFiles + "\n").getBytes(StandardCharsets.UTF_8));

                // Then write the file names to the output stream
                for (File file : files) {
                    if (file.isFile()) {
                        outputStream.write((file.getName() + "\n").getBytes(StandardCharsets.UTF_8));
                    }
                }

            } else {
                // If no files, write 0 to the output stream
                outputStream.write("0\n".getBytes(StandardCharsets.UTF_8));
            }

            outputStream.flush(); // Ensure all data is written
            System.out.println("List sent successfully");

        } catch (IOException e) {
            System.err.println("Error writing file list to output stream: " + e.getMessage());
        }
    }
}
