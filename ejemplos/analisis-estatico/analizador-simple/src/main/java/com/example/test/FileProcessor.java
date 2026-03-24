package com.example.test;

import java.io.*;

public class FileProcessor {

    // This method has an issue - FileInputStream not in try-with-resources
    public String readFileIncorrectly(String path) throws IOException {
        try(FileInputStream fis = new FileInputStream(path)) {
        	
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }
        
        reader.close();
        fis.close();
        return content.toString();
        }
    }
    
    // This method is correct - uses try-with-resources
    public String readFileCorrectly(String path) throws IOException {
        StringBuilder content = new StringBuilder();
        
        try (FileInputStream fis = new FileInputStream(path);
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        
        return content.toString();
    }
    
    // Mixed approach - some resources managed correctly, others not
    public void processFileMixed(String inputPath, String outputPath) throws IOException {
        FileInputStream fis = new FileInputStream(inputPath);
        
        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            int data;
            while ((data = fis.read()) != -1) {
                fos.write(data);
            }
        }
        
        fis.close(); // Manual close instead of using try-with-resources
    }
}