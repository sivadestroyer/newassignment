package com.zeetaminds.newnio;
import com.zeetaminds.newnio.exceptions.InvalidComandException;
import com.zeetaminds.ftp.Command;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class Parser {

    private static final Logger LOG = Logger.getLogger(Parser.class.getName());
    private static final int BUFFER_SIZE = 1024;
    String message = "Syntax error in parameters or arguments.\n";
    int bytesRead=0;

    private Command processCommand(String[] tokens, SocketChannel clientChannel,SessionState sessionState) throws IOException {
        String operation = tokens[0].toUpperCase();

        switch (operation) {
            case "LIST":
                return new ListFiles(clientChannel);
            case "GET":
                return tokens.length > 1 ? new SendFiles(tokens[1], clientChannel)
                        : new HandleError(clientChannel, message);
            case "PUT":
                return tokens.length > 2 ? new ReceiveFiles(tokens[1], clientChannel, Long.parseLong(tokens[2]),sessionState)
                        : new HandleError(clientChannel, message);
            default:
                return new HandleError(clientChannel, "unknown command");
        }
    }

    public Command parse(ByteBuffer buffer, SocketChannel clientChannel, SessionState sessionState)
            throws IOException, InvalidComandException {
        StringBuilder commandLine = new StringBuilder();
        while(buffer.remaining()>0){
            byte b=buffer.get();
            commandLine.append((char)b);
            if (b=='\n'){
                String fullCommand =commandLine.toString().trim();
                String[] tokens = fullCommand.split(" ");
                if(buffer.limit()==buffer.position()){
                    buffer.clear();
                    sessionState.isReadable=false;

                }
                commandLine.setLength(0);
                return processCommand(tokens,clientChannel,sessionState);
            }
            if(buffer.limit()==buffer.position()){
                buffer.clear();
            }
            if(commandLine.length()>=BUFFER_SIZE){
               throw new InvalidComandException("command too long");
            }
        }
        return new HandleError(clientChannel, "");
    }
}
