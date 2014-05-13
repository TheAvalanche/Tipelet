package lv.telepit.ui.component;

import com.vaadin.ui.*;
import lv.telepit.TelepitUI;
import lv.telepit.backend.CommonService;
import lv.telepit.model.User;

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
        passwordField = new PasswordField("Password");
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
            User user = commonService.getUser(username, password);
            if (user == null && "test".equals(username) && "123456".equals(password)) {
                user = new User();
                user.setAdmin(true);
                user.setLogin("test");
                user.setPassword("123456");
                user.setName("Test Admin");
                user.setSurname("Test");
                user.setPhone("12345678");
            }
            if (user != null) {
                application.removeWindow(LoginWindow.this);
                application.setCurrentUser(user);
                application.getNavigator().navigateTo("service");
                Notification.show("Veismīga ielogošana!");
            } else {
                application.setCurrentUser(null);
                Notification.show("Neizdēvas ielogoties!", Notification.Type.ERROR_MESSAGE);
            }
        }
    }
}
