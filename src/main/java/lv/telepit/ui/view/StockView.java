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
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import lv.telepit.TelepitUI;
import lv.telepit.backend.criteria.StockGoodCriteria;
import lv.telepit.model.Category;
import lv.telepit.model.StockGood;
import lv.telepit.model.User;
import lv.telepit.ui.component.CommonTable;
import lv.telepit.ui.component.Hr;
import lv.telepit.ui.form.OrderStockGoodForm;
import lv.telepit.ui.form.StockGoodForm;
import lv.telepit.ui.form.fields.FieldFactory;
import lv.telepit.ui.view.context.StockContext;
import lv.telepit.utils.ExcelUtils;
import org.vaadin.dialogs.ConfirmDialog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockView extends AbstractView {

    private Button addGood;
    private Button orderGood;
    private Button updateGood;
    private Button deleteGood;
    private Button xlsButton;

    private TextField idField;
    private TextField priceField;
    private TextField nameField;
    private TextField modelField;
    private ComboBox userField;
    private ComboBox storeField;
    private ComboBox categoryField;
    private Button expandButton;
    private Button searchButton;
    private Button resetButton;

    private Label label;
    private Button refreshButton;
    private Table table;
    private BeanItemContainer<StockGood> container;

    private Window subWindow;
    private ResetListener resetListener;

    public StockView(Navigator navigator, TelepitUI ui, String name) {
        super(navigator, ui, name);
    }

    @Override
    public void buildContent() {
        label = new Label(bundle.getString("stock.view.label"));
        label.setContentMode(ContentMode.HTML);
        label.setStyleName("compact-header");

        idField = FieldFactory.getTextField("search.stock.id");
        priceField = FieldFactory.getTextField("search.stock.price");
        nameField = FieldFactory.getTextField("search.stock.name");
        modelField = FieldFactory.getTextField("search.stock.model");
        userField = FieldFactory.getUserComboBox("search.user");
        storeField = FieldFactory.getStoreComboBox("search.store");
        categoryField = FieldFactory.getCategoryComboBox("search.category");

        searchButton = new Button(bundle.getString("default.button.search"));
        searchButton.addClickListener(new SearchListener());
        searchButton.setIcon(new ThemeResource("img/search.png"));

        resetButton = new Button(bundle.getString("default.button.reset"));
        resetListener = new ResetListener();
        resetButton.addClickListener(resetListener);
        resetButton.setIcon(new ThemeResource("img/reset.png"));

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

        container = new BeanItemContainer<>(StockGood.class);
        table = new CommonTable(container, "stock.good", "incrementId", "store", "category", "name", "model", "compatibleModels", "price", "count", "total", "lastSoldDate");
        table.setCellStyleGenerator((Table.CellStyleGenerator) (source, itemId, propertyId) -> {
            StockGood sg = container.getItem(itemId).getBean();
            if (sg.isOrdered()) {
                return "ordered";
            } else if (sg.isAttention()) {
                return "attention";
            } else if (sg.getCount() == 0) {
                return "sold";
            } else if (sg.isBestseller()) {
                return "bestseller";
            }
            return "";
        });
        table.addItemClickListener(new EditStockGoodListener());
        table.addValueChangeListener(new EditStockGoodListener());
        table.addActionHandler(new StockContext(this));


        addGood = new Button(bundle.getString("default.button.add"));
        addGood.setIcon(new ThemeResource("img/add.png"));
        addGood.setWidth("150");
        addGood.addClickListener(new EditStockGoodListener());

        orderGood = new Button(bundle.getString("default.button.order"));
        orderGood.setIcon(new ThemeResource("img/add.png"));
        orderGood.setWidth("150");
        orderGood.addClickListener(new EditStockGoodListener());

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

        final HorizontalLayout searchLayout1 = new HorizontalLayout(idField, userField, storeField, categoryField);
        searchLayout1.setSpacing(true);
        final HorizontalLayout searchLayout2 = new HorizontalLayout(nameField, modelField, priceField);
        searchLayout2.setSpacing(true);

        final VerticalLayout searchLayout = new VerticalLayout(new Hr(), searchLayout1, searchLayout2,
                new HorizontalLayout(searchButton, resetButton), new Hr());
        searchLayout.setSpacing(true);
        searchLayout.setVisible(true);

        expandButton = new Button(bundle.getString("show.hide.search"));
        expandButton.setStyleName(Reindeer.BUTTON_LINK);
        expandButton.addClickListener((Button.ClickListener) event -> searchLayout.setVisible(!searchLayout.isVisible()));

        final HorizontalLayout headerLayout = new HorizontalLayout(label, expandButton);
        headerLayout.setSpacing(true);
        headerLayout.setWidth("1200px");
        headerLayout.setComponentAlignment(expandButton, Alignment.BOTTOM_RIGHT);

        final HorizontalLayout buttonLayout = new HorizontalLayout(addGood, updateGood, deleteGood, orderGood, xlsButton, refreshButton);
        buttonLayout.setSpacing(true);
        buttonLayout.setWidth("1200px");
        buttonLayout.setExpandRatio(refreshButton, 1.0f);
        buttonLayout.setComponentAlignment(refreshButton, Alignment.BOTTOM_RIGHT);

        content.addComponent(headerLayout);
        content.addComponent(searchLayout);
        content.addComponent(buttonLayout);
        content.addComponent(table);
        content.setExpandRatio(table, 1.0f);

    }

    @Override
    public void refreshView() {
        refreshView(null);
    }

    public void refreshView(List<StockGood> stockGoods) {

        ui.removeWindow(subWindow);
        updateGood.setEnabled(false);
        deleteGood.setEnabled(false);

        if (stockGoods == null) {
            stockGoods = ui.getStockService().findGoods(buildMap());
        }
        container.removeAllItems();
        container.addAll(stockGoods);
        container.sort(new Object[]{"id"}, new boolean[]{false});
        table.refreshRowCache();
    }

    @Override
    public void checkAuthority() {
        User currentUser = ui.getCurrentUser();
        storeField.setVisible(currentUser.isAdmin());
        userField.setVisible(currentUser.isAdmin());
        addGood.setVisible(currentUser.isAdmin() || currentUser.isAccessToAddInStock());
        updateGood.setVisible(currentUser.isAdmin() || currentUser.isAccessToAddInStock());
        deleteGood.setVisible(currentUser.isAdmin());
        orderGood.setVisible(!currentUser.isAdmin() && !currentUser.isAccessToAddInStock());
    }

    @Override
    public void reset() {
        resetListener.buttonClick(null);
    }

    private StreamResource getExcelStream() {
        StreamResource.StreamSource source = (StreamResource.StreamSource) () -> {
            try {
                ExcelUtils excelUtils = new ExcelUtils();
                excelUtils.stockGoodsToExcel(ui.getStockService().findGoods(buildMap()));
                excelUtils.close();
                return new ByteArrayInputStream(excelUtils.getOutputStream().toByteArray());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        };
        return new StreamResource(source, createReportName("xls"));
    }

    private String createReportName(String extension) {
        StringBuilder builder = new StringBuilder();
        builder.append("stock");
        builder.append(new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()));
        builder.append("." + extension);
        return builder.toString();
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
            } else if (clickEvent.getButton() == orderGood){
                openOrderStockGoodForm(new BeanItem<>(new StockGood()));
            } else if (clickEvent.getButton() == updateGood && table.getValue() != null){
                openStockGoodForm(container.getItem(table.getValue()));
            } else if (clickEvent.getButton() == deleteGood && table.getValue() != null) {
                final StockGood goodToDelete = container.getItem(table.getValue()).getBean();
                ConfirmDialog.show(ui,
                        bundle.getString("stock.view.delete.header"),
                        bundle.getString("stock.view.delete.message"),
                        bundle.getString("default.button.ok"), bundle.getString("default.button.cancel"), (ConfirmDialog.Listener) dialog -> {
                            if (dialog.isConfirmed()) {
                                ui.getStockService().deleteGood(goodToDelete);
                                refreshView();
                            }
                        }
                );
            }
        }

        @Override
        public void itemClick(ItemClickEvent event) {

            if (event.isDoubleClick() && event.getItem() != null && ui.getCurrentUser().isAdmin()) {
                openStockGoodForm((BeanItem<StockGood>) event.getItem());
            }
        }

        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            updateGood.setEnabled(table.getValue() != null);
            deleteGood.setEnabled(table.getValue() != null);
        }

        private void openStockGoodForm(BeanItem<StockGood> serviceGood) {
            openForm(new StockGoodForm(serviceGood, StockView.this));
        }

        private void openOrderStockGoodForm(BeanItem<StockGood> serviceGood) {
            openForm(new OrderStockGoodForm(serviceGood, StockView.this));
        }

        private void openForm(Component form) {
            subWindow = new Window();
            subWindow.setModal(true);
            subWindow.setHeight("650px");
            subWindow.setWidth("450px");
            subWindow.setClosable(true);
            ui.addWindow(subWindow);

            final VerticalLayout layout = new VerticalLayout();
            layout.setMargin(true);
            layout.setSpacing(true);

            layout.addComponent(form);

            subWindow.setContent(layout);
        }
    }

    private class ResetListener implements Button.ClickListener {
        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {
            idField.setValue(null);
            priceField.setValue(null);
            nameField.setValue(null);
            modelField.setValue(null);
            userField.setValue(null);
            storeField.setValue(ui.getCurrentUser().isAdmin() ? null : ui.getCurrentUser().getStore());
            categoryField.setValue(null);
        }
    }

    private class SearchListener implements Button.ClickListener {
        @Override
        public void buttonClick(Button.ClickEvent event) {
            List<StockGood> list = ui.getStockService().findGoods(buildMap());
            refreshView(list);
        }
    }

    private Map<StockGoodCriteria, Object> buildMap() {
        Map<StockGoodCriteria, Object> map = new HashMap<>();
        if (!Strings.isNullOrEmpty(idField.getValue())) {
            map.put(StockGoodCriteria.INCREMENT_ID, idField.getValue().trim().toLowerCase());
        }
        if (!Strings.isNullOrEmpty(priceField.getValue())) {
            map.put(StockGoodCriteria.PRICE, priceField.getValue().trim().toLowerCase());
        }
        if (!Strings.isNullOrEmpty(nameField.getValue())) {
            map.put(StockGoodCriteria.NAME, nameField.getValue().trim().toLowerCase());
        }
        if (!Strings.isNullOrEmpty(modelField.getValue())) {
            map.put(StockGoodCriteria.MODEL, modelField.getValue().trim().toLowerCase());
        }
        if (userField.getValue() != null) {
            map.put(StockGoodCriteria.USER, userField.getValue());
        }
        if (storeField.getValue() != null) {
            map.put(StockGoodCriteria.STORE, storeField.getValue());
        }
        if (categoryField.getValue() != null) {
            map.put(StockGoodCriteria.CATEGORY, ((Category) categoryField.getValue()).getAllIds());
        }

        return map;
    }
}
