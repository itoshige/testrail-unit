/**
 * 
 */
package org.itoshige.testrail.util;

import java.io.FileInputStream;
import java.util.Properties;

import org.itoshige.testrail.client.TestInitializerException;

/**
 * @author abhijeet.burle
 *
 * Created on 2015/04/19
 */
public enum TestRailUnitPropertyUtil {
	INSTANCE;
	private final Properties prop = new Properties();
    
	/**
	 * Loads the property file [testrail-unit.properties]
	 */
	TestRailUnitPropertyUtil(){
		try {
			prop.load(new FileInputStream(FileSearchUtil.getFilePath(System.getProperty("user.dir"),
		            "testrail-unit.properties")));
        } catch (Exception e) {
            throw new TestInitializerException("testrail-unit can't be read.");
        }
	}
	
	/**
	 * @param key
	 * @return
	 */
	public String getProperty(String key) {
		return prop.getProperty(key);
	}
	
	/**
	 * @param key
	 * @return
	 */
	public Boolean isTestRailUnitDisabled() {
		Boolean isDisabled=Boolean.FALSE;
		String strValue = TestRailUnitPropertyUtil.INSTANCE.getProperty("testrail.isDisabled");
    	if(strValue!=null&&"TRUE".equals(strValue.toUpperCase())){
    		isDisabled=Boolean.TRUE;
    	}
    	return isDisabled;
	}
}
