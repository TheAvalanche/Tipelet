package lv.telepit.ui.view;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import lv.telepit.TelepitUI;
import lv.telepit.ui.component.CustomMenuBar;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Alex on 24/02/14.
 */
public abstract class AbstractView extends VerticalLayout implements View {

    protected TelepitUI ui;

    protected VerticalLayout content;

    protected static ResourceBundle bundle = ResourceBundle.getBundle("bundle");

    protected AbstractView(Navigator navigator, TelepitUI ui) {
        this.ui = ui;

        Label logo = new Label("<div class='logo'></div>");
        logo.setContentMode(ContentMode.HTML);

        CustomMenuBar menuBar = new CustomMenuBar(navigator);
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
        }
    }

    public abstract void buildContent();

    public abstract void refreshView();

    public TelepitUI getUi() {
        return ui;
    }
}
