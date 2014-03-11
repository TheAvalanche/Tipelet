package lv.telepit.ui.form.fields;


import com.vaadin.server.Sizeable;
import com.vaadin.ui.TextField;
import lv.telepit.ui.form.converters.StringToDoubleConverter;
import lv.telepit.ui.form.converters.StringToIntConverter;
import lv.telepit.ui.form.converters.StringToLongConverter;

/**
 * SimpleNumberField.
 *
 * @author Alex Kartishev <aleksandrs.kartisevs@point.lv>
 *         Date: 13.3.7
 *         Time: 11:25
 */
public class SimpleNumberField extends TextField {

    /**Fields default width.*/
    private static final int DEFAULT_WIDTH = 200;

    /**
     * Constructor.
     * @param caption field caption
     * @param prompt field prompt
     * @param type type of field value
     * @param required is field required
     */
    public SimpleNumberField(final String caption,
                               final String prompt,
                               final String type,
                               final boolean required) {
        super(caption);
        setInputPrompt(prompt);
        setNullRepresentation("");
        setImmediate(true);
        setWidth(DEFAULT_WIDTH, Sizeable.Unit.PIXELS);
        setRequired(required);
        if ("long".equals(type)) {
            setConverter(new StringToLongConverter());
        } else if ("double".equals(type)) {
            setConverter(new StringToDoubleConverter());
        } else {
            setConverter(new StringToIntConverter());
        }
    }
}
