package com.nsl.web.data;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * A class containing image data.
 * 
 * @author PGD
 */
public class BinaryContainer extends DataContainer<byte[]> {
    
    public BinaryContainer() {
        super();
    }
    
    /**
     * Store the buffer data in this image data.
     * 
     * @param buffer should contain a buffer of image data
     *               and its length.
     */
    public void addBuffer(Buffer<byte[]> buffer) {
        this.data.add(buffer);
    }
    
    @Override
    public String toString() {
        return super.toString();
    }
}