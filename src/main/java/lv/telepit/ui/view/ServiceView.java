package lv.telepit.ui.view;

import com.google.common.base.Strings;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import lv.telepit.TelepitUI;
import lv.telepit.backend.criteria.ServiceGoodCriteria;
import lv.telepit.model.ServiceGood;
import lv.telepit.ui.form.ServiceGoodForm;
import lv.telepit.ui.form.fields.FieldFactory;
import org.vaadin.dialogs.ConfirmDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private ComboBox statusField;
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
        imeiField = new TextField(bundle.getString("service.good.imei"));
        accumNumField = new TextField(bundle.getString("service.good.accumNum"));
        userField = FieldFactory.getUserComboBox("user");
        storeField = FieldFactory.getStoreComboBox("store");
        statusField = FieldFactory.getStatusComboBox("status");
        deliveredField = new DateField(bundle.getString("service.good.deliveredDate"));
        deliveredField.setDateFormat("dd.MM.yyyy");
        returnedField = new DateField(bundle.getString("service.good.returnedDate"));
        returnedField.setDateFormat("dd.MM.yyyy");

        searchButton = new Button(bundle.getString("default.button.search"));
        searchButton.addClickListener(new SearchListener());
        searchButton.setIcon(new ThemeResource("img/search.png"));

        refreshButton = new Button(bundle.getString("default.button.refresh"));
        refreshButton.addClickListener(new RefreshListener());
        refreshButton.setIcon(new ThemeResource("img/refresh.png"));

        container = new BeanItemContainer<>(ServiceGood.class);
        table = new Table() {
            @Override
            protected String formatPropertyValue(Object rowId, Object colId, Property property) {
                Object v = property.getValue();
                if (v instanceof Date) {
                Date dateValue = (Date) v;
                    return new SimpleDateFormat("dd.MM.yyyy").format(dateValue);
                }
                return super.formatPropertyValue(rowId, colId, property);
            }
        };
        table.setImmediate(true);
        table.setContainerDataSource(container);
        table.setVisibleColumns("store", "name", "status","imei", "accumNum", "problem", "price", "deliveredDate", "returnedDate", "contactName", "contactPhone");
        table.setColumnHeaders(bundle.getString("service.good.store"),
                bundle.getString("service.good.name"),
                bundle.getString("service.good.status"),
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

        final HorizontalLayout searchLayout1 = new HorizontalLayout(userField, storeField, statusField);
        searchLayout1.setSpacing(true);
        final HorizontalLayout searchLayout2 = new HorizontalLayout(nameField, imeiField, accumNumField, deliveredField, returnedField, searchButton);
        searchLayout2.setSpacing(true);
        searchLayout2.setComponentAlignment(searchButton, Alignment.BOTTOM_RIGHT);

        final HorizontalLayout buttonLayout = new HorizontalLayout(addGood, updateGood, deleteGood);
        buttonLayout.setSpacing(true);

        content.addComponent(label);
        content.addComponent(searchLayout1);
        content.addComponent(searchLayout2);
        content.addComponent(refreshButton);
        content.addComponent(buttonLayout);
        content.addComponent(table);

        refreshView();

    }

    @Override
    public void refreshView() {
        refreshView(null);
    }

    public void refreshView(List<ServiceGood> serviceGoods) {
        ui.removeWindow(subWindow);
        updateGood.setEnabled(false);
        deleteGood.setEnabled(false);

        if (serviceGoods == null) {
            serviceGoods = ui.getServiceGoodService().getAllGoods();
        }
        container.removeAllItems();
        container.addAll(serviceGoods);
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

    private class SearchListener implements Button.ClickListener {
        @Override
        public void buttonClick(Button.ClickEvent event) {
            Map<ServiceGoodCriteria, Object> map = new HashMap<>();
            if (!Strings.isNullOrEmpty(nameField.getValue())) {
                map.put(ServiceGoodCriteria.NAME, nameField.getValue().trim().toLowerCase());
            }
            if (!Strings.isNullOrEmpty(imeiField.getValue())) {
                map.put(ServiceGoodCriteria.IMEI, imeiField.getValue().trim().toLowerCase());
            }
            if (!Strings.isNullOrEmpty(accumNumField.getValue())) {
                map.put(ServiceGoodCriteria.ACCUM_NUM, accumNumField.getValue().trim().toLowerCase());
            }
            if (userField.getValue() != null) {
                map.put(ServiceGoodCriteria.USER, userField.getValue());
            }
            if (storeField.getValue() != null) {
                map.put(ServiceGoodCriteria.STORE, storeField.getValue());
            }
            if (statusField.getValue() != null) {
                map.put(ServiceGoodCriteria.STATUS, statusField.getValue());
            }
            if (deliveredField.getValue() != null) {
                map.put(ServiceGoodCriteria.DELIVERED_DATE_FROM, deliveredField.getValue());
            }
            if (returnedField.getValue() != null) {
                map.put(ServiceGoodCriteria.RETURNED_DATE_FROM, returnedField.getValue());
            }
            List<ServiceGood> list = ui.getServiceGoodService().findGoods(map);
            refreshView(list);
        }
    }
}
