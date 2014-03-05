package lv.telepit.ui.form;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import lv.telepit.TelepitUI;
import lv.telepit.backend.UserService;
import lv.telepit.model.User;
import lv.telepit.ui.actions.SaveOnClick;
import lv.telepit.ui.form.fields.FieldFactory;
import lv.telepit.ui.view.AbstractView;
import lv.telepit.ui.view.ServiceView;

import java.util.ResourceBundle;

/**
 * Created by Alex on 21/02/14.
 */
public class UserForm extends FormLayout {

    private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");

    @PropertyId("name")
    private TextField nameField = FieldFactory.getTextField("name");

    @PropertyId("surname")
    private TextField surnameField = FieldFactory.getTextField("surname");

    @PropertyId("login")
    private TextField loginField = FieldFactory.getTextField("login");

    @PropertyId("password")
    private PasswordField passwordField = FieldFactory.getPasswordField("password");

    @PropertyId("phone")
    private TextField phoneField = FieldFactory.getTextField("phone");

    @PropertyId("store")
    private ComboBox storeField = FieldFactory.getStoreComboBox("store");

    @PropertyId("admin")
    private CheckBox adminField = FieldFactory.getCheckBox("admin");

    @PropertyId("deleted")
    private CheckBox deletedField = FieldFactory.getCheckBox("deleted");


    public UserForm(BeanItem<User> userItem, AbstractView view) {

        FieldGroup binder = new FieldGroup(userItem);
        binder.bindMemberFields(this);
        addComponent(nameField);
        addComponent(surnameField);
        addComponent(loginField);
        addComponent(passwordField);
        addComponent(passwordField);
        addComponent(phoneField);
        addComponent(storeField);
        addComponent(adminField);
        addComponent(deletedField);


        Button saveButton = new Button(bundle.getString("default.button.save.changes"));
        saveButton.addClickListener(new SaveUser(binder, userItem.getBean(), view));

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        buttonLayout.setMargin(true);
        buttonLayout.setWidth("100%");
        buttonLayout.addComponent(saveButton);
        buttonLayout.setComponentAlignment(saveButton, Alignment.BOTTOM_RIGHT);

        addComponent(buttonLayout);


    }

    private class SaveUser extends SaveOnClick<User> {

        private SaveUser(FieldGroup binder, User entity, AbstractView view) {
            super(binder, entity, view);
        }

        @Override
        public void businessMethod() {
            UserService service = view.getUi().getUserService();
            if (entity.getId() == 0) {
                service.saveUser(entity);
            } else {
                service.updateUser(entity);
            }
        }
    }

}
