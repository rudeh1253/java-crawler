package com.nsl.web.data;

import java.util.LinkedList;
import java.util.List;

/**
 * Container which can contain data.
 * Containers of each type of data should extend this class.
 * 
 * @author PGD
 */
public abstract class DataContainer<D> {
    /**
     * Protected field that represents the full data.
     */
    protected final List<Buffer<D>> data;

    /**
     * Default constructor.
     */
    public DataContainer() {
        this.data = new LinkedList<>();
    }
    
    /**
     * Add a buffer of data.
     * The type of data will be determined by generic type.
     * @param buffer should contain a buffer of data and its length.
     */
    public void addBuffer(Buffer<D> buffer) {
        this.data.add(buffer);
    }

    /**
     * Return data stored in this instance.
     * @return a list of buffers of data.
     */
    public List<Buffer<D>> getData() {
        return this.data;
    }
    
    @Override
    public String toString() {
        return this.data.toString();
    }
}