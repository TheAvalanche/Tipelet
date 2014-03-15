package lv.telepit.ui.component;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.MenuBar;
import lv.telepit.model.User;

/**
 * Created by Alex on 21/02/14.
 */
public class CustomMenuBar extends MenuBar {

    private Navigator navigator;
    private final MenuItem adminItem;

    public CustomMenuBar(Navigator navigator) {
        this.navigator = navigator;
        this.addItem("Noliktāva", new NavigateCommand("stock"));
        this.addItem("Serviss", new NavigateCommand("service"));
        adminItem = this.addItem("Vadīšana", null, null);
        adminItem.addItem("Veikali", new NavigateCommand("store"));
        adminItem.addItem("Lietotāji", new NavigateCommand("user"));
        adminItem.addItem("Kategorijas", new NavigateCommand("category"));
    }

    public void checkAutherity(User user) {
        if (user != null && !user.isAdmin()) {
            adminItem.setVisible(false);
        }
    }

    private class NavigateCommand implements Command {

        private String destination;

        private NavigateCommand(String destination) {
            this.destination = destination;
        }

        @Override
        public void menuSelected(MenuItem menuItem) {
            navigator.navigateTo(destination);
        }
    }


}
