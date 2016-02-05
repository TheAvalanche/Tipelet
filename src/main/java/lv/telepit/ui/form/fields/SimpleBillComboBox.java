package lv.telepit.ui.form.fields;

import com.vaadin.ui.ComboBox;

import java.util.ResourceBundle;

public class SimpleBillComboBox extends ComboBox {
    /**Fields default width.*/
    private static final int DEFAULT_WIDTH = 200;

    private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");

    /**
     * Constructor.
     * @param caption caption
     * @param required if required
     */
    public SimpleBillComboBox(final String caption, final boolean required) {
        super(caption);
        setRequired(required);
        setImmediate(true);
        setWidth(DEFAULT_WIDTH, Unit.PIXELS);
        try {
            for (Type type : Type.values()) {
                addItem(type);
                setItemCaption(type, type.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public enum Type {
        ALL(bundle.getString("all.types"), null), WITH_BILL(bundle.getString("withBill"), Boolean.TRUE), WITHOUT_BILL(bundle.getString("withoutBill"), Boolean.FALSE);

        private String value;

        private Boolean boolValue;

        Type(String value, Boolean boolValue) {
            this.value = value;
            this.boolValue = boolValue;
        }

        public String getValue() {
            return value;
        }

        public Boolean getBoolValue() {
            return boolValue;
        }
    }
}
