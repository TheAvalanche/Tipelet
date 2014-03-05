package lv.telepit.ui.form.converters;

import com.vaadin.data.util.converter.AbstractStringToNumberConverter;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * StringToLongConverter.
 *
 * @author Alex Kartishev <aleksandrs.kartisevs@point.lv>
 *         Date: 13.2.7
 *         Time: 16:53
 */
public class StringToLongConverter extends AbstractStringToNumberConverter<Long> {

    @Override
    public final Long convertToModel(final String value,
                               final Class<? extends Long> targetType,
                               final Locale locale) throws ConversionException {
        Number n = convertToNumber(value, targetType, locale);
        return n == null ? null : n.longValue();
    }

    @Override
    public final Class<Long> getModelType() {
        return Long.class;
    }

    @Override
    protected final NumberFormat getFormat(final Locale locale) {
        NumberFormat format = super.getFormat(locale);
        format.setGroupingUsed(false);
        return format;
    }
}
