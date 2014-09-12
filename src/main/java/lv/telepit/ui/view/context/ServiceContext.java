package lv.telepit.ui.view.context;

import com.vaadin.event.Action;
import lv.telepit.model.ServiceGood;
import lv.telepit.ui.view.ServiceView;
import lv.telepit.ui.view.context.actions.AbstractAction;
import lv.telepit.ui.view.context.actions.ServiceBillAction;
import lv.telepit.ui.view.context.actions.ServiceHistoryAction;
import lv.telepit.ui.view.context.actions.ServiceStatusBrokenAction;
import lv.telepit.ui.view.context.actions.ServiceStatusInRepairAction;
import lv.telepit.ui.view.context.actions.ServiceStatusInWaitingAction;
import lv.telepit.ui.view.context.actions.ServiceStatusOnDetailsAction;
import lv.telepit.ui.view.context.actions.ServiceStatusRepairedAction;
import lv.telepit.ui.view.context.actions.ServiceStatusReturnedAction;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ServiceContext implements Action.Handler {

    private ServiceView view;

    public ServiceContext(ServiceView view) {
        this.view = view;
    }

    @Override
    public Action[] getActions(Object target, Object sender) {
        if (target == null) {
            return new Action[]{};
        }

        List<AbstractAction> visibleActions = new LinkedList<>();
        visibleActions.add(new ServiceStatusInWaitingAction(((ServiceGood) target), view));
        visibleActions.add(new ServiceStatusInRepairAction(((ServiceGood) target), view));
        visibleActions.add(new ServiceStatusRepairedAction(((ServiceGood) target), view));
        visibleActions.add(new ServiceStatusBrokenAction(((ServiceGood) target), view));
        visibleActions.add(new ServiceStatusReturnedAction(((ServiceGood) target), view));
        visibleActions.add(new ServiceStatusOnDetailsAction(((ServiceGood) target), view));
        visibleActions.add(new ServiceBillAction(((ServiceGood) target), view));
        visibleActions.add(new ServiceHistoryAction(((ServiceGood) target), view));

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
