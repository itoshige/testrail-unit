package com.github.itoshige.testrail;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.junit.ClassRule;
import org.junit.Rule;

import com.github.itoshige.testrail.rules.TestRailStore;
import com.github.itoshige.testrail.rules.TestRailUnit;

/**
 * Junit Test base
 * 
 * @author itoshige
 * 
 */
public class TestBase {

    @ClassRule
    public static TestRailUnit tr = new TestRailUnit();
    @Rule
    public TestRailStore ts = new TestRailStore(tr);

    static {
        /** slf4j console output */
        System.setProperty("org.slf4j.simpleLogger.log.defaultLogLevel", "DEBUG");

        /** log4j console output */
        Logger rootLogger = Logger.getRootLogger();
        rootLogger.setLevel(Level.DEBUG);
        rootLogger.addAppender(new ConsoleAppender(new PatternLayout(
            "%d{MM-dd HH:mm:ss} [%.25t] [%-5p] (%c) %m%n")));

        Logger.getLogger("org.springframework.test").setLevel(Level.DEBUG);
    };
}
