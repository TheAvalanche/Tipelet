package lv.telepit.ui.view;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import lv.telepit.TelepitUI;
import lv.telepit.model.StockGood;
import lv.telepit.ui.form.ServiceGoodForm;
import lv.telepit.ui.form.StockGoodForm;
import org.vaadin.dialogs.ConfirmDialog;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Alex on 21/02/14.
 */
public class StockView extends AbstractView {

    private Button addGood;
    private Button updateGood;
    private Button deleteGood;

    private Label label;
    private Button refreshButton;
    private Table table;
    private BeanItemContainer<StockGood> container;

    private Window subWindow;

    public StockView(Navigator navigator, TelepitUI ui, String name) {
        super(navigator, ui, name);
    }

    @Override
    public void buildContent() {
        label = new Label(bundle.getString("stock.view.label"));
        label.setContentMode(ContentMode.HTML);

        refreshButton = new Button();
        refreshButton.addClickListener(new RefreshListener());
        refreshButton.setIcon(new ThemeResource("img/refresh.png"));

        container = new BeanItemContainer<>(StockGood.class);
        table = new Table() {
            @Override
            protected String formatPropertyValue(Object rowId, Object colId, Property property) {
                Object v = property.getValue();
                if (v instanceof Date) {
                    Date dateValue = (Date) v;
                    return new SimpleDateFormat("dd.MM.yyyy").format(dateValue);
                } else if (v instanceof Double) {
                    Double doubleValue = (Double) v;
                    return String.format("%.2f", doubleValue);
                }
                return super.formatPropertyValue(rowId, colId, property);
            }
        };
        table.setImmediate(true);
        table.setWidth("1000px");
        table.setContainerDataSource(container);
        table.setVisibleColumns("store", "category", "name", "price", "count", "lastSoldDate");
        table.setColumnHeaders(bundle.getString("stock.good.store"),
                bundle.getString("stock.good.category"),
                bundle.getString("stock.good.name"),
                bundle.getString("stock.good.price"),
                bundle.getString("stock.good.count"),
                bundle.getString("stock.good.lastSoldDate"));
        table.setSelectable(true);
        table.setImmediate(true);
        table.addItemClickListener(new EditStockGoodListener());
        table.addValueChangeListener(new EditStockGoodListener());


        addGood = new Button(bundle.getString("default.button.add"));
        addGood.setIcon(new ThemeResource("img/add.png"));
        addGood.setWidth("150");
        addGood.addClickListener(new EditStockGoodListener());

        updateGood = new Button(bundle.getString("default.button.update"));
        updateGood.setIcon(new ThemeResource("img/update.png"));
        updateGood.setWidth("150");
        updateGood.setEnabled(false);
        updateGood.addClickListener(new EditStockGoodListener());

        deleteGood = new Button(bundle.getString("default.button.delete"));
        deleteGood.setIcon(new ThemeResource("img/delete.png"));
        deleteGood.setWidth("150");
        deleteGood.setEnabled(false);
        deleteGood.addClickListener(new EditStockGoodListener());

    }

    @Override
    public void refreshView() {

    }

    @Override
    public void checkAuthority() {

    }

    private class RefreshListener implements Button.ClickListener {
        @Override
        public void buttonClick(Button.ClickEvent event) {
            refreshView();
        }
    }

    private class EditStockGoodListener implements Button.ClickListener, ItemClickEvent.ItemClickListener, Property.ValueChangeListener {
        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {
            if (clickEvent.getButton() == addGood) {
                openStockGoodForm(new BeanItem<>(new StockGood()));
            } else if (clickEvent.getButton() == updateGood && table.getValue() != null){
                openStockGoodForm(container.getItem(table.getValue()));
            } else if (clickEvent.getButton() == deleteGood && table.getValue() != null) {
                final StockGood goodToDelete = container.getItem(table.getValue()).getBean();
                ConfirmDialog.show(ui,
                        bundle.getString("stock.view.delete.header"),
                        bundle.getString("stock.view.delete.message"),
                        bundle.getString("default.button.ok"), bundle.getString("default.button.cancel"), new ConfirmDialog.Listener() {
                            @Override
                            public void onClose(ConfirmDialog dialog) {
                                if (dialog.isConfirmed()) {
                                    ui.getStockService().deleteGood(goodToDelete);
                                    refreshView();
                                }
                            }
                        }
                );
            }
        }

        @Override
        public void itemClick(ItemClickEvent event) {

            if (event.isDoubleClick() && event.getItem() != null) {
                openStockGoodForm((BeanItem<StockGood>) event.getItem());
            }
        }

        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            updateGood.setEnabled(table.getValue() != null);
            deleteGood.setEnabled(table.getValue() != null);
        }

        private void openStockGoodForm(BeanItem<StockGood> serviceGood) {

            subWindow = new Window();
            subWindow.setModal(true);
            subWindow.setHeight("650px");
            subWindow.setWidth("450px");
            subWindow.setClosable(true);
            ui.addWindow(subWindow);

            final VerticalLayout layout = new VerticalLayout();
            layout.setMargin(true);
            layout.setSpacing(true);

            layout.addComponent(new StockGoodForm(serviceGood, StockView.this));

            subWindow.setContent(layout);
        }
    }
}
