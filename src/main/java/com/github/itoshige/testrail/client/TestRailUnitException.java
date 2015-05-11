/**
 * Copyright 2015 itoshige1017@gmail.com
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
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
