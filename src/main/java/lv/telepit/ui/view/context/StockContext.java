package lv.telepit.ui.view.context;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.Action;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import lv.telepit.model.ChangeRecord;
import lv.telepit.model.SoldItem;
import lv.telepit.model.StockGood;
import lv.telepit.ui.component.Hr;
import lv.telepit.ui.view.StockView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Alex on 12/05/2014.
 */
public class StockContext implements Action.Handler {

    private final Action sell = new Action("Pārdot");
    private final Action showHistory = new Action("Radīt vēsturi");

    private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");

    private StockView view;

    public StockContext(StockView view) {
        this.view = view;
    }

    @Override
    public Action[] getActions(Object target, Object sender) {
        return new Action[]{sell, showHistory};
    }
    //todo mark good as bestseller
    @Override
    public void handleAction(Action action, Object sender, Object target) {
         if (action == sell) {
            showSell((StockGood) target);
            view.refreshView();
        } else if (action == showHistory) {
            showHistory((StockGood) target);
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

        Button addButton = new Button("Pievienot...");
        addButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (soldItems.size() < stockGood.getCount()) {
                    addSoldItem(soldItems, subLayout);
                } else {
                    Notification.show("Nedrikst pārsniegt pieejamo preču skaitu.");
                }
            }
        });

        Button sellButton = new Button("Pārdot...");
        sellButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                view.getUi().getStockService().sell(stockGood, soldItems);
                Notification.show("Saglabāts.");
                subWindow.close();
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

        Button deleteButton = new Button("Nodzēst");
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
            table.setColumnHeaders("Vertība", "Vecā vertība", "Jaunā vertība");
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
