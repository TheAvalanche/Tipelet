package lv.telepit.ui.view.context.actions;

import com.vaadin.event.Action;
import lv.telepit.ui.view.AbstractView;

import java.util.ResourceBundle;

public abstract class AbstractAction<T extends AbstractView> extends Action {

    protected ResourceBundle bundle = ResourceBundle.getBundle("bundle");

    protected T view;

    protected AbstractAction(T view) {
        super("caption");
        this.view = view;
    }

    public abstract boolean show();

    public abstract void execute();
    
    public boolean refreshViewAfter() {
        return true;
    }
}
