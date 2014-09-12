package lv.telepit.ui.view;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import lv.telepit.TelepitUI;
import lv.telepit.ui.component.CustomMenuBar;
import lv.telepit.utils.AuthUtil;

import java.util.ResourceBundle;

public abstract class AbstractView extends VerticalLayout implements View {

    protected TelepitUI ui;

    protected VerticalLayout content;

    protected String name;

    protected static ResourceBundle bundle = ResourceBundle.getBundle("bundle");
    private final CustomMenuBar menuBar;

    protected AbstractView(Navigator navigator, TelepitUI ui, String name) {
        this.ui = ui;
        this.name = name;

        Label logo = new Label("<div class='logo'></div>");
        logo.setContentMode(ContentMode.HTML);

        menuBar = new CustomMenuBar(navigator);
        menuBar.setWidth("100%");

        addComponent(logo);
        addComponent(menuBar);

        content = new VerticalLayout();
        content.setSpacing(true);
        content.setMargin(true);

        buildContent();

        addComponent(content);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        if (ui.getCurrentUser() == null) {
            ui.getNavigator().navigateTo("");
            return;
        } else if (!AuthUtil.isAllowed(name, ui.getCurrentUser())) {
            ui.getNavigator().navigateTo("service");
            return;
        }
        menuBar.checkAuthority(ui.getCurrentUser());
        checkAuthority();
        reset();
        refreshView();
    }

    public abstract void buildContent();

    public abstract void refreshView();

    public abstract void reset();

    public abstract void checkAuthority();

    public TelepitUI getUi() {
        return ui;
    }
}
