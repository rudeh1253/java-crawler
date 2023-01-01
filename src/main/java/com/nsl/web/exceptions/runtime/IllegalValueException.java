package com.nsl.web.exceptions.runtime;

/**
 * A runtime exception thrown if given values violate a specification.
 * 
 * @author PGD
 */
public class IllegalValueException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public IllegalValueException(String message) {
	super(message);
    }

    public IllegalValueException(Throwable cause) {
	super(cause);
    }

    public IllegalValueException(String message, Throwable cause) {
	super(message, cause);
    }

    public IllegalValueException(String message, Throwable cause, boolean enableSuppression,
	    boolean writableStackTrace) {
	super(message, cause, enableSuppression, writableStackTrace);
    }

}

