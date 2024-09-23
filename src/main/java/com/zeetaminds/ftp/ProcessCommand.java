package com.zeetaminds.ftp;

import java.io.*;
import java.net.*;
import java.util.logging.Logger;

import com.zeetaminds.client.ReceiveFiles;
import com.zeetaminds.client.SendFiles;

public class ProcessCommand {
    static final Logger LOG = Logger.getLogger(ProcessCommand.class.getName());

    public void processCommand(String command, PrintWriter writer, Socket socket) {

        if (command.isEmpty()) {
            return;
        }
        String[] tokens = command.split(" ");
        String operation = tokens[0].toUpperCase();

        switch (operation) {
            case "LIST":
                ListFiles add = new ListFiles();
                add.listFiles(writer);
                writer.flush();
                break;
            case "GET":
                if (tokens.length > 1) {
                    SendFiles obj = new SendFiles(tokens[1], socket);
                    obj.sendFiles();
                } else {
                    LOG.info("filename is missing");
                }
                break;
            case "PUT":
                if (tokens.length > 1) {
                    ReceiveFiles obj = new ReceiveFiles(tokens[1], socket);
                    obj.receiveFiles();
                } else {
                    LOG.info("filename not given");
                }
                break;
            default:
                LOG.info("unknown command");
                writer.println("ERROR: Unknown command");
                writer.flush();
                break;
        }
    }
}
