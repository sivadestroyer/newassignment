package com.zeetaminds.bloomfilter;
import java.util.BitSet;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BloomFilter {
    private BitSet bitarray;
    private int bitArraySize;
    private int numberHashfuctions;
    public BloomFilter(int bitArraySize,int numberHashfuctions){
        this.bitArraySize = bitArraySize;
        this.numberHashfuctions = numberHashfuctions;
        this.bitarray= new BitSet(bitArraySize);
    }
    private int[] getHashes(String word) throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest=md.digest(word.getBytes());
        int[] hashes = new int[numberHashfuctions];
        for(int i=0; i<numberHashfuctions; i++){
             int hash = 0;
             for(int j=0;j<4;j++){
                 hash=(hash<<8) | (digest[i*4+j] & 0xFF);
             }
             hashes[i]=Math.abs(hash)%bitArraySize;
        }
        return hashes;
    }
    public void add(String word) throws NoSuchAlgorithmException {
    int[] hashes = getHashes(word);
    for(int hash:hashes){
        bitarray.set(hash);
    }
    }
    public boolean contains(String word) throws NoSuchAlgorithmException{
        int[] hashes = getHashes(word);
        for(int hash:hashes){
            if(!bitarray.get(hash)){
                return false;
            }
        }
        return true;
    }
    public boolean spellchecker(String word) throws NoSuchAlgorithmException{
        return contains(word);
    }
    public static void experiment(int bitArraySize, int numberOfHashFunctions, String dictionaryPath) throws IOException, NoSuchAlgorithmException {
        BloomFilter bloomFilter = new BloomFilter(bitArraySize, numberOfHashFunctions);
        DictionaryLoader.loadDictionary(bloomFilter, dictionaryPath);
        System.out.println(bloomFilter.spellchecker("hello"));
    }
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        experiment(1000000, 3, "/usr/share/dict/words");
    }

}



