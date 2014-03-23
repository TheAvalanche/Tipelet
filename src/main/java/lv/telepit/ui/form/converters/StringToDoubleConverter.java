package lv.telepit.ui.form.converters;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Alex on 05/03/14.
 */
public class StringToDoubleConverter extends com.vaadin.data.util.converter.StringToDoubleConverter {

    @Override
    public final Double convertToModel(final String value,
                                     final Class<? extends Double> targetType,
                                     final Locale locale) throws ConversionException {

        try {
            return value == null ? null : Double.parseDouble(value.replace(",", ".").replace(" ", "."));
        } catch (NumberFormatException e) {
            throw new ConversionException("Cannot convert to Double");
        }
    }

    @Override
    public final Class<Double> getModelType() {
        return Double.class;
    }

    @Override
    protected final NumberFormat getFormat(final Locale locale) {
        NumberFormat format = super.getFormat(locale);
        format.setMinimumFractionDigits(2);
        format.setGroupingUsed(false);
        return format;
    }
}
