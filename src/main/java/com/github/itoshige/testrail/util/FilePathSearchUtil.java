package com.github.itoshige.testrail.util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();

    /**
     * get target file path
     * 
     * @param fileName
     * @return
     */
    public static String getPath(String fileName) {
        URL url = FilePathSearchUtil.class.getClassLoader().getResource(fileName);
        if (url != null && !url.getPath().isEmpty())
            return url.getPath();

        if (isWindows()) {
            String path = getPathFromRepository(System.getProperty("user.dir"), fileName);
            if (path != null && !path.isEmpty())
                return path;
        }

        throw new TestInitializerException(String.format("fileName:%s doesn't exist.",
            ConfigrationUtil.CONFIG_FILE));
    }

    private static boolean isWindows() {
        return OS_NAME.startsWith("windows");
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
}
