package lv.telepit.ui.component;

import com.vaadin.data.util.converter.StringToBooleanConverter;

public class BooleanToTickConverter extends StringToBooleanConverter {
    @Override
    protected String getTrueString() {
        return "✔";
    }

    @Override
    protected String getFalseString() {
        return "✘";
    }
}
