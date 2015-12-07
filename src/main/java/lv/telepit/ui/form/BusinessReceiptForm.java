package lv.telepit.ui.form;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import lv.telepit.model.BusinessReceipt;
import lv.telepit.model.ReceiptItem;
import lv.telepit.ui.actions.SaveOnClick;
import lv.telepit.ui.component.Hr;
import lv.telepit.ui.form.converters.StringToDoubleConverter;
import lv.telepit.ui.form.converters.StringToIntConverter;
import lv.telepit.ui.form.fields.FieldFactory;
import lv.telepit.ui.view.AbstractView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BusinessReceiptForm extends FormLayout {

    private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");

    @PropertyId("store")
    private ComboBox storeField = FieldFactory.getStoreComboBox("businessReceipt.store");

    @PropertyId("providerName")
    private TextField providerNameField = FieldFactory.getTextField("businessReceipt.providerName");

    @PropertyId("providerPvnNum")
    private TextField providerPvnNumField = FieldFactory.getTextField("businessReceipt.providerPvnNum");

    @PropertyId("providerRegNum")
    private TextField providerRegNumField = FieldFactory.getTextField("businessReceipt.providerRegNum");

    @PropertyId("providerBankName")
    private TextField providerBankNameField = FieldFactory.getTextField("businessReceipt.providerBankName");

    @PropertyId("providerBankNum")
    private TextField providerBankNumField = FieldFactory.getTextField("businessReceipt.providerBankNum");

    @PropertyId("providerAddress")
    private TextField providerAddressField = FieldFactory.getTextField("businessReceipt.providerAddress");

    @PropertyId("receiverName")
    private TextField receiverNameField = FieldFactory.getTextField("businessReceipt.receiverName");

    @PropertyId("receiverRegNum")
    private TextField receiverRegNumField = FieldFactory.getTextField("businessReceipt.receiverRegNum");

    @PropertyId("receiverAddress")
    private TextField receiverAddressField = FieldFactory.getTextField("businessReceipt.receiverAddress");

    @PropertyId("receiverBankName")
    private TextField receiverBankNameField = FieldFactory.getTextField("businessReceipt.receiverBankName");

    @PropertyId("payTillDate")
    private DateField payTillDateField = FieldFactory.getDateField("businessReceipt.payTillDate");

    public BusinessReceiptForm(BeanItem<BusinessReceipt> businessReceiptItem, AbstractView view) {

        /*Initial settings.*/
        BusinessReceipt good = businessReceiptItem.getBean();
        if (good.getId() == 0) {
            good.setUser(view.getUi().getCurrentUser());
            good.setStore(view.getUi().getCurrentUser().getStore());
        }

        if (view.getUi().getCurrentUser().isAdmin()) {
            storeField.setRequired(true);
            addComponent(storeField);
        }

        /*View creation.*/
        FieldGroup binder = new FieldGroup(businessReceiptItem);
        binder.bindMemberFields(this);

        addComponent(new HorizontalLayout(providerNameField, providerRegNumField));
        addComponent(new HorizontalLayout(providerAddressField, providerPvnNumField));
        addComponent(new HorizontalLayout(providerBankNameField, providerBankNumField));
        addComponent(new Hr());
        addComponent(new HorizontalLayout(receiverNameField, receiverRegNumField));
        addComponent(new HorizontalLayout(receiverAddressField, receiverBankNameField));
        addComponent(new Hr());

        final VerticalLayout subLayout = new VerticalLayout();
        subLayout.setWidth("100%");
        subLayout.setSpacing(true);

        final Label total = new Label("", ContentMode.HTML);

        final List<ReceiptItem> receiptItems = new ArrayList<>();
        final PriceListener priceListener = new PriceListener(total, receiptItems);
        addReceiptItem(receiptItems, subLayout, priceListener);

        Button addButton = new Button(bundle.getString("default.button.add"));
        addButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                addReceiptItem(receiptItems, subLayout, priceListener);
            }
        });

        addComponent(addButton);
        addComponent(subLayout);
        addComponent(total);

        Button saveButton = new Button(bundle.getString("default.button.save.changes"));
        saveButton.addClickListener(new SaveGood(binder, businessReceiptItem.getBean(), view));

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        buttonLayout.setMargin(true);
        buttonLayout.setWidth("100%");
        buttonLayout.addComponent(saveButton);
        buttonLayout.setComponentAlignment(saveButton, Alignment.BOTTOM_RIGHT);

        addComponent(buttonLayout);

    }

    private void addReceiptItem(final List<ReceiptItem> receiptItems, final Layout layout, final PriceListener priceListener) {
        final ReceiptItem item = new ReceiptItem();
        item.setPrice(0.0);
        item.setCount(1);
        receiptItems.add(item);
        priceListener.update();

        BeanItem<ReceiptItem> beanItem = new BeanItem<>(item);

        TextField nameField = new TextField("Nosaukums", beanItem.getItemProperty("name"));
        nameField.setImmediate(true);
        nameField.setWidth(100f, Sizeable.Unit.PIXELS);
        nameField.setNullRepresentation("");

        final TextField priceField = new TextField("Cena", beanItem.getItemProperty("price"));
        priceField.setImmediate(true);
        priceField.setRequired(true);
        priceField.setWidth(100f, Sizeable.Unit.PIXELS);
        priceField.setConverter(new StringToDoubleConverter());
        priceField.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                try {
                    priceField.validate();
                } catch (Validator.InvalidValueException e) {
                    Notification.show("Nepareiza cena!", Notification.Type.ERROR_MESSAGE);
                    item.setPrice(0.0);
                    priceField.setValue(String.valueOf(0.0));
                }
                priceListener.update();
            }
        });

        final TextField countField = new TextField("Daudzums", beanItem.getItemProperty("count"));
        countField.setImmediate(true);
        countField.setRequired(true);
        countField.setWidth(100f, Sizeable.Unit.PIXELS);
        countField.setConverter(new StringToIntConverter());
        countField.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                try {
                    countField.validate();
                } catch (Validator.InvalidValueException e) {
                    Notification.show("Nepareizs daudzums!", Notification.Type.ERROR_MESSAGE);
                    item.setCount(1);
                    countField.setValue(String.valueOf(1));
                }
                priceListener.update();
            }
        });

        Button deleteButton = new Button(bundle.getString("default.button.delete"));
        deleteButton.setStyleName("small");
        if (receiptItems.size() == 1) {
            deleteButton.setEnabled(false);
        }

        final HorizontalLayout subLayout = new HorizontalLayout(nameField, priceField, countField, deleteButton);
        subLayout.setWidth("100%");
        subLayout.setSpacing(true);
        subLayout.setComponentAlignment(priceField, Alignment.BOTTOM_RIGHT);
        subLayout.setComponentAlignment(countField, Alignment.BOTTOM_RIGHT);
        subLayout.setComponentAlignment(deleteButton, Alignment.BOTTOM_RIGHT);

        deleteButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                receiptItems.remove(item);
                layout.removeComponent(subLayout);
                priceListener.update();
            }
        });

        layout.addComponent(subLayout);

    }

    private class SaveGood extends SaveOnClick<BusinessReceipt> {

        private SaveGood(FieldGroup binder, BusinessReceipt entity, AbstractView view) {
            super(binder, entity, view);
        }

        @Override
        public void businessMethod() {
/*            StockService service = view.getUi().getStockService();
            if (entity.getId() == 0) {
                service.createGood(entity);
            } else {
                service.updateGood(entity);
            }*/
        }
    }

    private class PriceListener {
        Label label;
        List<ReceiptItem> list;

        private PriceListener(Label label, List<ReceiptItem> list) {
            this.label = label;
            this.list = list;
        }

        private void update() {
            BigDecimal total = new BigDecimal("0");
            for (ReceiptItem receiptItem : list) {
                total = total.add(receiptItem.getTotalPrice());
            }
            label.setValue("Kopā: " + total + "€");
        }
    }
}
