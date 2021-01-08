package lv.telepit.ui.form;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import lv.telepit.backend.ServiceGoodService;
import lv.telepit.model.ServiceGood;
import lv.telepit.model.ServiceStatus;
import lv.telepit.model.User;
import lv.telepit.ui.actions.SaveOnClick;
import lv.telepit.ui.component.SpacedHorizontalLayout;
import lv.telepit.ui.form.fields.FieldFactory;
import lv.telepit.ui.view.AbstractView;

import java.util.Date;
import java.util.ResourceBundle;
import java.util.function.Predicate;

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

    @PropertyId("diagnostics")
    private TextField diagnostics = FieldFactory.getNumberField("service.diagnostics");

    @PropertyId("details")
    private TextField details = FieldFactory.getNumberField("service.details");

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

    @PropertyId("withBill")
    private CheckBox withBill = FieldFactory.getCheckBox("service.withBill");

    public ServiceGoodForm(BeanItem<ServiceGood> serviceGoodItem, AbstractView view) {
        /*Initial settings.*/
        ServiceGood good = serviceGoodItem.getBean();
        if (good.getId() == 0) {
            good.setUser(view.getUi().getCurrentUser());
            good.setStore(view.getUi().getCurrentUser().getStore());
            good.setDeliveredDate(new Date());
            good.setStatus(ServiceStatus.WAITING);
            good.setPrice(0.00);
            good.setDetails(0.00);
            good.setDiagnostics(0.00);
        }

        if (view.getUi().getCurrentUser().isAdmin()) {
            storeField.setRequired(true);
            addComponent(storeField);
        }

        /*View creation.*/
        FieldGroup binder = new FieldGroup(serviceGoodItem);
        binder.bindMemberFields(this);

        addComponent(view, customIdField, u -> true, User::isServiceWorker);
        addComponent(view, categoryField, u -> true, User::isServiceWorker);
        addComponent(view, nameField, u -> true, User::isServiceWorker);
        addComponent(view, statusField, User::isAdmin);
        addComponent(view, imeiField, u -> true, User::isServiceWorker);
        addComponent(view, problemField, u -> true, User::isServiceWorker);
        addComponent(view, deliveredDateField, User::isAdmin);
        addComponent(view, startDateField, User::isAdmin);
        addComponent(view, finishDateField, User::isAdmin);
        addComponent(view, returnedDateField, User::isAdmin);
        addComponent(view, diagnostics, u -> !u.isServiceWorker(), u -> !u.isAdmin() && !good.isNew());
        addComponent(view, details, u -> !u.isServiceWorker(), u -> !u.isAdmin() && !good.isNew());
        addComponent(view, price, u -> !u.isServiceWorker(), u -> !u.isAdmin() && !good.isNew());
        addComponent(view, warranty, u -> !u.isServiceWorker());
        addComponent(view, contactNameField, u -> !u.isServiceWorker());
        addComponent(view, contactPhoneField, u -> !u.isServiceWorker());
        addComponent(view, contactMailField, u -> !u.isServiceWorker());
        addComponent(view, additionalDescriptionField);
        if (!view.getUi().getCurrentUser().isAccessToBillOnly()) {
            addComponent(view, withBill, u -> !u.isServiceWorker());
        }

        Button saveButton = new Button(bundle.getString("default.button.save.changes"));
        saveButton.addClickListener(new SaveGood(binder, serviceGoodItem.getBean(), view));

        HorizontalLayout buttonLayout = new SpacedHorizontalLayout();
        buttonLayout.setSpacing(true);
        buttonLayout.setMargin(true);
        buttonLayout.setWidth("100%");
        buttonLayout.addComponent(saveButton);
        buttonLayout.setComponentAlignment(saveButton, Alignment.BOTTOM_RIGHT);

        addComponent(buttonLayout);
    }

    private void addComponent(AbstractView view, Component c) {
        addComponent(view, c, u -> true);
    }

    private void addComponent(AbstractView view, Component c, Predicate<User> visible) {
        addComponent(view, c, visible, u -> false);
    }

    private void addComponent(AbstractView view, Component c, Predicate<User> visible, Predicate<User> readOnly) {
        if (visible.test(view.getUi().getCurrentUser())) {
            addComponent(c);

            if (readOnly.test(view.getUi().getCurrentUser())) {
                c.setReadOnly(true);
            }
        }
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
