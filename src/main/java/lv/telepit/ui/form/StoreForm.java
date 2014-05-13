package lv.telepit.ui.form;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import lv.telepit.backend.CommonService;
import lv.telepit.model.Store;
import lv.telepit.ui.actions.SaveOnClick;
import lv.telepit.ui.form.fields.FieldFactory;
import lv.telepit.ui.view.AbstractView;

import java.util.ResourceBundle;

/**
 * Created by Alex on 03/03/14.
 */
public class StoreForm extends FormLayout {

    private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");

    @PropertyId("name")
    private TextField nameField = FieldFactory.getTextField("store.name");

    @PropertyId("city")
    private TextField cityField = FieldFactory.getTextField("store.city");

    @PropertyId("address")
    private TextField addressField = FieldFactory.getTextField("store.address");


    public StoreForm(BeanItem<Store> storeItem, AbstractView view) {

        FieldGroup binder = new FieldGroup(storeItem);
        binder.bindMemberFields(this);
        addComponent(nameField);
        addComponent(cityField);
        addComponent(addressField);


        Button saveButton = new Button(bundle.getString("default.button.save.changes"));
        saveButton.addClickListener(new SaveStore(binder, storeItem.getBean(), view));

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        buttonLayout.setMargin(true);
        buttonLayout.setWidth("100%");
        buttonLayout.addComponent(saveButton);
        buttonLayout.setComponentAlignment(saveButton, Alignment.BOTTOM_RIGHT);

        addComponent(buttonLayout);


    }

    private class SaveStore extends SaveOnClick<Store> {

        private SaveStore(FieldGroup binder, Store entity, AbstractView view) {
            super(binder, entity, view);
        }

        @Override
        public void businessMethod() throws Exception {
            CommonService service = view.getUi().getCommonService();
            if (entity.getId() == 0) {
                service.saveStore(entity);
            } else {
                service.updateStore(entity);
            }
        }
    }
}
