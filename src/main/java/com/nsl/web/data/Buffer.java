package com.nsl.web.data;

/**
 * This contains a buffer of data.
 */
public class Buffer<D> {
    final D buffer;
    final int length;

    public Buffer(D buffer, int length) {
        this.buffer = buffer;
        this.length = length;
    }

    public D getBuffer() {
        return this.buffer;
    }

    public int getLength() {
        return this.length;
    }
}
