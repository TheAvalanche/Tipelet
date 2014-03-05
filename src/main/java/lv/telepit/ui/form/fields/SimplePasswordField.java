package lv.telepit.ui.form.fields;

import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.PasswordField;

/**
 * SimplePasswordField.
 *
 * @author Alex Kartishev <aleksandrs.kartisevs@point.lv>
 *         Date: 13.3.7
 *         Time: 11:16
 */
public class SimplePasswordField extends PasswordField {

    /**Fields default width.*/
    private static final int DEFAULT_WIDTH = 200;

    /**
     * Constructor.
     * @param caption field caption
     * @param prompt field prompt
     * @param regex field validation regex
     * @param required is field required
     */
    public SimplePasswordField(final String caption,
                           final String prompt,
                           final String regex,
                           final boolean required) {
        super(caption);
        setInputPrompt(prompt);
        setNullRepresentation("");
        setImmediate(true);
        setWidth(DEFAULT_WIDTH, Sizeable.Unit.PIXELS);
        setRequired(required);
        addValidator(new RegexpValidator(regex, caption + ": Should be of form - " + prompt));
    }
}
