package lv.telepit.ui.view.context.actions;

import com.vaadin.ui.*;
import lv.telepit.model.StockGood;
import lv.telepit.model.Store;
import lv.telepit.ui.form.fields.FieldFactory;
import lv.telepit.ui.view.AbstractView;

public class StockMoveAction extends AbstractAction {

    private final StockGood good;

    public StockMoveAction(StockGood good, AbstractView view) {
        super(view);
        this.setCaption(bundle.getString("move.item"));
        this.good = good;
    }

    @Override
    public boolean show() {
        return !good.isOrdered() && good.getCount() > 0 && view.getUi().getCurrentUser().isAdmin();
    }

    @Override
    public void execute() {

        final Window subWindow = new Window();
        subWindow.setModal(true);
        subWindow.setHeight("300px");
        subWindow.setWidth("400px");
        subWindow.setClosable(true);
        view.getUi().addWindow(subWindow);

        final VerticalLayout layout = new VerticalLayout();
        layout.setWidth("100%");
        layout.setMargin(true);
        layout.setSpacing(true);

        final Slider slider = new Slider("Daudzums", 0, good.getCount());
        slider.setWidth("100%");
        final ComboBox stores = FieldFactory.getStoreComboBox("search.store");
        layout.addComponent(slider);
        layout.addComponent(stores);

        final Button move = new Button("Pārcelt");
        move.addClickListener((Button.ClickListener) event -> {
            if (slider.getValue().equals(0.0)) {
                Notification.show("Nav ko pārcelt");
                return;
            }
            if (stores.getValue() == null) {
                Notification.show("Veikals nav izvelēts");
                return;
            }
            if (stores.getValue().equals(good.getStore())) {
                Notification.show("Izvelies citu veikalu");
                return;
            }
            view.getUi().getStockService().moveToStore(good, slider.getValue().intValue(), (Store) stores.getValue());
            subWindow.close();
            view.refreshView();
            Notification.show(bundle.getString("save.success"));
        });
        layout.addComponent(move);

        subWindow.setContent(layout);
    }
}
