package lv.telepit.ui.form.fields;

import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.TextField;

/**
 * SimpleTextField.
 *
 * @author Alex Kartishev <aleksandrs.kartisevs@point.lv>
 *         Date: 13.3.7
 *         Time: 10:05
 */
public class SimpleTextField extends TextField {

    /**Fields default width.*/
    private static final int DEFAULT_WIDTH = 200;

    /**
     * Constructor.
     * @param caption field caption
     * @param prompt field prompt
     * @param regex field validation regex
     * @param required is field required
     */
    public SimpleTextField(final String caption,
                           final String prompt,
                           final String regex,
                           final boolean required) {
        super(caption);
        setInputPrompt(prompt);
        setNullRepresentation("");
        setImmediate(true);
        setWidth(DEFAULT_WIDTH, Unit.PIXELS);
        setRequired(required);
        addValidator(new RegexpValidator(regex, caption + ": Jābūt formātā - " + prompt));
    }
}
