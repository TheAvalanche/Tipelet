package lv.telepit.ui.form.fields;

import com.vaadin.ui.*;

import static lv.telepit.ui.form.fields.FieldPropertyFactory.*;

/**
 * FieldFactory.
 * Depends on FieldPropertyFactory class, to resolve field properties.
 * @author Alex Kartishev <aleksandrs.kartisevs@point.lv>
 *         Date: 13.3.7
 *         Time: 09:50
 */
public final class FieldFactory {

    /**
     * Private non-instantiable constructor.
     */
    private FieldFactory() {

    }

    /**
     * Simple text field.
     * @param key field's key
     * @return field
     */
    public static TextField getTextField(final String key) {
        return new SimpleTextField(getCaption(key),
                                   getPrompt(key),
                                   getRegex(key),
                                   getRequired(key));
    }

    /**
     * Password field.
     * @param key field's key
     * @return field
     */
    public static PasswordField getPasswordField(final String key) {
        return new SimplePasswordField(getCaption(key),
                                       getPrompt(key),
                                       getRegex(key),
                                       getRequired(key));
    }

    /**
     * Checkbox field.
     * @param key field's key
     * @return checkbox
     */
    public static CheckBox getCheckBox(final String key) {
        return new CheckBox(getCaption(key));
    }

    /**
     * Number field (separate from simple text field as converter is needed).
     * @param key field's key
     * @return field
     */
    public static TextField getNumberField(final String key) {
        return new SimpleNumberField(getCaption(key),
                                     getPrompt(key),
                                     getType(key),
                                     getRequired(key));
    }

    /**
     * Partners combobox with all partners prefilled.
     * @param key field's key
     * @return combo box
     */
    public static ComboBox getStoreComboBox(final String key) {
        return new SimpleStoreComboBox(getCaption(key),
                                         getRequired(key));
    }

    /**
     * Partners combobox with all partners prefilled.
     * @param key field's key
     * @return combo box
     */
    public static ComboBox getUserComboBox(final String key) {
        return new SimpleUserComboBox(getCaption(key),
                getRequired(key));
    }

    /**
     * Partners combobox with all partners prefilled.
     * @param key field's key
     * @return combo box
     */
    public static ComboBox getStatusComboBox(final String key) {
        return new SimpleStatusComboBox(getCaption(key),
                getRequired(key));
    }

    /**
     * Partners combobox with all partners prefilled.
     * @param key field's key
     * @return combo box
     */
    public static ComboBox getCategoryComboBox(final String key) {
        return new SimpleCategoryComboBox(getCaption(key),
                getRequired(key));
    }

    /**
     * Partners combobox with change record types.
     * @param key field's key
     * @return combo box
     */
    public static ComboBox getTypeComboBox(final String key) {
        return new SimpleTypeComboBox(getCaption(key),
                getRequired(key));
    }

    public static ComboBox getBillComboBox(final String key) {
        return new SimpleBillComboBox(getCaption(key),
                getRequired(key));
    }

    /**
     * Partners combobox with all partners prefilled.
     * @param key field's key
     * @return combo box
     */
    public static TextArea getTextArea(final String key) {
        return new SimpleTextArea(getCaption(key),
                getPrompt(key),
                getRequired(key));
    }

    /**
     * Partners combobox with all partners prefilled.
     * @param key field's key
     * @return combo box
     */
    public static DateField getDateField(final String key) {
        return new SimpleDateField(getCaption(key),
                getRequired(key));
    }
}