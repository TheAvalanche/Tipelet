package lv.telepit.ui.form.fields;

import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.DateField;

/**
 * Created by Alex on 04/03/14.
 */
public class SimpleDateField extends DateField {
    /**Fields default width.*/
    private static final int DEFAULT_WIDTH = 200;

    /**
     * Constructor.
     * @param caption field caption
     * @param required is field required
     */
    public SimpleDateField(final String caption,
                           final boolean required) {
        super(caption);
        setImmediate(true);
        setWidth(DEFAULT_WIDTH, Unit.PIXELS);
        setRequired(required);
    }
}
