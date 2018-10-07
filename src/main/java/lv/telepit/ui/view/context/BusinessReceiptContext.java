package lv.telepit.ui.view.context;

import com.vaadin.event.Action;
import lv.telepit.model.BusinessReceipt;
import lv.telepit.ui.view.BusinessReceiptView;
import lv.telepit.ui.view.context.actions.AbstractAction;
import lv.telepit.ui.view.context.actions.BusinessReceiptBillAction;
import lv.telepit.ui.view.context.actions.BusinessReceiptFromTemplateAction;
import lv.telepit.ui.view.context.actions.BusinessReceiptPaidAction;

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
        visibleActions.add(new BusinessReceiptBillAction(((BusinessReceipt) target), view));
        visibleActions.add(new BusinessReceiptPaidAction(((BusinessReceipt) target), view));
        visibleActions.add(new BusinessReceiptFromTemplateAction(((BusinessReceipt) target), view));

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
