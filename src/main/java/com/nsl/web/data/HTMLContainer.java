package com.nsl.web.data;

/**
 * A class containing HTML file.
 * Its inner state doesn't change such that this is guaranteed an
 * immutable type.  
 * 
 * @author PGD
 */
public final class HTMLContainer extends DataContainer<String> {
    
    /**
     * Default constructor.
     */
    public HTMLContainer() {
        super();
    }

    @Override
    public void addBuffer(Buffer<String> buffer) {
        this.data.add(buffer);
    }
    
    @Override
    public String toString() {
        return super.toString();
    }
}