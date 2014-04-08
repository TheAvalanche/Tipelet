package lv.telepit.ui.form;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import lv.telepit.backend.ServiceGoodService;
import lv.telepit.backend.StockService;
import lv.telepit.model.ServiceGood;
import lv.telepit.model.ServiceStatus;
import lv.telepit.model.StockGood;
import lv.telepit.ui.actions.SaveOnClick;
import lv.telepit.ui.form.fields.FieldFactory;
import lv.telepit.ui.view.AbstractView;

import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by Alex on 07/04/2014.
 */
public class StockGoodForm extends FormLayout {

    private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");

    @PropertyId("category")
    private ComboBox categoryField = FieldFactory.getCategoryComboBox("category");

    @PropertyId("store")
    private ComboBox storeField = FieldFactory.getStoreComboBox("store");

    @PropertyId("name")
    private TextField nameField = FieldFactory.getTextField("name");

    @PropertyId("price")
    private TextField priceField = FieldFactory.getNumberField("price");

    @PropertyId("count")
    private TextField countField = FieldFactory.getNumberField("count");


    public StockGoodForm(BeanItem<StockGood> stockGoodItem, AbstractView view) {

                /*Initial settings.*/
        StockGood good = stockGoodItem.getBean();
        if (good.getId() == 0) {
            good.setUser(view.getUi().getCurrentUser());
            good.setStore(view.getUi().getCurrentUser().getStore());
        }

        if (view.getUi().getCurrentUser().isAdmin()) {
            storeField.setRequired(true);
            addComponent(storeField);
        }

        /*View creation.*/
        FieldGroup binder = new FieldGroup(stockGoodItem);
        binder.bindMemberFields(this);

        nameField.setCaption("Nosaukums");
        nameField.setInputPrompt("Vads NP420");
        addComponent(categoryField);
        addComponent(nameField);
        addComponent(priceField);
        addComponent(countField);



        Button saveButton = new Button(bundle.getString("default.button.save.changes"));
        saveButton.addClickListener(new SaveGood(binder, stockGoodItem.getBean(), view));

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        buttonLayout.setMargin(true);
        buttonLayout.setWidth("100%");
        buttonLayout.addComponent(saveButton);
        buttonLayout.setComponentAlignment(saveButton, Alignment.BOTTOM_RIGHT);

        addComponent(buttonLayout);

    }

    private class SaveGood extends SaveOnClick<StockGood> {

        private SaveGood(FieldGroup binder, StockGood entity, AbstractView view) {
            super(binder, entity, view);
        }

        @Override
        public void businessMethod() {
            StockService service = view.getUi().getStockService();
            if (entity.getId() == 0) {
                service.createGood(entity);
            } else {
                service.updateGood(entity);
            }
        }
    }
}
