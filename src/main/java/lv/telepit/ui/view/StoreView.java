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
import lv.telepit.model.Store;
import lv.telepit.ui.component.Hr;
import lv.telepit.ui.form.StoreForm;
import org.vaadin.dialogs.ConfirmDialog;

import java.util.List;

/**
 * Created by Alex on 26/02/14.
 */
public class StoreView extends AbstractView {

    private Button addStore;
    private Button updateStore;
    private Button deleteStore;
    private Table table;
    private BeanItemContainer<Store> container;
    private Label label;
    private Window subWindow;

    public StoreView(Navigator navigator, TelepitUI ui, String name) {
        super(navigator, ui, name);
    }

    @Override
    public void buildContent() {
        label = new Label(bundle.getString("store.view.label"));
        label.setContentMode(ContentMode.HTML);

        container = new BeanItemContainer<>(Store.class);
        table = new Table();
        table.setWidth("500px");
        table.setImmediate(true);
        table.setContainerDataSource(container);
        table.setVisibleColumns("name", "city", "address");
        table.setColumnHeaders(bundle.getString("store.name"),
                bundle.getString("store.city"),
                bundle.getString("store.address"));
        table.setSelectable(true);
        table.setImmediate(true);
        table.addItemClickListener(new EditStoreListener());
        table.addValueChangeListener(new EditStoreListener());

        addStore = new Button(bundle.getString("default.button.add"));
        addStore.setIcon(new ThemeResource("img/add.png"));
        addStore.setWidth("150");
        addStore.addClickListener(new EditStoreListener());

        updateStore = new Button(bundle.getString("default.button.update"));
        updateStore.setIcon(new ThemeResource("img/update.png"));
        updateStore.setWidth("150");
        updateStore.setEnabled(false);
        updateStore.addClickListener(new EditStoreListener());

        deleteStore = new Button(bundle.getString("default.button.delete"));
        deleteStore.setIcon(new ThemeResource("img/delete.png"));
        deleteStore.setWidth("150");
        deleteStore.setEnabled(false);
        deleteStore.addClickListener(new EditStoreListener());

        final VerticalLayout buttonLayout = new VerticalLayout(addStore, updateStore, deleteStore);
        buttonLayout.setSpacing(true);

        final HorizontalLayout tableButtonsLayout = new HorizontalLayout(table, buttonLayout);
        tableButtonsLayout.setSpacing(true);

        content.addComponent(label);
        content.addComponent(new Hr());
        content.addComponent(tableButtonsLayout);

        refreshView();

    }

    @Override
    public void refreshView() {

        ui.removeWindow(subWindow);
        updateStore.setEnabled(false);
        deleteStore.setEnabled(false);

        List<Store> stores = ui.getStoreService().getAllStores();
        container.removeAllItems();
        container.addAll(stores);
        table.refreshRowCache();
    }

    @Override
    public void checkAuthority() {

    }

    private class EditStoreListener implements Button.ClickListener, ItemClickEvent.ItemClickListener, Property.ValueChangeListener {
        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {
            if (clickEvent.getButton() == addStore) {
                openStoreForm(new BeanItem<>(new Store()));
            } else if (clickEvent.getButton() == updateStore && table.getValue() != null){
                openStoreForm(container.getItem(table.getValue()));
            } else if (clickEvent.getButton() == deleteStore && table.getValue() != null) {
                final Store storeToDelete = container.getItem(table.getValue()).getBean();
                ConfirmDialog.show(ui,
                        bundle.getString("store.view.delete.header"),
                        bundle.getString("store.view.delete.message"),
                        bundle.getString("default.button.ok"), bundle.getString("default.button.cancel"), new ConfirmDialog.Listener() {
                    @Override
                    public void onClose(ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            try {
                                ui.getStoreService().deleteStore(storeToDelete);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            refreshView();
                        }
                    }
                });
            }
        }

        @Override
        public void itemClick(ItemClickEvent event) {

            if (event.isDoubleClick() && event.getItem() != null) {
                openStoreForm((BeanItem<Store>) event.getItem());
            }
        }

        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            updateStore.setEnabled(table.getValue() != null);
            deleteStore.setEnabled(table.getValue() != null);
        }

        private void openStoreForm(BeanItem<Store> store) {

            subWindow = new Window();
            subWindow.setModal(true);
            subWindow.setHeight("400px");
            subWindow.setWidth("450px");
            subWindow.setClosable(true);
            ui.addWindow(subWindow);

            final VerticalLayout layout = new VerticalLayout();
            layout.setMargin(true);
            layout.setSpacing(true);

            layout.addComponent(new StoreForm(store, StoreView.this));

            subWindow.setContent(layout);
        }
    }
}
