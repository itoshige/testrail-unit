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

import com.github.itoshige.testrail.annotation.Section;
import com.github.itoshige.testrail.client.TestInitializerException;

/**
 * target package class get
 * 
 * @author itoshige
 * 
 */
public class PackageClassSearchUtil {
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

        if (testClasses.get(runId).isEmpty())
            throw new TestInitializerException("package name is invalid.");

        return testClasses.get(runId);
    }

    private static void putTestClasses(String runId, String packageName) {
        Set<String> clazzes = getTestClassesByPackage(packageName);
        for (String clazz : clazzes) {
            if (testClasses.get(runId).contains(clazz))
                throw new TestInitializerException(new StringBuilder(
                    "junit test has same secion name. section:").append(clazz).toString());
        }
        testClasses.put(runId, clazzes);
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
                String fileName = f.getName();
                if (!fileName.endsWith(FILE_END))
                    continue;

                String fileNameWithPackage = getFileNameWithPackage(fileName, packageName);
                Class<?> clazz = Class.forName(fileNameWithPackage);

                if (TestRailUnitUtil.isSkipClass(clazz))
                    continue;

                Section sectionName = clazz.getAnnotation(Section.class);
                if (TestRailUnitUtil.hasSection(sectionName)) {
                    String trimSectionName = sectionName.name().trim();
                    if (sectionNamesInTestRun.contains(trimSectionName))
                        throw new TestInitializerException(new StringBuilder(
                            "junit test has same secion name. section:").append(trimSectionName).toString());

                    sectionNamesInTestRun.add(trimSectionName);
                    continue;
                }
                sectionNamesInTestRun.add(clazz.getSimpleName());
            }

            return sectionNamesInTestRun;
        } catch (Exception e) {
            throw new TestInitializerException(
                new StringBuilder("couldn't get package classes. packageName:").append(packageName)
                    .append(" Exception:").append(e).toString());
        }
    }

    private static String getFileNameWithPackage(String fileName, String packageName) {
        String filePackage = fileName.replace("\\", ".");

        Pattern pattern = Pattern.compile(new StringBuilder(packageName).append(".*").toString());
        Matcher matcher = pattern.matcher(filePackage);
        if (matcher.find()) {
            String match = matcher.group(0);
            int last = match.lastIndexOf(".class");
            return match.substring(0, last);
        }
        throw new TestInitializerException(new StringBuilder("couldn't find package class. fileName:")
            .append(fileName).append(" packageName:").append(packageName).toString());
    }
}
