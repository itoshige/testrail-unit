/**
 * Copyright 2015 itoshige1017@gmail.com
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package com.github.itoshige.testrail.client;

/**
 * Initializer Exception
 * 
 * @author itoshige
 * 
 */
public class TestInitializerException extends RuntimeException {
    private static final long serialVersionUID = 1838277662353534750L;

    public TestInitializerException(String message) {
        super(message);
    }

    public TestInitializerException(String message, Throwable cause) {
        super(message, cause);
    }
}
