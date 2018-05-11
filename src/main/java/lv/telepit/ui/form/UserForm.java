package lv.telepit.ui.form;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import lv.telepit.backend.CommonService;
import lv.telepit.model.User;
import lv.telepit.ui.actions.SaveOnClick;
import lv.telepit.ui.component.Hr;
import lv.telepit.ui.component.SpacedHorizontalLayout;
import lv.telepit.ui.form.fields.FieldFactory;
import lv.telepit.ui.view.AbstractView;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ResourceBundle;

/**
 * Created by Alex on 21/02/14.
 */
public class UserForm extends FormLayout {

    private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");

    @PropertyId("name")
    private TextField nameField = FieldFactory.getTextField("user.name");

    @PropertyId("surname")
    private TextField surnameField = FieldFactory.getTextField("user.surname");

    @PropertyId("login")
    private TextField loginField = FieldFactory.getTextField("user.login");

    @PropertyId("password")
    private PasswordField passwordField = FieldFactory.getPasswordField("user.password");

    @PropertyId("phone")
    private TextField phoneField = FieldFactory.getTextField("user.phone");

    @PropertyId("store")
    private ComboBox storeField = FieldFactory.getStoreComboBox("user.store");

    @PropertyId("admin")
    private CheckBox adminField = FieldFactory.getCheckBox("user.admin");

    @PropertyId("accessToAddInStock")
    private CheckBox accessToAddInStock = FieldFactory.getCheckBox("user.accessToAddInStock");

    @PropertyId("accessToBillOnly")
    private CheckBox accessToBillOnly = FieldFactory.getCheckBox("user.accessToBillOnly");


    public UserForm(BeanItem<User> userItem, AbstractView view) {

        FieldGroup binder = new FieldGroup(userItem);
        binder.bindMemberFields(this);
        addComponent(nameField);
        addComponent(surnameField);
        addComponent(loginField);
        addComponent(passwordField);
        addComponent(passwordField);
        addComponent(phoneField);
        addComponent(adminField);
        addComponent(storeField);
        addComponent(new Hr());
        addComponent(new Label(bundle.getString("user.form.access.label")));
        addComponent(accessToAddInStock);
        addComponent(accessToBillOnly);
        addComponent(new Hr());


        Button saveButton = new Button(bundle.getString("default.button.save.changes"));
        saveButton.addClickListener(new SaveUser(binder, userItem.getBean(), view));

        HorizontalLayout buttonLayout = new SpacedHorizontalLayout();
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
        public void businessMethod() throws Exception {

            CommonService service = view.getUi().getCommonService();
            if (entity.getId() == 0) {
                entity.setPassword(DigestUtils.md5Hex(entity.getPassword()));
                service.saveUser(entity);
            } else {
                if (entity.getPassword().length() != 32) {
                    entity.setPassword(DigestUtils.md5Hex(entity.getPassword()));
                }
                service.updateUser(entity);
            }

        }
    }

}
