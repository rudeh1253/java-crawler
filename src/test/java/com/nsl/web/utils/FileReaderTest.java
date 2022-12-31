package com.nsl.web.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class FileReaderTest {
    
    @Test
    void testReadAsString() {
	final String path = "C:\\Users\\rudeh\\testcases\\sample.txt";
	String actual = null;
	try {
	    FileReader reader = new FileReader(path);
	    actual = reader.readAsString();
	} catch (IOException e) {
	    e.printStackTrace();
	    assertTrue(false);
	}
	
	final String expected = "November, December";
	assertEquals(expected, actual);
	
	final String path2 = "C:\\Users\\rudeh\\testcases\\sample2.txt";
	String actual2 = null;
	try {
	    FileReader reader = new FileReader(path2);
	    actual2 = reader.readAsString();
	} catch (IOException e) {
	    e.printStackTrace();
	    assertTrue(false);
	}
	
	final String expected2 = "";
	assertEquals(expected2, actual2);
	
	final String path3 = "C:\\Users\\rudeh\\testcases\\sample3.txt";
	String actual3 = null;
	try {
	    FileReader reader = new FileReader(path3);
	    actual3 = reader.readAsString();
	} catch (IOException e) {
	    e.printStackTrace();
	    assertTrue(false);
	}
	
	final String expected3 = "asdf";
	assertEquals(expected3, actual3);
    }
}