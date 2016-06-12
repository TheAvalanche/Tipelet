package lv.telepit.ui.component;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Table;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;


public class CommonTable extends Table {

    private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");

    public CommonTable(BeanItemContainer container, final String bundleBase, String... headers) {
        this.setImmediate(true);
        this.setWidth("1200px");
        this.setContainerDataSource(container);
        this.setVisibleColumns(headers);
        this.setColumnHeaders(Collections2.transform(Arrays.asList(headers), s -> bundle.getString(bundleBase + "." + s)).toArray(new String[headers.length]));

        this.setSelectable(true);
        this.setImmediate(true);
        this.setSizeFull();
    }

    @Override
    protected String formatPropertyValue(Object rowId, Object colId, Property property) {
        Object v = property.getValue();
        if (v instanceof Date) {
            Date dateValue = (Date) v;
            return new SimpleDateFormat("dd.MM.yyyy HH:mm").format(dateValue);
        } else if (v instanceof Double) {
            Double doubleValue = (Double) v;
            return String.format("%.2f", doubleValue);
        }
        return super.formatPropertyValue(rowId, colId, property);
    }

    public void setColumnWidths(float... expandRatios) {
        int i = 0;
        for (Object o : this.getVisibleColumns()) {
            this.setColumnExpandRatio(o, expandRatios[i]);
            i++;
        }
    }

    public void setAlwaysRecalculateColumnWidths(boolean value) {
        this.alwaysRecalculateColumnWidths = value;
    }
}
