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
import lv.telepit.model.ServiceGood;
import lv.telepit.model.User;
import lv.telepit.ui.component.PropertyFilter;
import lv.telepit.ui.form.ServiceGoodForm;
import lv.telepit.ui.form.UserForm;
import org.vaadin.dialogs.ConfirmDialog;

import javax.xml.ws.Service;
import java.util.List;

/**
 * Created by Alex on 21/02/14.
 */
public class ServiceView extends AbstractView {

    private Button addGood;
    private Button updateGood;
    private Button deleteGood;

    private TextField nameField;
    private TextField imeiField;
    private TextField accumNumField;
    private ComboBox userField;
    private ComboBox storeField;
    private DateField deliveredField;
    private DateField returnedField;
    private Button searchButton;
    private Button refreshButton;
    private Table table;
    private BeanItemContainer<ServiceGood> container;
    private Label label;
    private Window subWindow;

    public ServiceView(Navigator navigator, TelepitUI ui) {
        super(navigator, ui);

    }

    @Override
    public void buildContent() {
        label = new Label(bundle.getString("service.view.label"));
        label.setContentMode(ContentMode.HTML);

        nameField = new TextField(bundle.getString("service.good.name"));

        refreshButton = new Button(bundle.getString("default.button.refresh"));
        refreshButton.addClickListener(new RefreshListener());
        refreshButton.setIcon(new ThemeResource("img/refresh.png"));

        container = new BeanItemContainer<>(ServiceGood.class);
        table = new Table();
        table.setImmediate(true);
        table.setContainerDataSource(container);
        table.setVisibleColumns("store", "name", "imei", "accumNum", "problem", "price", "deliveredDate", "returnedDate", "contactName", "contactPhone");
        table.setColumnHeaders(bundle.getString("service.good.store"),
                bundle.getString("service.good.name"),
                bundle.getString("service.good.imei"),
                bundle.getString("service.good.accumNum"),
                bundle.getString("service.good.problem"),
                bundle.getString("service.good.price"),
                bundle.getString("service.good.deliveredDate"),
                bundle.getString("service.good.returnedDate"),
                bundle.getString("service.good.contactName"),
                bundle.getString("service.good.contactPhone"));
        table.setSelectable(true);
        table.setImmediate(true);
        table.addItemClickListener(new EditServiceGoodListener());
        table.addValueChangeListener(new EditServiceGoodListener());

        addGood = new Button(bundle.getString("default.button.add"));
        addGood.setIcon(new ThemeResource("img/add.png"));
        addGood.setWidth("150");
        addGood.addClickListener(new EditServiceGoodListener());

        updateGood = new Button(bundle.getString("default.button.update"));
        updateGood.setIcon(new ThemeResource("img/update.png"));
        updateGood.setWidth("150");
        updateGood.setEnabled(false);
        updateGood.addClickListener(new EditServiceGoodListener());

        deleteGood = new Button(bundle.getString("default.button.delete"));
        deleteGood.setIcon(new ThemeResource("img/delete.png"));
        deleteGood.setWidth("150");
        deleteGood.setEnabled(false);
        deleteGood.addClickListener(new EditServiceGoodListener());



        final HorizontalLayout buttonLayout = new HorizontalLayout(addGood, updateGood, deleteGood);
        buttonLayout.setSpacing(true);

        content.addComponent(label);
        content.addComponent(refreshButton);
        content.addComponent(buttonLayout);
        content.addComponent(table);

        refreshView();

    }

    @Override
    public void refreshView() {

        ui.removeWindow(subWindow);
        updateGood.setEnabled(false);
        deleteGood.setEnabled(false);

        List<ServiceGood> goods = ui.getServiceGoodService().getAllGoods();
        container.removeAllItems();

        container.addAll(goods);
        table.refreshRowCache();

    }

    private class EditServiceGoodListener implements Button.ClickListener, ItemClickEvent.ItemClickListener, Property.ValueChangeListener {
        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {
            if (clickEvent.getButton() == addGood) {
                openServiceGoodForm(new BeanItem<>(new ServiceGood()));
            } else if (clickEvent.getButton() == updateGood && table.getValue() != null){
                openServiceGoodForm(container.getItem(table.getValue()));
            } else if (clickEvent.getButton() == deleteGood && table.getValue() != null) {
                final ServiceGood goodToDelete = container.getItem(table.getValue()).getBean();
                ConfirmDialog.show(ui,
                        bundle.getString("service.view.delete.header"),
                        bundle.getString("service.view.delete.message"),
                        bundle.getString("default.button.ok"), bundle.getString("default.button.cancel"), new ConfirmDialog.Listener() {
                    @Override
                    public void onClose(ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            ui.getServiceGoodService().deleteGood(goodToDelete);
                            refreshView();
                        }
                    }
                });
            }
        }

        @Override
        public void itemClick(ItemClickEvent event) {

            if (event.isDoubleClick() && event.getItem() != null) {
                openServiceGoodForm((BeanItem<ServiceGood>) event.getItem());
            }
        }

        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            updateGood.setEnabled(table.getValue() != null);
            deleteGood.setEnabled(table.getValue() != null);
        }

        private void openServiceGoodForm(BeanItem<ServiceGood> serviceGood) {

            subWindow = new Window();
            subWindow.setModal(true);
            subWindow.setHeight("650px");
            subWindow.setWidth("450px");
            subWindow.setClosable(true);
            ui.addWindow(subWindow);

            final VerticalLayout layout = new VerticalLayout();
            layout.setMargin(true);
            layout.setSpacing(true);

            layout.addComponent(new ServiceGoodForm(serviceGood, ServiceView.this));

            subWindow.setContent(layout);
        }
    }

    private class RefreshListener implements Button.ClickListener {
        @Override
        public void buttonClick(Button.ClickEvent event) {
            refreshView();
        }
    }
}
