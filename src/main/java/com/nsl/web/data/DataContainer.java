package com.nsl.web.data;

import com.nsl.web.exceptions.runtime.IllegalValueException;

/**
 * Container which can contain data of different types.
 * Each container can contain only one object.
 * 
 * @author PGD
 */
public class DataContainer {
    private HTMLContainer html;
    private ImageContainer image;
    
    /**
     * This is similar to a label attached on boxes.
     * It represents what type of data is contained in the
     * object.
     */
    private DataType type;
    
    /**
     * This guarantees immutability of the type (class).
     */
    private boolean alreadyDetermined;

    public DataContainer() {
	alreadyDetermined = false;
    }
    
    /**
     * Set data to the DataContainer instance.
     * Once you set data onto this container,
     * if you try putting in the container with
     * another data later, a runtime exception will be thrown.
     * 
     * @param data which you intend to store.
     * @param type of which you intend to store.
     * @throws IllegalValueException if the specified type is not included in
     *                               enum DataContainer.DataType
     *         ImmutabilityViolationException is thrown if you violate immutability
     *                                        of this type.
     */
    public void setData(Object data, DataType type) {
	if (alreadyDetermined) {
	    return;
	}
	switch (type) {
	case HTML:
	    this.html = (HTMLContainer)data;
	    break;
	case IMAGE:
	    this.image = (ImageContainer)data;
	    break;
	default:
	    throw new IllegalValueException("The specified DataType is not available");
	}
	this.type = type;
    }
    
    /**
     * Return data stored in this instance.
     * 
     * @param type of which you intend to get.
     * @return the data whose the type is what you specified.
     * @throws IllegalValueException if the specified type is not included in
     *                               enum DataContainer.DataType
     */
    public Object getData() {
	switch (this.type) {
	case HTML:
	    return html;
	case IMAGE:
	    return image;
	default:
	    throw new IllegalValueException("The specified DataType is not available");
	}
    }
    
    public static enum DataType {
	HTML, IMAGE
    }
    
    @Override
    public String toString() {
        switch (this.type) {
        case HTML:
            return this.html.toString();
        case IMAGE:
            return this.image.toString();
        default:
            return "";
        }
    }
}