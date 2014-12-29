package lv.telepit.ui.form;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import lv.telepit.backend.ServiceGoodService;
import lv.telepit.model.ServiceGood;
import lv.telepit.model.ServiceStatus;
import lv.telepit.ui.actions.SaveOnClick;
import lv.telepit.ui.form.fields.FieldFactory;
import lv.telepit.ui.view.AbstractView;

import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by Alex on 04/03/14.
 */
public class ServiceGoodForm extends FormLayout {
    private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");

    @PropertyId("customId")
    private TextField customIdField = FieldFactory.getTextField("service.customId");

    @PropertyId("category")
    private ComboBox categoryField = FieldFactory.getCategoryComboBox("service.category");

    @PropertyId("store")
    private ComboBox storeField = FieldFactory.getStoreComboBox("service.store");

    @PropertyId("name")
    private TextField nameField = FieldFactory.getTextField("service.name");

    @PropertyId("status")
    private ComboBox statusField = FieldFactory.getStatusComboBox("service.status");

    @PropertyId("imei")
    private TextField imeiField = FieldFactory.getTextField("service.imei");

    @PropertyId("accumNum")
    private TextField accumNumField = FieldFactory.getTextField("service.accumNum");

    @PropertyId("problem")
    private TextArea problemField = FieldFactory.getTextArea("service.problem");

    @PropertyId("price")
    private TextField price = FieldFactory.getNumberField("service.price");

    @PropertyId("warranty")
    private CheckBox warranty = FieldFactory.getCheckBox("service.warranty");

    @PropertyId("deliveredDate")
    private DateField deliveredDateField = FieldFactory.getDateField("service.deliveredDate");

    @PropertyId("startDate")
    private DateField startDateField = FieldFactory.getDateField("service.startDate");

    @PropertyId("finishDate")
    private DateField finishDateField = FieldFactory.getDateField("service.finishDate");

    @PropertyId("returnedDate")
    private DateField returnedDateField = FieldFactory.getDateField("service.returnedDate");

    @PropertyId("contactName")
    private TextField contactNameField = FieldFactory.getTextField("service.contactName");

    @PropertyId("contactPhone")
    private TextField contactPhoneField = FieldFactory.getTextField("service.contactPhone");

    @PropertyId("contactMail")
    private TextField contactMailField = FieldFactory.getTextField("service.contactMail");

    @PropertyId("additionalDescription")
    private TextArea additionalDescriptionField = FieldFactory.getTextArea("service.additionalDescription");

    public ServiceGoodForm(BeanItem<ServiceGood> serviceGoodItem, AbstractView view) {
        /*Initial settings.*/
        ServiceGood good = serviceGoodItem.getBean();
        if (good.getId() == 0) {
            good.setUser(view.getUi().getCurrentUser());
            good.setStore(view.getUi().getCurrentUser().getStore());
            good.setDeliveredDate(new Date());
            good.setStatus(ServiceStatus.WAITING);
            good.setPrice(0.00);
        }

        if (view.getUi().getCurrentUser().isAdmin()) {
            storeField.setRequired(true);
            addComponent(storeField);
        }

        /*View creation.*/
        FieldGroup binder = new FieldGroup(serviceGoodItem);
        binder.bindMemberFields(this);

        addComponent(customIdField);
        addComponent(categoryField);
        addComponent(nameField);
        if (view.getUi().getCurrentUser().isAdmin()) {
            addComponent(statusField);
        }
        addComponent(imeiField);
        addComponent(accumNumField);
        addComponent(problemField);
        if (view.getUi().getCurrentUser().isAdmin()) {
            addComponent(deliveredDateField);
            addComponent(startDateField);
            addComponent(finishDateField);
            addComponent(returnedDateField);
        }
        addComponent(price);
        addComponent(warranty);
        addComponent(contactNameField);
        addComponent(contactPhoneField);
        addComponent(contactMailField);
        addComponent(additionalDescriptionField);


        Button saveButton = new Button(bundle.getString("default.button.save.changes"));
        saveButton.addClickListener(new SaveGood(binder, serviceGoodItem.getBean(), view));

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        buttonLayout.setMargin(true);
        buttonLayout.setWidth("100%");
        buttonLayout.addComponent(saveButton);
        buttonLayout.setComponentAlignment(saveButton, Alignment.BOTTOM_RIGHT);

        addComponent(buttonLayout);
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
