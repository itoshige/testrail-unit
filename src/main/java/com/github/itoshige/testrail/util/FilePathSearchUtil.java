package com.github.itoshige.testrail.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.itoshige.testrail.client.TestInitializerException;

/**
 * File Search Util
 * 
 * @author itoshige
 * 
 */
public class FilePathSearchUtil {

    private String fileNameToSearch;
    private List<String> result = new ArrayList<String>();

    /**
     * get target file path
     * 
     * @param fileName
     * @return
     */
    public static String getPath(String fileName) {
        String path = getPathFromRepository(System.getProperty("user.dir"), fileName);
        if (path.isEmpty()) {
            path = getPathFromClassPath(fileName);
        }
        return path;
    }

    /**
     * @see http 
     *      ://www.mkyong.com/java/search-directories-recursively-for-file-in
     *      -java/
     * 
     * @param filePath
     * @param fileName
     * @return
     */
    private static String getPathFromRepository(String filePath, String fileName) {

        FilePathSearchUtil fileSearch = new FilePathSearchUtil();
        fileSearch.searchDirectory(new File(filePath), fileName);

        int size = fileSearch.result.size();
        if (size > 1)
            throw new TestInitializerException(new StringBuilder(fileName).append(" exist multiple")
                .toString());
        if (size == 0)
            return "";

        String matched = fileSearch.result.get(0);
        return matched;
    }

    public void searchDirectory(File directory, String fileNameToSearch) {
        this.fileNameToSearch = fileNameToSearch;

        if (directory.isDirectory())
            search(directory);

    }

    private void search(File file) {

        if (file.isDirectory()) {
            if (file.canRead()) {
                for (File temp : file.listFiles()) {
                    if (temp.isDirectory()) {
                        search(temp);
                    } else {
                        if (fileNameToSearch.equals(temp.getName().toLowerCase())) {
                            result.add(temp.getAbsoluteFile().toString());
                        }
                    }
                }
            }
        }
    }

    /**
     * @see http://m12i.hatenablog.com/entry/2015/02/02/005357
     * 
     * @param fileName
     * @return
     */
    private static String getPathFromClassPath(final String fileName) {
        final String pathSeparator = System.getProperty("path.separator");
        final String javaClassPath = System.getProperty("java.class.path");

        String filePath = "";
        final Set<File> set = new HashSet<File>();
        for (final String path : javaClassPath.split(pathSeparator)) {

            final File directoryOrArchiveFile = new File(path);
            final File directory = directoryOrArchiveFile.isDirectory() ? directoryOrArchiveFile
                : directoryOrArchiveFile.getParentFile();
            final File file = new File(directory, fileName);
            if (file.isFile()) {
                if (!set.add(file)) {
                    throw new TestInitializerException(new StringBuilder(fileName).append(" exist multiple")
                        .toString());
                }
                filePath = file.getPath();
            }
        }

        if (filePath.isEmpty())
            throw new TestInitializerException(new StringBuilder(fileName).append(" doesn't exist")
                .toString());

        return filePath;
    }
}
