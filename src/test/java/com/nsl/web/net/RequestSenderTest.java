package com.nsl.web.net;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.nsl.web.data.DataContainer;
import com.nsl.web.data.HTMLContainer;
import com.nsl.web.net.HttpsRequestHTML;
import com.nsl.web.utils.FileReader;

/**
 * A test case for com.nsl.crawler.request.RequestSender
 * 
 * @author PGD
 */
public class RequestSenderTest {

    @Test
    void testRequest() {
	DataContainer actual = null;
	String url = "https://www.google.com/";
	try {
	    HttpsRequestHTML requestSender = new HttpsRequestHTML(url);
	    actual = requestSender.request();
	} catch (IOException e) {
	    e.printStackTrace();
	    assertTrue(false);
	}
	HTMLContainer expected = null;
	try {
	    FileReader reader = new FileReader("C:\\Users\\rudeh\\testcases\\Google.html");
	    String content = reader.readAsString();
	    expected = new HTMLContainer(content);
	} catch (IOException e) {
	    e.printStackTrace();
	    assertTrue(false);
	}
	assertEquals(expected, actual);
    }
}