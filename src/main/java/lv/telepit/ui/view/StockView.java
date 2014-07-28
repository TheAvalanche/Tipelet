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
import lv.telepit.backend.criteria.StockGoodCriteria;
import lv.telepit.model.Category;
import lv.telepit.model.StockGood;
import lv.telepit.ui.component.Hr;
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

/**
 * Created by Alex on 21/02/14.
 */
public class StockView extends AbstractView {

    private Button addGood;
    private Button updateGood;
    private Button deleteGood;
    private Button xlsButton;

    private TextField idField;
    private TextField nameField;
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

        idField = FieldFactory.getTextField("search.stock.id");
        nameField = FieldFactory.getTextField("search.stock.name");
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
        table.setVisibleColumns("id", "store", "category", "name", "model", "compatibleModels", "price", "count", "total", "lastSoldDate");
        table.setColumnHeaders(bundle.getString("stock.good.id"),
                bundle.getString("stock.good.store"),
                bundle.getString("stock.good.category"),
                bundle.getString("stock.good.name"),
                bundle.getString("stock.good.model"),
                bundle.getString("stock.good.compatibleModels"),
                bundle.getString("stock.good.price"),
                bundle.getString("stock.good.count"),
                bundle.getString("stock.good.total"),
                bundle.getString("stock.good.lastSoldDate"));
        table.setCellStyleGenerator(new Table.CellStyleGenerator() {
            @Override
            public String getStyle(Table source, Object itemId, Object propertyId) {
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
            }
        });
        table.setSelectable(true);
        table.setImmediate(true);
        table.addItemClickListener(new EditStockGoodListener());
        table.addValueChangeListener(new EditStockGoodListener());
        table.addActionHandler(new StockContext(this));


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

        final HorizontalLayout searchLayout1 = new HorizontalLayout(idField, userField, storeField, categoryField, nameField);
        searchLayout1.setSpacing(true);

        final VerticalLayout searchLayout = new VerticalLayout(new Hr(), searchLayout1,
                new HorizontalLayout(searchButton, resetButton), new Hr());
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
        buttonLayout.setWidth("1000px");
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
        if (!ui.getCurrentUser().isAdmin()) {
            storeField.setVisible(false);
            userField.setVisible(false);
            updateGood.setVisible(false);
            deleteGood.setVisible(false);
        }
    }

    @Override
    public void reset() {
        resetListener.buttonClick(null);
    }

    private StreamResource getExcelStream() {
        StreamResource.StreamSource source = new StreamResource.StreamSource() {
            @Override
            public InputStream getStream() {
                try {
                    ExcelUtils excelUtils = new ExcelUtils();
                    excelUtils.stockGoodsToExcel(ui.getStockService().findGoods(buildMap()));
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

    private class ResetListener implements Button.ClickListener {
        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {
            idField.setValue(null);
            nameField.setValue(null);
            userField.setValue(null);
            storeField.setValue(ui.getCurrentUser().getStore());
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
            map.put(StockGoodCriteria.ID, idField.getValue().trim().toLowerCase());
        }
        if (!Strings.isNullOrEmpty(nameField.getValue())) {
            map.put(StockGoodCriteria.NAME, nameField.getValue().trim().toLowerCase());
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
