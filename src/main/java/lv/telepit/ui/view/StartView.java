package lv.telepit.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import lv.telepit.TelepitUI;
import lv.telepit.ui.component.LoginWindow;

/**
 * StartView.
 *
 * @author Alex Kartishev <aleksandrs.kartisevs@point.lv>
 *         Date: 13.2.7
 *         Time: 13:29
 */
public class StartView extends VerticalLayout implements View {

    /**Application.*/
    private final TelepitUI application;

    /**
     * Constructor.
     * @param application parent application
     */
    public StartView(final TelepitUI application) {
        this.application = application;
    }


    @Override
    public final void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        application.addWindow(new LoginWindow(application));
    }
}