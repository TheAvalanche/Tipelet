package lv.telepit.ui.form.fields;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.ComboBox;

import java.util.ResourceBundle;

public class SimpleTypeComboBox extends ComboBox {
    /**Fields default width.*/
    private static final int DEFAULT_WIDTH = 200;

    private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");

    /**
     * Constructor.
     * @param caption caption
     * @param required if required
     */
    public SimpleTypeComboBox(final String caption, final boolean required) {
        super(caption);
        setRequired(required);
        setImmediate(true);
        setWidth(DEFAULT_WIDTH, Sizeable.Unit.PIXELS);
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
        ALL(bundle.getString("all.types")), STOCK(bundle.getString("stock.types")), SERVICE(bundle.getString("service.types"));

        private String value;

        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
