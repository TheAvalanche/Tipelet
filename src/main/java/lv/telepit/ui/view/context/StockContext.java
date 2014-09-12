package lv.telepit.ui.view.context;

import com.vaadin.event.Action;

import lv.telepit.model.*;

import lv.telepit.ui.view.StockView;
import lv.telepit.ui.view.context.actions.*;

import java.util.*;

public class StockContext implements Action.Handler {

    private StockView view;

    public StockContext(StockView view) {
        this.view = view;
    }

    @Override
    public Action[] getActions(Object target, Object sender) {
        if (target == null) {
            return new Action[]{};
        }

        List<AbstractAction> visibleActions = new LinkedList<>();
        visibleActions.add(new StockSellAction(((StockGood) target), view));
        visibleActions.add(new StockMoveAction(((StockGood) target), view));
        visibleActions.add(new StockOrderedAction(((StockGood) target), view));
        visibleActions.add(new StockBestsellerAction(((StockGood) target), view));
        visibleActions.add(new StockAttentionAction(((StockGood) target), view));
        visibleActions.add(new StockWarrantyAction(((StockGood) target), view));
        visibleActions.add(new StockHistoryAction(((StockGood) target), view));

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
