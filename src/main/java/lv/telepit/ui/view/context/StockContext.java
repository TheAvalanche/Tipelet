package lv.telepit.ui.view.context;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.Action;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.shared.ui.slider.SliderOrientation;
import com.vaadin.ui.*;
import lv.telepit.model.ChangeRecord;
import lv.telepit.model.SoldItem;
import lv.telepit.model.StockGood;
import lv.telepit.ui.component.Hr;
import lv.telepit.ui.form.fields.FieldFactory;
import lv.telepit.ui.view.StockView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Alex on 12/05/2014.
 */
public class StockContext implements Action.Handler {

    private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");

    private final Action checkAsBestseller = new Action(bundle.getString("popular.item"));
    private final Action uncheckAsBestseller = new Action(bundle.getString("unpopular.item"));
    private final Action sell = new Action(bundle.getString("sell.item"));
    private final Action changeShop = new Action(bundle.getString("change.shop.item"));
    private final Action showHistory = new Action(bundle.getString("show.history"));

    private StockView view;

    public StockContext(StockView view) {
        this.view = view;
    }

    @Override
    public Action[] getActions(Object target, Object sender) {
        if (target == null) return new Action[]{};

        if (((StockGood) target).isBestseller()) {
            return new Action[]{sell, uncheckAsBestseller, changeShop, showHistory};
        } else {
            return new Action[]{sell, checkAsBestseller, changeShop, showHistory};
        }
    }

    @Override
    public void handleAction(Action action, Object sender, Object target) {
         if (action == sell) {
             showSell((StockGood) target);
             view.refreshView();
         } else if (action == checkAsBestseller) {
             StockGood good = (StockGood) target;
             good.setBestseller(true);
             view.getUi().getStockService().updateGood(good);
             view.refreshView();
         } else if (action == uncheckAsBestseller) {
             StockGood good = (StockGood) target;
             good.setBestseller(false);
             view.getUi().getStockService().updateGood(good);
             view.refreshView();
         } else if (action == showHistory) {
             showHistory((StockGood) target);
             view.refreshView();
         } else if (action == changeShop) {
             changeShop((StockGood) target);
             view.refreshView();
         }
    }

