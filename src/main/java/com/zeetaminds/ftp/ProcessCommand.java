//package com.zeetaminds.ftp;
//
//import java.io.*;
//import java.net.*;
//import java.util.Objects;
//import java.util.logging.Logger;
//
//import com.zeetaminds.client.ReceiveFiles;
//import com.zeetaminds.client.SendFiles;
//
//public class ProcessCommand {
//    static final Logger LOG = Logger.getLogger(ProcessCommand.class.getName());
//
//    public Command processCommand(String[] tokens, PrintWriter writer, Socket socket) {
//
//        String operation = tokens[0].toUpperCase();
//
//        if (Objects.equals(operation, "LIST")) {
//            return new ListFiles(writer);
//        } else if (Objects.equals(operation, "GET")) {
//            if (tokens.length > 1) {
//                return new SendFiles(tokens[1], socket);
//            } else {
//                writer.println("Syntax error in parameters or arguments.");
//            }
//
//        } else if (Objects.equals(operation, "PUT")) {
//            if (tokens.length > 1) {
//                System.out.println("entering put command");
//                return new ReceiveFiles(tokens[1], socket);
//
//            } else {
//                writer.println("Syntax error in parameters or arguments.");
//            }
//        } else {
//            LOG.info("unknown command"+operation);
//        }
//        return null;
//    }
//}
