package lv.telepit.ui.form;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import lv.telepit.backend.StockService;
import lv.telepit.backend.criteria.StockGoodCriteria;
import lv.telepit.model.SoldItem;
import lv.telepit.model.StockGood;
import lv.telepit.ui.actions.SaveOnClick;
import lv.telepit.ui.form.fields.FieldFactory;
import lv.telepit.ui.view.AbstractView;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class OrderStockGoodForm extends FormLayout {

    private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");

    @PropertyId("category")
    private ComboBox categoryField = FieldFactory.getCategoryComboBox("stock.category");

    @PropertyId("name")
    private TextField nameField = FieldFactory.getTextField("stock.name");

    @PropertyId("model")
    private TextField modelField = FieldFactory.getTextField("stock.model");

    @PropertyId("compatibleModels")
    private TextField compatibleModelsField = FieldFactory.getTextField("stock.compatibleModels");

    @PropertyId("count")
    private TextField countField = FieldFactory.getNumberField("stock.count");

    @PropertyId("advance")
    private TextField advanceField = FieldFactory.getNumberField("stock.advance");


    public OrderStockGoodForm(BeanItem<StockGood> stockGoodItem, AbstractView view) {

        /*Initial settings.*/
        StockGood good = stockGoodItem.getBean();
        if (good.getId() == 0) {
            good.setUser(view.getUi().getCurrentUser());
            good.setStore(view.getUi().getCurrentUser().getStore());
            good.setPrice(0.00);
            good.setLink(view.getUi().getStockService().generateUniqueLink());
            good.setOrdered(true);
            good.setAdvance(0.00);
        }

        /*View creation.*/
        FieldGroup binder = new FieldGroup(stockGoodItem);
        binder.bindMemberFields(this);

        advanceField.setRequired(true);

        addComponent(categoryField);
        addComponent(nameField);
        addComponent(modelField);
        addComponent(compatibleModelsField);
        addComponent(countField);
        addComponent(advanceField);


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

            } else {
                service.updateGood(entity);
            }
        }
    }
}
