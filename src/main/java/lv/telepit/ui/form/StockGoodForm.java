package lv.telepit.ui.form;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import lv.telepit.backend.ServiceGoodService;
import lv.telepit.model.ServiceGood;
import lv.telepit.model.ServiceStatus;
import lv.telepit.model.StockGood;
import lv.telepit.ui.actions.SaveOnClick;
import lv.telepit.ui.form.fields.FieldFactory;
import lv.telepit.ui.view.AbstractView;

import java.util.Date;

/**
 * Created by Alex on 07/04/2014.
 */
public class StockGoodForm extends FormLayout {

    @PropertyId("category")
    private ComboBox categoryField = FieldFactory.getCategoryComboBox("category");

    @PropertyId("store")
    private ComboBox storeField = FieldFactory.getStoreComboBox("store");

    @PropertyId("name")
    private TextField nameField = FieldFactory.getTextField("name");

    @PropertyId("price")
    private TextField price = FieldFactory.getNumberField("price");

    public StockGoodForm(BeanItem<StockGood> serviceGoodItem, AbstractView view) {

    }

    private class SaveGood extends SaveOnClick<ServiceGood> {

        private SaveGood(FieldGroup binder, ServiceGood entity, AbstractView view) {
            super(binder, entity, view);
        }

        @Override
        public void businessMethod() {
            ServiceGoodService service = view.getUi().getServiceGoodService();
            if (entity.getId() == 0) {
                service.saveGood(entity);
            } else {
                service.updateGood(entity);
            }
        }
    }
}
