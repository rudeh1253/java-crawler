package com.nsl.web.exceptions.runtime;

/**
 * A runtime exception thrown if trying mutate variables which are immutable data types.
 * 
 * @author PGD
 */
public class ImmutabilityViolationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ImmutabilityViolationException(String message) {
	super(message);
    }
}

