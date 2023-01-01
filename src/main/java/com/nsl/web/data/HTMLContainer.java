package com.nsl.web.data;

/**
 * A class containing HTML file.
 * Its inner state doesn't change such that this is guaranteed an
 * immutable type.  
 * 
 * @author PGD
 */
public final class HTMLContainer {
    /**
     * Full HTML content of String type.
     */
    private final String htmlContent;
    
    /**
     * Constructor.
     * 
     * @param htmlContent Content of HTML file provided.
     */
    public HTMLContainer(String htmlContent) {
        this.htmlContent = htmlContent;
    }
    
    @Override
    public boolean equals(Object obj) {
        return htmlContent.equals(obj);
    }
    
    @Override
    public int hashCode() {
        return htmlContent.hashCode();
    }
    
    @Override
    public String toString() {
        return this.htmlContent;
    }
}