package lv.telepit.ui.form.fields;

import com.vaadin.ui.ComboBox;
import lv.telepit.model.ServiceStatus;

/**
 * Created by Alex on 11/03/14.
 */
public class SimpleStatusComboBox extends ComboBox {

    /**Fields default width.*/
    private static final int DEFAULT_WIDTH = 200;


    /**
     * Constructor.
     * @param caption caption
     * @param required if required
     */
    public SimpleStatusComboBox(final String caption, final boolean required) {
        super(caption);
        setRequired(required);
        setWidth(DEFAULT_WIDTH, Unit.PIXELS);
        try {
            for (ServiceStatus status : ServiceStatus.values()) {
                addItem(status);
                setItemCaption(status, status.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
