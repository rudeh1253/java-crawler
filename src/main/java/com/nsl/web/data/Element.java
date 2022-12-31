package com.nsl.web.data;

import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;

public final class Element extends org.jsoup.nodes.Element {

    public Element(String tag) {
	super(tag);
    }

    public Element(Tag tag, String baseUri) {
	super(tag, baseUri);
    }

    public Element(Tag tag, String baseUri, Attributes attributes) {
	super(tag, baseUri, attributes);
    }
}