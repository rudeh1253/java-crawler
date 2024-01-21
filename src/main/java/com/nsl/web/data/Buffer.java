package com.nsl.web.data;

/**
 * This contains a buffer of data.
 * 
 * @author PGD
 */
public class Buffer<D> {
    final D buffer;
    final int length;

    /**
     * A constructor.
     * @param buffer a buffer block of data.
     * @param length of the buffer.
     */
    public Buffer(D buffer, int length) {
        this.buffer = buffer;
        this.length = length;
    }

    /**
     * Return the buffer this object contains.
     * @return the buffer.
     */
    public D getBuffer() {
        return this.buffer;
    }

    /**
     * Return the length of the buffer this object contains.
     * @return the length of buffer.
     */
    public int getLength() {
        return this.length;
    }
}
