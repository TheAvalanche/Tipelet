package lv.telepit.ui.view.context;

import com.vaadin.event.Action;
import lv.telepit.ui.view.BusinessReceiptView;
import lv.telepit.ui.view.context.actions.AbstractAction;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BusinessReceiptContext implements Action.Handler {

    private BusinessReceiptView view;

    public BusinessReceiptContext(BusinessReceiptView view) {
        this.view = view;
    }

    @Override
    public Action[] getActions(Object target, Object sender) {
        if (target == null) {
            return new Action[]{};
        }

        List<AbstractAction> visibleActions = new LinkedList<>();

        Iterator<AbstractAction> iter = visibleActions.iterator();
        while (iter.hasNext()) {
            AbstractAction action = iter.next();
            if (!action.show()) {
                iter.remove();
            }
        }
        return visibleActions.toArray(new Action[visibleActions.size()]);
    }

    @Override
    public void handleAction(Action action, Object sender, Object target) {
        ((AbstractAction) action).execute();
        view.refreshView();
    }


}
