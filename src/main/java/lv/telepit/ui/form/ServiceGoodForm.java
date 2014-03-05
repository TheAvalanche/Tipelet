package lv.telepit.ui.form;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import lv.telepit.backend.ServiceGoodService;
import lv.telepit.backend.UserService;
import lv.telepit.model.ServiceGood;
import lv.telepit.model.User;
import lv.telepit.ui.actions.SaveOnClick;
import lv.telepit.ui.form.fields.FieldFactory;
import lv.telepit.ui.view.AbstractView;

import java.util.ResourceBundle;

/**
 * Created by Alex on 04/03/14.
 */
public class ServiceGoodForm extends FormLayout {
    private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");

    @PropertyId("name")
    private TextField nameField = FieldFactory.getTextField("name3");

    @PropertyId("imei")
    private TextField imeiField = FieldFactory.getTextField("imei");

    @PropertyId("accumNum")
    private TextField accumNumField = FieldFactory.getTextField("accumNum");

    @PropertyId("store")
    private ComboBox storeField = FieldFactory.getStoreComboBox("store");

    @PropertyId("user")
    private ComboBox userField = FieldFactory.getUserComboBox("user");

    @PropertyId("problem")
    private TextArea problemField = FieldFactory.getTextArea("problem");

    @PropertyId("price")
    private TextField price = FieldFactory.getNumberField("price");

    @PropertyId("deliveredDate")
    private DateField deliveredDateField = FieldFactory.getDateField("deliveredDate");

    @PropertyId("startDate")
    private DateField startDateField = FieldFactory.getDateField("startDate");

    @PropertyId("finishDate")
    private DateField finishDateField = FieldFactory.getDateField("finishDate");

    @PropertyId("returnedDate")
    private DateField returnedDateField = FieldFactory.getDateField("returnedDate");

    @PropertyId("contactName")
    private TextField contactNameField = FieldFactory.getTextField("contactName");

    @PropertyId("contactPhone")
    private TextField contactPhoneField = FieldFactory.getTextField("contactPhone");

    @PropertyId("contactMail")
    private TextField contactMailField = FieldFactory.getTextField("contactMail");

    @PropertyId("additionalDescription")
    private TextArea additionalDescriptionField = FieldFactory.getTextArea("additionalDescription");

    public ServiceGoodForm(BeanItem<ServiceGood> serviceGoodItem, AbstractView view) {

        FieldGroup binder = new FieldGroup(serviceGoodItem);
        binder.bindMemberFields(this);
        addComponent(nameField);
        addComponent(imeiField);
        addComponent(storeField);
        addComponent(userField);
        addComponent(accumNumField);
        addComponent(problemField);
        addComponent(deliveredDateField);
        addComponent(startDateField);
        addComponent(finishDateField);
        addComponent(returnedDateField);
        addComponent(price);
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
