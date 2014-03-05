package lv.telepit.ui.component;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import lv.telepit.model.Store;

/**
 * Created by Alex on 02/03/14.
 */
public class PropertyFilter<T> implements Container.Filter {

    private String propertyName;
    private T criteria;

    public PropertyFilter(String propertyName, T criteria) {
        this.propertyName = propertyName;
        this.criteria = criteria;
    }

    @Override
    public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
        Property p = item.getItemProperty(propertyName);

        if (p == null || !p.getType().equals(criteria.getClass())) {
            return false;
        }

        T value = (T) p.getValue();
        if (value == null && criteria != null) {
            return false;
        }

        if (value instanceof String) {
            return ((String) value).toLowerCase().startsWith(((String) criteria).toLowerCase());
        } else if (value instanceof Boolean) {
            return value.equals(criteria);
        } else if (value instanceof Store) {
            return ((Store) value).getId() == ((Store) criteria).getId();
        }
        return true;
    }

    @Override
    public boolean appliesToProperty(Object propertyId) {
        return propertyId != null && propertyId.equals(propertyName);
    }
}
