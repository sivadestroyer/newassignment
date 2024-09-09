package com.zeetaminds.io;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
public class FileCopyUsingBuffer {
       public static void main(String[] args){
           String Sourcefile="home/sivabala/Documents/sample.txt";
           String Destinationfile="home/sivabala/Documents/sample5_copy.txt";
           long startTime=System.currentTimeMillis();
           try(BufferedInputStream bis=new BufferedInputStream(new FileInputStream(Sourcefile));
            BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(Destinationfile)))
           {
               byte[] buffer = new byte[1024];
               int bytesRead;
               while((bytesRead=bis.read(buffer)) != -1){
                   bos.write(buffer,0,bytesRead);
               }

           } catch (IOException e){
               System.out.println(e);
           }
       }
}
