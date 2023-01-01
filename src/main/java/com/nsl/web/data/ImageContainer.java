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
public class ImageContainer {
    private final List<BufferElement> imageData;
    
    public ImageContainer() {
	imageData = new LinkedList<>();
    }
    
    /**
     * Store the buffer data in this image data.
     * 
     * @param buffer should contain a buffer of image data
     *               and its length.
     */
    public void addBuffer(BufferElement buffer) {
	imageData.add(buffer);
    }
    
    /**
     * Write a file containing the image data.
     * 
     * @param path where to write the file. Folders within the specified path should
     *             exist.
     * @throws IOException in the cases of some errors occur in the process to write a file. 
     */
    public void writeAsFile(String path) throws IOException {
	File file = new File(path);
	writeAsFile(file);
    }
    
    /**
     * Write a file containing the image data.
     * 
     * @param file object specifying information of a file to write.
     * @throws IOException in the cases of some errors occur in the process to write a file.
     */
    public void writeAsFile(File file) throws IOException {
	BufferedOutputStream bw = null;
	try {
	    bw = new BufferedOutputStream(new FileOutputStream(file));
	    writeAsFile(bw);
	} finally {
	    if (bw != null) {
		bw.close();
	    }
	}
    }
    
    /**
     * Write a file containing the image data.
     * 
     * @param os file output stream.
     * @throws IOException in the cases of some errors occur in the process to write a file.
     */
    public void writeAsFile(OutputStream os) throws IOException {
	for (BufferElement buffer : this.imageData) {
	    os.write(buffer.buffer, 0, buffer.len);
	}
    }
    
    /**
     * For dealing with multiple data type as a type.
     * It is similar to structure in C.
     * It contains image data of each buffer.
     */
    public static class BufferElement {
	final byte[] buffer;
	final int len;
	
	public BufferElement(byte[] buffer, int len) {
	    this.buffer = buffer;
	    this.len = len;
	}
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (BufferElement bufferElem : this.imageData) {
            byte[] buffer = bufferElem.buffer;
            int len = bufferElem.len;
            appendBufferToStringBuilder(buffer, len, sb);
        }
        return super.toString();
    }
    
    private void appendBufferToStringBuilder(byte[] buffer, int len, StringBuilder sb) {
	for (int i = 0; i < len; i++) {
	    sb.append(buffer[i]);
	}
    }
}