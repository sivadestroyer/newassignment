package com.zeetaminds.bloomfilter;

import java.util.Random;
import java.security.NoSuchAlgorithmException;
public class RandomString {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    public static String generateRandomWord(int length) {
        Random random = new Random();
        StringBuilder word = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            word.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return word.toString();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        BloomFilter bloomFilter = new BloomFilter(1000000, 3);
        // Assume dictionary is loaded...
        DictionaryLoader.loadDictionary(bloomFilter, "/usr/share/dict/words");
        for (int i = 0; i < 1000; i++) {
            String randomWord = generateRandomWord(5);
            if (bloomFilter.contains(randomWord)) {
                System.out.println(randomWord + " might be a dictionary word (false positive check needed)");
            }

        }
    }
}
