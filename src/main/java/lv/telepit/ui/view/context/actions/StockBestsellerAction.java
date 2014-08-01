package lv.telepit.ui.view.context.actions;

import lv.telepit.model.StockGood;
import lv.telepit.ui.view.AbstractView;

public class StockBestsellerAction extends AbstractAction {
    private final StockGood good;

    public StockBestsellerAction(StockGood good, AbstractView view) {
        super(view);

        if (good.isBestseller()) {
            this.setCaption(bundle.getString("unpopular.item"));
        } else {
            this.setCaption(bundle.getString("popular.item"));
        }

        this.good = good;
    }

    @Override
    public boolean show() {
        return !good.isOrdered();
    }

    @Override
    public void execute() {
        good.setBestseller(!good.isBestseller());
        view.getUi().getStockService().updateGood(good);
    }
}
