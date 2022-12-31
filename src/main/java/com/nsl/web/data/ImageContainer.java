package com.nsl.web.data;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

public class ImageContainer {
    private List<BufferElement> imageData;
    
    public ImageContainer() {
	imageData = new LinkedList<>();
    }
    
    /**
     * 
     * 
     * @param buffer
     */
    public void addBuffer(BufferElement buffer) {
	imageData.add(buffer);
    }
    
    public void writeAsFile(String path) throws IOException {
	File file = new File(path);
	writeAsFile(file);
    }
    
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
    
    public void writeAsFile(OutputStream os) throws IOException {
	for (BufferElement buffer : this.imageData) {
	    os.write(buffer.buffer, 0, buffer.len);
	}
    }
    
    /**
     * Construct class in order to deal with multiple data type as a type.
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