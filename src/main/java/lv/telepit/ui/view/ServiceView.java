package lv.telepit.ui.view;

import com.google.common.base.Strings;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import lv.telepit.TelepitUI;
import lv.telepit.backend.criteria.ServiceGoodCriteria;
import lv.telepit.model.Category;
import lv.telepit.model.ServiceGood;
import lv.telepit.ui.component.Hr;
import lv.telepit.ui.form.ServiceGoodForm;
import lv.telepit.ui.form.fields.FieldFactory;
import lv.telepit.ui.view.context.ServiceContext;
import lv.telepit.utils.ExcelUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.vaadin.dialogs.ConfirmDialog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.*;

/**
 * Created by Alex on 21/02/14.
 */
public class ServiceView extends AbstractView {

    private Button addGood;
    private Button updateGood;
    private Button deleteGood;
    private Button xlsButton;
    private TextField idField;
    private TextField nameField;
    private TextField imeiField;
    private TextField accumNumField;
    private ComboBox userField;
    private ComboBox storeField;
    private ComboBox statusField;
    private ComboBox categoryField;
    private DateField deliveredField;
    private DateField returnedField;
    private Button expandButton;
    private Button searchButton;
    private Button refreshButton;
    private Table table;
    private BeanItemContainer<ServiceGood> container;
    private Label label;
    private Window subWindow;

    public ServiceView(Navigator navigator, TelepitUI ui, String name) {
        super(navigator, ui, name);

    }

