package com.nsl.web.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

/**
 * Support to read files.
 * 
 * @author PGD
 */
public class FileReader {
    /**
     * A file object.
     */
    private final File FILE;
    
    /**
     * Constructor.
     * 
     * @param path should represent the existing file name.
     *             Otherwise, it will behave unpredictably.
     * @throws IOException is thrown if there is a problem in the process of file stream
     */
    public FileReader(String path) throws IOException {
        this.FILE = new File(path);
    }
    
    /**
     * Constructor.
     * 
     * @param file should represent the existing file name.
     *             Otherwise, it will behave unpredictably.
     * @throws IOException is thrown if there is a problem in the process of file stream
     */
    public FileReader(File file) throws IOException {
        this.FILE = file;
    }
    
    /**
     * Read the file.
     * 
     * @return all of the content of the file.
     * @throws IOException is thrown if there is a problem in the process of file stream
     */
    public String readAsString() throws IOException {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(new java.io.FileReader(this.FILE));
            
            String line = "";
            while (true) {
                line = br.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line).append('\n');
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }
        
        String completedString = sb.toString();
        return completedString.stripTrailing();
    }
}
