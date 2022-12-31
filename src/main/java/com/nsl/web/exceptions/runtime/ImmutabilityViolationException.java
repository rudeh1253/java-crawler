package com.nsl.web.exceptions.runtime;

public class ImmutabilityViolationException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ImmutabilityViolationException(String message) {
	super(message);
    }

}