    private void showSell(final StockGood stockGood) {
        final Window subWindow = new Window();
        subWindow.setModal(true);
        subWindow.setHeight("500px");
        subWindow.setWidth("500px");
        subWindow.setClosable(true);
        view.getUi().addWindow(subWindow);

        final VerticalLayout layout = new VerticalLayout();
        layout.setWidth("100%");
        layout.setMargin(true);
        layout.setSpacing(true);

        final VerticalLayout subLayout = new VerticalLayout();
        subLayout.setWidth("100%");
        subLayout.setSpacing(true);

        final List<SoldItem> soldItems = new ArrayList<>();
        addSoldItem(soldItems, subLayout);

        Button addButton = new Button(bundle.getString("default.button.add"));
        addButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (soldItems.size() < stockGood.getCount()) {
                    addSoldItem(soldItems, subLayout);
                } else {
                    Notification.show(bundle.getString("add.fail"));
                }
            }
        });

        Button sellButton = new Button(bundle.getString("sell.item"));
        sellButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                view.getUi().getStockService().sell(stockGood, soldItems);
                Notification.show(bundle.getString("save.success"));
                subWindow.close();
                view.refreshView();
            }
        });

        HorizontalLayout buttonLayout = new HorizontalLayout(addButton, sellButton);
        buttonLayout.setWidth("100%");
        buttonLayout.setComponentAlignment(sellButton, Alignment.BOTTOM_RIGHT);

        layout.addComponent(subLayout);
        layout.addComponent(new Hr());
        layout.addComponent(buttonLayout);

        subWindow.setContent(layout);
    }

    private void addSoldItem(final List<SoldItem> soldItems, final Layout layout) {
        final SoldItem item = new SoldItem();
        item.setUser(view.getUi().getCurrentUser());
        item.setStore(view.getUi().getCurrentUser().getStore());
        soldItems.add(item);

        BeanItem<SoldItem> beanItem = new BeanItem<>(item);

        TextField codeField = new TextField("Code", beanItem.getItemProperty("code"));
        codeField.setImmediate(true);
        codeField.setWidth(200f, Sizeable.Unit.PIXELS);
        codeField.setNullRepresentation("");
        CheckBox billField = new CheckBox("Ar čeku", beanItem.getItemProperty("withBill"));
        billField.setImmediate(true);

        Button deleteButton = new Button(bundle.getString("default.button.delete"));
        deleteButton.setStyleName("small");
        if (soldItems.size() == 1) {
            deleteButton.setEnabled(false);
        }

        final HorizontalLayout subLayout = new HorizontalLayout(codeField, billField, deleteButton);
        subLayout.setWidth("100%");
        subLayout.setSpacing(true);
        subLayout.setComponentAlignment(billField, Alignment.BOTTOM_CENTER);
        subLayout.setComponentAlignment(deleteButton, Alignment.BOTTOM_RIGHT);

        deleteButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                soldItems.remove(item);
                layout.removeComponent(subLayout);
            }
        });

        layout.addComponent(subLayout);

    }

    private void changeShop(final StockGood stockGood) {
        final Window subWindow = new Window();
        subWindow.setModal(true);
        subWindow.setHeight("500px");
        subWindow.setWidth("500px");
        subWindow.setClosable(true);
        view.getUi().addWindow(subWindow);

        VerticalLayout layout = new VerticalLayout();

        final ComboBox storeField = FieldFactory.getStoreComboBox("search.store");

        final Slider slider = new Slider(bundle.getString("item.count"), 0, stockGood.getCount());
        slider.setOrientation(SliderOrientation.HORIZONTAL);
        slider.setWidth("200px");

        Button moveButton = new Button(bundle.getString("default.button.move"));
        moveButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {

            }
        });

        layout.addComponent(storeField);
        layout.addComponent(slider);
        layout.addComponent(new Hr());
        layout.addComponent(moveButton);

        subWindow.setContent(layout);
    }

    private void showHistory(StockGood stockGood) {

        List<ChangeRecord> historyList = view.getUi().getStockService().findChanges(stockGood);
        Window subWindow = new Window();
        subWindow.setModal(true);
        subWindow.setHeight("650px");
        subWindow.setWidth("700px");
        subWindow.setClosable(true);
        view.getUi().addWindow(subWindow);

        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        for (ChangeRecord record : historyList) {
            for (ChangeRecord.PropertyChange p : record.getChangeList()) {
                p.setName(bundle.getString(p.getName()));
            }
            Panel panel = new Panel();
            BeanItemContainer<ChangeRecord.PropertyChange> container = new BeanItemContainer<>(ChangeRecord.PropertyChange.class, record.getChangeList());
            Table table = new Table();
            table.setContainerDataSource(container);
            table.setVisibleColumns("name", "oldValue", "newValue");
            table.setColumnHeaders(bundle.getString("history.property"), bundle.getString("history.old"), bundle.getString("history.new"));
            table.setColumnExpandRatio("name", 0.33f);
            table.setColumnExpandRatio("oldValue", 0.33f);
            table.setColumnExpandRatio("newValue", 0.33f);
            table.setPageLength(0);
            table.setWidth("100%");

            VerticalLayout panelLayout = new VerticalLayout();
            panelLayout.addComponent(new Label("<b>" + new SimpleDateFormat("dd-MM-YYYY HH:mm").format(record.getDate())
                    + ": " + record.getUser().getName() + " "
                    + record.getUser().getSurname() + "</b><br/>", ContentMode.HTML));
            panelLayout.addComponent(table);
            panel.setContent(panelLayout);
            layout.addComponent(panel);
        }

        subWindow.setContent(layout);
    }
}
