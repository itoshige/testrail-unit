package com.github.itoshige.testrail.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.Description;

import com.github.itoshige.testrail.annotation.IgnoreTestRail;
import com.github.itoshige.testrail.annotation.Section;

/**
 * TestRailUnit Util
 * 
 * 
 * @author itoshige
 * 
 */
public class TestRailUnitUtil {

    public static void checkParams(String runId) {
        // check same section name
        PackageClassSearchUtil.getTestClasses(runId);
    }

    public static boolean isSkipClass(Class<?> clazz) {
        IgnoreTestRail ignoreTestRailClass = clazz.getAnnotation(IgnoreTestRail.class);
        Ignore ignoreClass = clazz.getAnnotation(Ignore.class);
        return isSkip(ignoreTestRailClass, ignoreClass);
    }

    public static boolean isSkipMethod(Description desc) {
        IgnoreTestRail ignoreTestRailMethod = desc.getAnnotation(IgnoreTestRail.class);
        Ignore ignoreMethod = desc.getAnnotation(Ignore.class);
        return isSkip(ignoreTestRailMethod, ignoreMethod);
    }

    public static boolean isSkipMethod(Method method) {
        IgnoreTestRail ignoreTestRailMethod = method.getAnnotation(IgnoreTestRail.class);
        Ignore ignoreMethod = method.getAnnotation(Ignore.class);
        return isSkip(ignoreTestRailMethod, ignoreMethod);
    }

    private static boolean isSkip(IgnoreTestRail ignoreTestRail, Ignore ignore) {
        boolean hasIgnoreTestRail = hasIgnoreTestRail(ignoreTestRail);
        boolean hasIgnore = hasIgnore(ignore);
        return hasIgnoreTestRail || hasIgnore;
    }

    public static List<Method> getDeclaredTestMethods(Method[] methods) {
        List<Method> testMethods = new ArrayList<Method>();
        for (Method method : methods) {
            Test test = method.getAnnotation(Test.class);
            if (test != null && Test.class.equals(test.annotationType()))
                testMethods.add(method);
        }
        return testMethods;
    }

    private static boolean hasIgnoreTestRail(IgnoreTestRail annotation) {
        return annotation != null && IgnoreTestRail.class.equals(annotation.annotationType());
    }

    private static boolean hasIgnore(Ignore annotation) {
        return annotation != null && Ignore.class.equals(annotation.annotationType());
    }

    public static String getSectionName(Class<?> testClass) {
        Section definedSection = testClass.getAnnotation(Section.class);
        String sectionName = "";
        if (hasSection(definedSection)) {
            sectionName = definedSection.name().trim();
        } else {
            sectionName = testClass.getSimpleName();
        }
        return sectionName;
    }

    public static boolean hasSection(Section section) {
        return section != null && Section.class.equals(section.annotationType());
    }
}
