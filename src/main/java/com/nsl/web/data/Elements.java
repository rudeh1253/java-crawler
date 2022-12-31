package com.nsl.web.data;

import java.util.Collection;
import java.util.List;

import org.jsoup.nodes.Element;

public class Elements extends org.jsoup.select.Elements {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public Elements() {
	super();
    }

    public Elements(int initialCapacity) {
	super(initialCapacity);
    }

    public Elements(Collection<Element> elements) {
	super(elements);
    }

    public Elements(List<Element> elements) {
	super(elements);
    }

    public Elements(Element... elements) {
	super(elements);
    }
}
