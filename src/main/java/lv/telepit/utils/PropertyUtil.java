package lv.telepit.utils;

import com.google.common.base.Strings;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Alex on 22/02/14.
 */
public class PropertyUtil {

    private Properties properties1 = new Properties();
    private Properties properties2 = new Properties();
    private static PropertyUtil ourInstance;
    private static Logger log = Logger.getLogger(PropertyUtil.class.getName());

    public static PropertyUtil getInstance() {
        if (ourInstance == null) {
            ourInstance = new PropertyUtil();
        }
        return ourInstance;
    }

    private PropertyUtil() {
        cacheProperties();
    }

    public void cacheProperties() {
        cacheExternalProperties();
        cacheLocalProperties();
    }

    /**
     * Cache properties from TMS_HOME/application.properties to properties map.
     */
    private void cacheExternalProperties() {
        try {
            String tmsHome = System.getenv("TELEPIT_HOME");
            if (tmsHome != null) {
                FileInputStream file = new FileInputStream(tmsHome + "/application.properties");
                properties1.load(file);
            } else {
                throw new FileNotFoundException("TELEPIT_HOME environmental variable is not set!");
            }
        } catch (Exception e) {
            log.warn("Error during caching properties from external file: " + e.getMessage());
        }
    }

    /**
     * Cache properties from application.properties to properties map.
     */
    private void cacheLocalProperties() {
        try {
            InputStream inputStream = PropertyUtil.this.getClass().getResourceAsStream("/application.properties");
            properties2.load(inputStream);
        } catch (Exception e) {
            log.error("Error during caching properties from internal file!", e);
            e.printStackTrace();
        }
    }

    /**
     * Get property value or return default if not found.
     * @param arg property key
     * @param def default value
     * @return property value
     */
    public static String get(Object arg, String def) {
        String value = get(arg);
        return value == null ? def : value;
    }

    public static Boolean get(Object arg, Boolean def) {
        String value = get(arg);
        return value == null ? def : Boolean.valueOf(value);
    }

    public static Integer get(Object arg, Integer def) {
        String value = get(arg);
        return value == null ? def : Integer.valueOf(value);
    }

    /**
     * Return property value.
     * @param arg property key
     * @return value if found or null if not found
     */
    public static String get(Object arg) {
        String value1 = (String) getInstance().properties1.get(arg);
        String value2 = (String) getInstance().properties2.get(arg);

        //return value1 if it is found or value2 if not found. If both are not found - null is returned.
        String toReturn = Strings.isNullOrEmpty(value1) ? (Strings.isNullOrEmpty(value2) ? null : value2) : value1;
        log.debug("Looking for property: " + arg + " Found: " + toReturn);
        return toReturn;
    }
}
