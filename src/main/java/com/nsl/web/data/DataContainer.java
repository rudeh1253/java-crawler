package com.nsl.web.data;

import java.util.LinkedList;
import java.util.List;

/**
 * Container which can contain data of different types.
 * Each container can contain only one object.
 * 
 * @author PGD
 */
public abstract class DataContainer<D> {
    protected final List<Buffer<D>> data;

    public DataContainer() {
        this.data = new LinkedList<>();
    }
    
    public abstract void addBuffer(Buffer<D> buffer);

    /**
     * Return data stored in this instance.
     * 
     * @return the data this container contains.
     */
    public List<Buffer<D>> getData() {
        return this.data;
    }
    
    @Override
    public String toString() {
        return this.data.toString();
    }
}