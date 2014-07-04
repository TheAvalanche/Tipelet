package lv.telepit.ui.component;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.MenuBar;
import lv.telepit.model.User;

import java.util.ResourceBundle;

/**
 * Created by Alex on 21/02/14.
 */
public class CustomMenuBar extends MenuBar {

    private Navigator navigator;
    private final MenuItem adminItem;
    private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");
    private final MenuItem nameLabel;

    public CustomMenuBar(Navigator navigator) {
        this.navigator = navigator;
        this.addItem(bundle.getString("menu.stock"), new NavigateCommand("stock"));
        this.addItem(bundle.getString("menu.service"), new NavigateCommand("service"));
        adminItem = this.addItem(bundle.getString("menu.admin"), null, null);
        adminItem.addItem(bundle.getString("menu.financial"), new NavigateCommand("report"));
        adminItem.addItem(bundle.getString("menu.changes"), new NavigateCommand("changes"));
        adminItem.addItem(bundle.getString("menu.store"), new NavigateCommand("store"));
        adminItem.addItem(bundle.getString("menu.user"), new NavigateCommand("user"));
        adminItem.addItem(bundle.getString("menu.categories"), new NavigateCommand("category"));
        nameLabel = addItem("", null, null);
        nameLabel.setStyleName("align-right");
        nameLabel.setEnabled(false);
    }

    public void checkAuthority(User user) {
        if (user == null) return;
        if (!user.isAdmin()) {
            adminItem.setVisible(false);
        }
        nameLabel.setText("Sveiki, " + user.getName() + " (" + user.getStore().getName() + ")");
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
