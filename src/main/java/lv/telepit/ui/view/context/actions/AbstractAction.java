package lv.telepit.ui.view.context.actions;

import com.vaadin.event.Action;
import lv.telepit.model.StockGood;
import lv.telepit.ui.view.AbstractView;

import java.util.ResourceBundle;

public abstract class AbstractAction extends Action {

    protected ResourceBundle bundle = ResourceBundle.getBundle("bundle");

    protected AbstractView view;

    protected AbstractAction(AbstractView view) {
        super("caption");
        this.view = view;
    }

    public abstract boolean show();

    public abstract void execute();
}
