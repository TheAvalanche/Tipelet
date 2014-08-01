package lv.telepit.ui.view.context.actions;

import lv.telepit.model.StockGood;
import lv.telepit.ui.view.AbstractView;

public class StockOrderedAction extends AbstractAction {

    private final StockGood good;

    public StockOrderedAction(StockGood good, AbstractView view) {
        super(view);
        this.setCaption(bundle.getString("ordered.item"));
        this.good = good;
    }

    @Override
    public boolean show() {
        return good.isOrdered() && view.getUi().getCurrentUser().isAdmin();
    }

    @Override
    public void execute() {
        good.setOrdered(false);
        good.setAdvance(null);
        view.getUi().getStockService().updateGood(good);
    }
}
