package com.zeetaminds.bloomfilter;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.io.BufferedReader;
import java.io.FileReader;
public class DictionaryLoader {

    public static void loadDictionary(BloomFilter bloomFilter, String filePath) throws NoSuchAlgorithmException {
            try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
                String word;
                while((word=reader.readLine()) != null){
                    bloomFilter.add(word.trim());
                }
            }catch(IOException e) {
                System.out.println(e.getMessage());
            }
    }
}
