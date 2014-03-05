package lv.telepit.ui.form.fields;


import lv.telepit.utils.PropertyUtil;

/**
 * FieldPropertyFactory.
 *
 * @author Alex Kartishev <aleksandrs.kartisevs@point.lv>
 *         Date: 13.3.7
 *         Time: 10:47
 */
public class FieldPropertyFactory {

    /**
     * Get caption from fields.properties file.
     * @param key key to field
     * @return caption
     */
    public static String getCaption(final String key) {
        return getFieldProperty(key + ".caption");
    }

    /**
     * Get prompt from fields.properties file.
     * @param key key to field
     * @return prompt
     */
    public static String getPrompt(final String key) {
        return getFieldProperty(key + ".prompt");
    }

    /**
     * Get regex from fields.properties file.
     * @param key key to field
     * @return regex
     */
    public static String getRegex(final String key) {
        return getFieldProperty(key + ".regex");
    }

    /**
     * Get required from fields.properties file.
     * @param key key to field
     * @return required
     */
    public static boolean getRequired(final String key) {
        return "true".equals(getFieldProperty(key + ".required"));
    }

    /**
     * Get type from fields.properties file.
     * @param key key to field
     * @return type
     */
    public static String getType(final String key) {
        return getFieldProperty(key + ".type");
    }

    /**
     * Get abstract property from fields.properties file.
     * @param key key to field
     * @return property
     */
    private static String getFieldProperty(final String key) {
        return PropertyUtil.get(key);
    }
}