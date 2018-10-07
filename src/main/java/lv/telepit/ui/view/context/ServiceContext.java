package lv.telepit.ui.view.context;

import com.vaadin.event.Action;
import lv.telepit.model.ServiceGood;
import lv.telepit.ui.view.ServiceView;
import lv.telepit.ui.view.context.actions.*;

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
        visibleActions.add(new ServiceCalledAction(((ServiceGood) target), view));
        visibleActions.add(new ServiceStatusInWaitingAction(((ServiceGood) target), view));
        visibleActions.add(new ServiceStatusInRepairAction(((ServiceGood) target), view));
        visibleActions.add(new ServiceStatusRepairedAction(((ServiceGood) target), view));
        visibleActions.add(new ServiceStatusBrokenAction(((ServiceGood) target), view));
        visibleActions.add(new ServiceStatusReturnedWithBillAction(((ServiceGood) target), view));
        if (!view.getUi().getCurrentUser().isAccessToBillOnly()) {
            visibleActions.add(new ServiceStatusReturnedWithoutBillAction(((ServiceGood) target), view));
        }
        visibleActions.add(new ServiceStatusOnDetailsAction(((ServiceGood) target), view));
        visibleActions.add(new ServiceBillAction(((ServiceGood) target), view));
        visibleActions.add(new ServiceHistoryAction(((ServiceGood) target), view));

        visibleActions.removeIf(action -> !action.show());
        return visibleActions.toArray(new Action[visibleActions.size()]);
    }

    @Override
    public void handleAction(Action action, Object sender, Object target) {
        ((AbstractAction) action).execute();
        if (((AbstractAction) action).refreshViewAfter()) {
            view.refreshView();
        }
    }
}
