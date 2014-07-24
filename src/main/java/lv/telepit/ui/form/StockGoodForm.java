package lv.telepit.ui.form;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import lv.telepit.backend.StockService;
import lv.telepit.backend.criteria.StockGoodCriteria;
import lv.telepit.model.SoldItem;
import lv.telepit.model.StockGood;
import lv.telepit.ui.actions.SaveOnClick;
import lv.telepit.ui.form.fields.FieldFactory;
import lv.telepit.ui.view.AbstractView;

import java.util.*;

/**
 * Created by Alex on 07/04/2014.
 */
public class StockGoodForm extends FormLayout {

    private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");

    @PropertyId("category")
    private ComboBox categoryField = FieldFactory.getCategoryComboBox("stock.category");

    @PropertyId("store")
    private ComboBox storeField = FieldFactory.getStoreComboBox("stock.store");

    @PropertyId("name")
    private TextField nameField = FieldFactory.getTextField("stock.name");

    @PropertyId("model")
    private TextField modelField = FieldFactory.getTextField("stock.model");

    @PropertyId("compatibleModels")
    private TextField compatibleModelsField = FieldFactory.getTextField("stock.compatibleModels");

    @PropertyId("price")
    private TextField priceField = FieldFactory.getNumberField("stock.price");

    @PropertyId("count")
    private TextField countField = FieldFactory.getNumberField("stock.count");

    @PropertyId("advance")
    private TextField advanceField = FieldFactory.getNumberField("stock.advance");


    public StockGoodForm(BeanItem<StockGood> stockGoodItem, AbstractView view) {

        /*Initial settings.*/
        StockGood good = stockGoodItem.getBean();
        if (good.getId() == 0) {
            good.setUser(view.getUi().getCurrentUser());
            good.setStore(view.getUi().getCurrentUser().getStore());
            good.setPrice(0.00);
            if (!view.getUi().getCurrentUser().isAdmin()) {
                good.setOrdered(true);
                good.setAdvance(0.00);
            }
        }

        if (view.getUi().getCurrentUser().isAdmin()) {
            storeField.setRequired(true);
            addComponent(storeField);
        }

        /*View creation.*/
        FieldGroup binder = new FieldGroup(stockGoodItem);
        binder.bindMemberFields(this);

        addComponent(categoryField);
        addComponent(nameField);
        addComponent(modelField);
        addComponent(compatibleModelsField);
        addComponent(countField);
        if (view.getUi().getCurrentUser().isAdmin()) {
            addComponent(priceField);
        }
        if (good.isOrdered() && !view.getUi().getCurrentUser().isAdmin()) {
            addComponent(advanceField);
        }

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

                if (entity.isOrdered()) {
                    Map<StockGoodCriteria, Object> criteria = new HashMap<>();
                    criteria.put(StockGoodCriteria.ID, entity.getId());
                    entity = service.findGoods(criteria).get(0);

                    SoldItem soldItem = new SoldItem();
                    soldItem.setPrice(entity.getAdvance());
                    soldItem.setCode(null);
                    soldItem.setInfo("Avanss");
                    soldItem.setSoldDate(new Date());
                    soldItem.setStore(entity.getStore());
                    soldItem.setUser(entity.getUser());
                    soldItem.setWithBill(false);

                    service.sell(entity, Collections.singletonList(soldItem));
                }
            } else {
                service.updateGood(entity);
            }
        }
    }
}
