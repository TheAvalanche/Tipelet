package lv.telepit.ui.view.context.actions;

import lv.telepit.model.StockGood;
import lv.telepit.ui.view.AbstractView;

public class StockAttentionAction extends AbstractAction {

    private final StockGood good;

    public StockAttentionAction(StockGood good, AbstractView view) {
        super(view);

        if (good.isAttention()) {
            this.setCaption(bundle.getString("unattention.item"));
        } else {
            this.setCaption(bundle.getString("attention.item"));
        }

        this.good = good;
    }

    @Override
    public boolean show() {
        return !good.isOrdered();
    }

    @Override
    public void execute() {
        good.setAttention(!good.isAttention());
        view.getUi().getStockService().updateGood(good);
    }
}
