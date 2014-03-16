package lv.telepit.ui.actions;


import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import lv.telepit.ui.view.AbstractView;


/**
 * SaveAction.
 *
 * @author Alex Kartishev <aleksandrs.kartisevs@point.lv>
 *         Date: 13.3.7
 *         Time: 13:25
 */
public abstract class SaveOnClick<T> implements Button.ClickListener {

    /**Entity to save.*/
    protected T entity;
    /**Field to entity binder.*/
    private FieldGroup binder;
    /**Parent view.*/
    protected AbstractView view;


    /**
     * Constructor.
     * @param binder form to entity binder
     * @param entity entity to save
     * @param view parent view where action is called
     */
    public SaveOnClick(final FieldGroup binder, final T entity, final AbstractView view) {
        this.entity = entity;
        this.binder = binder;
        this.view = view;
    }

    @Override
    public final void buttonClick(final Button.ClickEvent event) {
        try {
            binder.commit();

            businessMethod();

            Notification.show("Veiksmīgi saglabāts!");
            view.refreshView();

        } catch (Exception e) {
            Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public abstract void businessMethod() throws Exception;
}
