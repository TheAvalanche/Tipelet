package lv.telepit.ui.form.converters;

import com.vaadin.data.util.converter.StringToIntegerConverter;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * StringToIntConverter.
 *
 * @author Alex Kartishev <aleksandrs.kartisevs@point.lv>
 *         Date: 13.3.7
 *         Time: 12:01
 */
public class StringToIntConverter extends StringToIntegerConverter {

    @Override
    protected final NumberFormat getFormat(final Locale locale) {
        NumberFormat format = super.getFormat(locale);
        format.setGroupingUsed(false);
        return format;
    }

}
