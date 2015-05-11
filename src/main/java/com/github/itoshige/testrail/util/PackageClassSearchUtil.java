/**
 * Copyright 2015 itoshige1017@gmail.com
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package com.github.itoshige.testrail.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.itoshige.testrail.annotation.Section;
import com.github.itoshige.testrail.client.TestInitializerException;

/**
 * target package class get
 * 
 * @author itoshige
 * 
 */
public class PackageClassSearchUtil {
    private static final Logger logger = LoggerFactory.getLogger(PackageClassSearchUtil.class);

    private static final String FILE_END = "Test.class";

    /** sectionStore */
    private final static ConcurrentHashMap<String, Set<String>> testClasses = CollectionUtil
        .newConcurrentMap();

    public static Set<String> getTestClasses(String runId) {
        Set<String> testClassNames = testClasses.get(runId);
        if (testClassNames != null && !testClassNames.isEmpty())
            return testClassNames;

        List<String> packages = ConfigrationUtil.getPackages(runId);

        for (String packageName : packages) {
            putTestClasses(runId, packageName);
        }

        if (testClasses.get(runId) == null || testClasses.get(runId).isEmpty())
            throw new TestInitializerException(String.format("package name is invalid. runId:%s", runId));

        return testClasses.get(runId);
    }

    private static void putTestClasses(String runId, String packageName) {
        logger.debug("target package:{}", packageName);

        Set<String> clazzes = getTestClassesByPackage(packageName);

        Set<String> putClasses = new HashSet<String>();
        for (String clazz : clazzes) {
            if (TestRailUnitUtil.isTestRailDebugEnabled())
                logger.debug("target class:{}", clazz);

            if (existTestClasses(clazz))
                continue;

            putClasses.add(clazz);
        }
        testClasses.put(runId, putClasses);
    }

    private static boolean existTestClasses(String clazz) {
        if (testClasses == null || testClasses.keySet() == null)
            return false;

        if (TestRailUnitUtil.isTestRailDebugEnabled())
            logger.debug("testClasses values:{} class:{} result:{}", new Object[] { testClasses.values(),
                clazz, testClasses.values().contains(clazz) });

        return testClasses.values().contains(clazz);
    }

    private static Set<String> getTestClassesByPackage(String packageName) {

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        JavaFileManager fm = compiler.getStandardFileManager(new DiagnosticCollector<JavaFileObject>(), null,
            null);

        Set<JavaFileObject.Kind> kind = new HashSet<JavaFileObject.Kind>() {
            private static final long serialVersionUID = 1L;
            {
                add(JavaFileObject.Kind.CLASS);
            }
        };

        Set<String> sectionNamesInTestRun = new HashSet<String>();
        try {
            for (JavaFileObject f : fm.list(StandardLocation.CLASS_PATH, packageName, kind, true)) {
                String fileName = f.toUri().toString();
                if (!fileName.endsWith(FILE_END))
                    continue;

                String fileNameWithPackage = getFileNameWithPackage(fileName, packageName);
                Class<?> clazz = Class.forName(fileNameWithPackage);

                if (TestRailUnitUtil.isSkipClass(clazz))
                    continue;

                Section sectionName = clazz.getAnnotation(Section.class);
                if (TestRailUnitUtil.hasSection(sectionName)) {
                    String trimSectionName = sectionName.name().trim();

                    sectionNamesInTestRun.add(trimSectionName);
                    continue;
                }

                sectionNamesInTestRun.add(clazz.getSimpleName());
            }

            return sectionNamesInTestRun;
        } catch (Exception e) {
            throw new TestInitializerException(String.format("couldn't get package classes. packageName:%s",
                packageName), e);
        }
    }

    private static String getFileNameWithPackage(String fileName, String packageName) {
        String filePackage = fileName.replace("/", ".");

        Pattern pattern = Pattern.compile(new StringBuilder(packageName).append(".*").toString());
        Matcher matcher = pattern.matcher(filePackage);
        if (matcher.find()) {
            String match = matcher.group(0);
            int last = match.lastIndexOf(".class");
            return match.substring(0, last);
        }
        throw new TestInitializerException(String.format(
            "couldn't find package class. fileName:%s  packageName:%s", fileName, packageName));
    }
}
