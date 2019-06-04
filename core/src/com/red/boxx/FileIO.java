package com.red.boxx;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

class FileIO {
    static String read(String path){
        {
            byte[] encoded = new byte[0];
            try {
                encoded = Files.readAllBytes(Paths.get(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new String(encoded, StandardCharsets.UTF_8);
        }
    }
}