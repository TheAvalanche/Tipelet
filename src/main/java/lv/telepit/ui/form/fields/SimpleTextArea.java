package lv.telepit.ui.form.fields;

import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.TextArea;

/**
 * Created by Alex on 04/03/14.
 */
public class SimpleTextArea extends TextArea {

    /**Fields default width.*/
    private static final int DEFAULT_WIDTH = 200;

    /**
     * Constructor.
     * @param caption field caption
     * @param prompt field prompt
     * @param required is field required
     */
    public SimpleTextArea(final String caption,
                           final String prompt,
                           final boolean required) {
        super(caption);
        setInputPrompt(prompt);
        setNullRepresentation("");
        setImmediate(true);
        setWidth(DEFAULT_WIDTH, Unit.PIXELS);
        setRequired(required);
    }
}
