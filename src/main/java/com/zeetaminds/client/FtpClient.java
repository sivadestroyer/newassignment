package com.zeetaminds.client;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
public class FtpClient {
    static Logger logger = Logger.getLogger(FtpClient.class.getName());
    void receiveFiles(String fileName,Socket socket){
        byte[] bytes =new byte[1024];
        try{


            // Construct the file path using the current directory
            String filePath = "/home/sivabala/IdeaProjects/newassignment/src/main/java/com/zeetaminds/client/" + fileName;
        InputStream is = socket.getInputStream();
        DataInputStream dis = new DataInputStream(is);
        FileOutputStream fos = new FileOutputStream(filePath);
        int read;
        int totalBytes=0;
        long filesize=dis.readLong();
        while(totalBytes<filesize && (read=dis.read(bytes))>0){
            fos.write(bytes,0,read);
            totalBytes+=read;
        }
        fos.flush();
    }catch(IOException e){
        logger.log(Level.SEVERE,"Error during file transfer",e);
        }
    }
    void sendFiles(String fileName, Socket socket) {
        String filePath = "/home/sivabala/IdeaProjects/newassignment/src/main/java/com/zeetaminds/client/" + fileName;
        File file = new File(filePath);

        if (file.exists() && file.isFile()) {
            try {
                byte[] fileBytes = Files.readAllBytes(file.toPath()); // Use file.toPath()
                OutputStream os = socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);
                dos.writeLong(fileBytes.length); // Send file size first
                dos.write(fileBytes); // Send file data
                dos.flush();
                System.out.println("File sent successfully");
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error during file transfer", e);
            }
        } else {
            System.out.println("File not found: " + filePath);
        }
    }

    public static void main(String[] a){
        try{
            FtpClient client = new FtpClient();
            Socket socket = new Socket("localhost",9806);
            BufferedReader in =new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out =new PrintWriter(socket.getOutputStream(),true);
            BufferedReader reader =new BufferedReader(new InputStreamReader(socket.getInputStream()));
            logger.info("connected to ftp server");
            while(true){
                String command =in.readLine();
                out.println(command);
                String[] tokens = command.split(" ");
                String operation = tokens[0].toUpperCase();
                switch(operation){
                    case "LIST":
                        while (!(command = reader.readLine()).equals("END_OF_LIST")) {
                            System.out.println(command);
                        }
                        break;
                    case "GET":
                        if(tokens.length > 1){
                            client.receiveFiles(tokens[1],socket);
                        }else{
                            logger.info("filename is missing");
                        }
                        break;
                    case "PUT":
                        if(tokens.length > 1){
                            client.sendFiles(tokens[1],socket);
                        }else{
                            logger.info("filename not given");
                        }
                        break;
                    default:
                        logger.info("unknown command");
                }
            }
        }catch (IOException e){
            logger.info(e.getMessage());
        }
    }
}
