package com.github.itoshige.testrail.client;

/**
 * testrail logic exception
 * 
 * @author itoshige
 * 
 */
public class TestRailUnitException extends RuntimeException {
    private static final long serialVersionUID = 2001119939653767170L;

    public TestRailUnitException(Throwable e) {
        super(e);
    }

    public TestRailUnitException(String message) {
        super(message);
    }
}