    @Override
    public void buildContent() {
        label = new Label(bundle.getString("service.view.label"));
        label.setContentMode(ContentMode.HTML);

        idField = FieldFactory.getTextField("search.service.id");
        nameField = FieldFactory.getTextField("search.service.name");
        imeiField = FieldFactory.getTextField("search.service.imei");
        accumNumField = FieldFactory.getTextField("search.service.accumNum");
        userField = FieldFactory.getUserComboBox("search.user");
        storeField = FieldFactory.getStoreComboBox("search.store");
        statusField = FieldFactory.getStatusComboBox("search.status");
        categoryField = FieldFactory.getCategoryComboBox("search.category");
        deliveredField = FieldFactory.getDateField("search.service.deliveredDate");
        returnedField = FieldFactory.getDateField("search.service.returnedDate");

        searchButton = new Button(bundle.getString("default.button.search"));
        searchButton.addClickListener(new SearchListener());
        searchButton.setIcon(new ThemeResource("img/search.png"));

        refreshButton = new Button();
        refreshButton.addClickListener(new RefreshListener());
        refreshButton.setIcon(new ThemeResource("img/refresh.png"));

        xlsButton = new Button(bundle.getString("excel.export"));
        xlsButton.setIcon(new ThemeResource("img/excel.png"));
        xlsButton.setWidth("150");
        StreamResource excelStream = getExcelStream();
        excelStream.setMIMEType("application/vnd.ms-excel");
        FileDownloader excelDownloader = new FileDownloader(excelStream);
        excelDownloader.extend(xlsButton);

        container = new BeanItemContainer<>(ServiceGood.class);
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
        table.setWidth("1200px");
        table.setContainerDataSource(container);
        table.setVisibleColumns("id", "store", "category", "name", "status", "accumNum", "problem", "price", "deliveredDate", "returnedDate", "contactName", "contactPhone");
        table.setColumnHeaders(bundle.getString("service.good.id"),
                bundle.getString("service.good.store"),
                bundle.getString("service.good.category"),
                bundle.getString("service.good.name"),
                bundle.getString("service.good.status"),
                bundle.getString("service.good.accumNum"),
                bundle.getString("service.good.problem"),
                bundle.getString("service.good.price"),
                bundle.getString("service.good.deliveredDate"),
                bundle.getString("service.good.returnedDate"),
                bundle.getString("service.good.contactName"),
                bundle.getString("service.good.contactPhone"));
        table.setCellStyleGenerator(new Table.CellStyleGenerator() {
            @Override
            public String getStyle(Table source, Object itemId, Object propertyId) {
                ServiceGood sg = container.getItem(itemId).getBean();
                switch (sg.getStatus()) {

                    case WAITING:
                        return "waiting";
                    case IN_REPAIR:
                        return "in-repair";
                    case REPAIRED:
                    case BROKEN:
                        return "finished";
                    case RETURNED:
                    case ON_DETAILS:
                        return "returned";
                }
                return "";
            }
        });
        table.setSelectable(true);
        table.setImmediate(true);
        table.addItemClickListener(new EditServiceGoodListener());
        table.addValueChangeListener(new EditServiceGoodListener());
        table.addActionHandler(new ServiceContext(this));

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

        final HorizontalLayout searchLayout1 = new HorizontalLayout(userField, storeField, categoryField, statusField);
        searchLayout1.setSpacing(true);
        final HorizontalLayout searchLayout2 = new HorizontalLayout(idField, nameField, imeiField, accumNumField, deliveredField, returnedField);
        searchLayout2.setSpacing(true);

        final VerticalLayout searchLayout = new VerticalLayout(new Hr(), searchLayout1, searchLayout2, searchButton, new Hr());
        searchLayout.setSpacing(true);
        searchLayout.setVisible(false);

        expandButton = new Button(bundle.getString("show.hide.search"));
        expandButton.setStyleName(Reindeer.BUTTON_LINK);
        expandButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                searchLayout.setVisible(!searchLayout.isVisible());
            }
        });

        final HorizontalLayout buttonLayout = new HorizontalLayout(addGood, updateGood/*, deleteGood*/, xlsButton, refreshButton);
        buttonLayout.setSpacing(true);
        buttonLayout.setWidth("1200px");
        buttonLayout.setExpandRatio(refreshButton, 1.0f);
        buttonLayout.setComponentAlignment(refreshButton, Alignment.BOTTOM_RIGHT);

        content.addComponent(label);
        content.addComponent(expandButton);
        content.addComponent(searchLayout);
        content.addComponent(buttonLayout);
        content.addComponent(table);
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
            serviceGoods = ui.getServiceGoodService().findGoods(buildMap());
        }
        container.removeAllItems();
        container.addAll(serviceGoods);
        table.refreshRowCache();

        idField.setValue(null);
        nameField.setValue(null);
        imeiField.setValue(null);
        accumNumField.setValue(null);
        userField.setValue(null);
        storeField.setValue(null);
        statusField.setValue(null);
        categoryField.setValue(null);
        deliveredField.setValue(null);
        returnedField.setValue(null);
    }

    @Override
    public void checkAuthority() {
        if (!ui.getCurrentUser().isAdmin()) {
            storeField.setVisible(false);
            userField.setVisible(false);
            deleteGood.setVisible(false);
        }
    }

    private StreamResource getExcelStream() {
        StreamResource.StreamSource source = new StreamResource.StreamSource() {
            @Override
            public InputStream getStream() {
                try {
                    ExcelUtils excelUtils = new ExcelUtils();
                    excelUtils.serviceGoodsToExcel(ui.getServiceGoodService().findGoods(buildMap()));
                    excelUtils.close();
                    return new ByteArrayInputStream(excelUtils.getOutputStream().toByteArray());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        return new StreamResource(source, createReportName("xls"));
    }

    private String createReportName(String extension) {
        StringBuilder builder = new StringBuilder();
        builder.append("service");
        builder.append(new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()));
        builder.append("." + extension);
        return builder.toString();
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
            subWindow.setHeight("800px");
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
            List<ServiceGood> list = ui.getServiceGoodService().findGoods(buildMap());
            refreshView(list);
        }
    }

    private Map<ServiceGoodCriteria, Object> buildMap() {
        Map<ServiceGoodCriteria, Object> map = new HashMap<>();
        if (!Strings.isNullOrEmpty(idField.getValue())) {
            map.put(ServiceGoodCriteria.ID, idField.getValue().trim().toLowerCase());
        }
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
        if (categoryField.getValue() != null) {
            map.put(ServiceGoodCriteria.CATEGORY, ((Category) categoryField.getValue()).getAllIds());
        }
        if (statusField.getValue() != null) {
            map.put(ServiceGoodCriteria.STATUS, statusField.getValue());
        }
        if (deliveredField.getValue() != null) {
            map.put(ServiceGoodCriteria.DELIVERED_DATE_FROM, DateUtils.truncate(deliveredField.getValue(), Calendar.DATE));
        }
        if (returnedField.getValue() != null) {
            map.put(ServiceGoodCriteria.RETURNED_DATE_FROM, DateUtils.truncate(returnedField.getValue(), Calendar.DATE));
        }

        if (!ui.getCurrentUser().isAdmin()) {
            map.put(ServiceGoodCriteria.STORE, ui.getCurrentUser().getStore());
        }
        return map;
    }
}
