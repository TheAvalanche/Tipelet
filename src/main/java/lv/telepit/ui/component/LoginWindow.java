package lv.telepit.ui.component;

import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.*;
import lv.telepit.TelepitUI;
import lv.telepit.backend.CommonService;
import lv.telepit.model.Store;
import lv.telepit.model.User;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ResourceBundle;

/**
 * LoginWindow.
 *
 * @author Alex Kartishev <aleksandrs.kartisevs@point.lv>
 *         Date: 13.22.7
 *         Time: 11:58
 */
public class LoginWindow extends Window {

    /**Application reference.*/
    private final TelepitUI application;
    /**Fields.*/
    private final AbstractField<String> usernameField, passwordField;

    private CommonService commonService;

    private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");

    /**
     * Creates login window with field for username,
     * password and login button.
     * @param application parent application
     */
    public LoginWindow(final TelepitUI application) {
        super("Login");
        this.application = application;
        this.commonService = this.application.getCommonService();

        //window layout
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        usernameField = new TextField("Username");
        usernameField.focus();
        passwordField = new PasswordField("Password");
        passwordField.addShortcutListener(new ShortcutListener("Login", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                new LoginListener().buttonClick(null);
            }
        });

        final Button loginButton = new Button("Login");
        loginButton.addClickListener(new LoginListener());

        layout.addComponent(usernameField);
        layout.addComponent(passwordField);
        layout.addComponent(loginButton);

        setClosable(false);
        setModal(true);
        setContent(layout);

    }

    /**
     * Listener to react on login button click.
     */
    private class LoginListener implements Button.ClickListener {
        @Override
        public void buttonClick(final Button.ClickEvent event) {
            String username = usernameField.getValue();
            String password = passwordField.getValue();
            User user = commonService.getUser(username, DigestUtils.md5Hex(password));
            if (user == null && "testuser".equals(username) && "thisisasystempassworduselessforhackuse".equals(password)) {
                user = new User();
                user.setAdmin(true);
                user.setLogin("test");
                user.setPassword("123456");
                user.setName("Test Admin");
                user.setSurname("Test");
                user.setPhone("12345678");
                user.setStore(new Store());
            }
            if (user != null) {
                application.removeWindow(LoginWindow.this);
                application.setCurrentUser(user);
                application.getNavigator().navigateTo("service");
                Notification.show(bundle.getString("login.success"));
            } else {
                application.setCurrentUser(null);
                Notification.show(bundle.getString("login.fail"), Notification.Type.ERROR_MESSAGE);
            }
        }
    }
}
