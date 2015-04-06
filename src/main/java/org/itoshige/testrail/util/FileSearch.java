package org.itoshige.testrail.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.itoshige.testrail.client.TestRailUnitException;

/**
 * @see http 
 *      ://www.mkyong.com/java/search-directories-recursively-for-file-in-java/
 * 
 * @author itoshige
 * 
 */
public class FileSearch {

    private String fileNameToSearch;
    private List<String> result = new ArrayList<String>();

    public String getFileNameToSearch() {
        return fileNameToSearch;
    }

    public void setFileNameToSearch(String fileNameToSearch) {
        this.fileNameToSearch = fileNameToSearch;
    }

    public List<String> getResult() {
        return result;
    }

    public static String getFilePath(String filePath, String fileName) {

        FileSearch fileSearch = new FileSearch();

        fileSearch.searchDirectory(new File(filePath), fileName);

        int size = fileSearch.getResult().size();
        if (size == 0)
            throw new TestRailUnitException(fileName + " doesn't exist");

        if (size > 1)
            throw new TestRailUnitException(fileName + " exist multiple");

        String matched = fileSearch.getResult().get(0);
        return matched;
    }

    public void searchDirectory(File directory, String fileNameToSearch) {

        setFileNameToSearch(fileNameToSearch);

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
                        if (getFileNameToSearch().equals(temp.getName().toLowerCase())) {
                            result.add(temp.getAbsoluteFile().toString());
                        }

                    }
                }
            }
        }

    }

}
