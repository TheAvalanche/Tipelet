package lv.telepit.ui.actions;


import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import lv.telepit.ui.view.AbstractView;

import java.util.ResourceBundle;


/**
 * DeleteOnClick.
 *
 * @author Alex Kartishev <aleksandrs.kartisevs@point.lv>
 *         Date: 13.4.7
 *         Time: 12:21
 */
public abstract class DeleteOnClick<T> implements Button.ClickListener {

    /**Entity to save.*/
    protected T entity;
    /**Field to entity binder.*/
    private FieldGroup binder;
    /**Parent view.*/
    protected AbstractView view;

    private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");

    /**
     * Constructor.
     * @param binder form to entity binder
     * @param entity entity to save
     * @param view parent view where action is called
     */
    public DeleteOnClick(final FieldGroup binder, final T entity, final AbstractView view) {
        this.entity = entity;
        this.binder = binder;
        this.view = view;
    }

    @Override
    public final void buttonClick(final Button.ClickEvent event) {
        try {
            binder.commit();


            businessMethod();

            Notification.show(bundle.getString("delete.success"));
            view.refreshView();

        } catch (Exception e) {
            Notification.show(bundle.getString("delete.fail"), Notification.Type.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public abstract void businessMethod();
}
