package lv.telepit.ui.view;

import com.itextpdf.text.DocumentException;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import lv.telepit.TelepitUI;
import lv.telepit.backend.criteria.ServiceGoodCriteria;
import lv.telepit.backend.criteria.SoldItemCriteria;
import lv.telepit.model.ReportData;
import lv.telepit.ui.component.Hr;
import lv.telepit.ui.form.fields.FieldFactory;
import lv.telepit.utils.PdfUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;

/**
 * Created by Alex on 12/05/2014.
 */
public class ReportView extends AbstractView {

    private ComboBox userField;
    private ComboBox storeField;
    private DateField fromDateField;
    private DateField toDateField;
    private Button expandButton;
    private Button searchButton;
    private Button refreshButton;
    private Button pdfButton;
    private Table table;
    private BeanItemContainer<ReportData> container;
    private Label label;
    private Label sumLabel;

    public ReportView(Navigator navigator, TelepitUI ui, String name) {
        super(navigator, ui, name);
    }

    @Override
    public void buildContent() {

        label = new Label(bundle.getString("reports.view.label"));
        label.setContentMode(ContentMode.HTML);

        userField = FieldFactory.getUserComboBox("search.user");
        storeField = FieldFactory.getStoreComboBox("search.store");
        fromDateField = FieldFactory.getDateField("search.change.fromDate");
        toDateField = FieldFactory.getDateField("search.change.toDate");

        searchButton = new Button(bundle.getString("default.button.search"));
        searchButton.addClickListener(new SearchListener());
        searchButton.setIcon(new ThemeResource("img/search.png"));

        refreshButton = new Button();
        refreshButton.addClickListener(new RefreshListener());
        refreshButton.setIcon(new ThemeResource("img/refresh.png"));

        container = new BeanItemContainer<>(ReportData.class);
        table = new Table() {
            @Override
            protected String formatPropertyValue(Object rowId, Object colId, Property property) {
                Object v = property.getValue();
                if (v instanceof Date) {
                    Date dateValue = (Date) v;
                    return new SimpleDateFormat("dd.MM.yyyy HH:mm").format(dateValue);
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
        table.setVisibleColumns("store", "user", "date", "type", "id", "name", "code", "price");
        table.setColumnHeaders(bundle.getString("report.data.store"),
                bundle.getString("report.data.user"),
                bundle.getString("report.data.date"),
                bundle.getString("report.data.type"),
                bundle.getString("report.data.id"),
                bundle.getString("report.data.name"),
                bundle.getString("report.data.code"),
                bundle.getString("report.data.price"));

        table.setSelectable(true);
        table.setImmediate(true);

        final HorizontalLayout searchLayout1 = new HorizontalLayout(userField, storeField);
        searchLayout1.setSpacing(true);
        final HorizontalLayout searchLayout2 = new HorizontalLayout(fromDateField, toDateField);
        searchLayout2.setSpacing(true);

        final VerticalLayout searchLayout = new VerticalLayout(new Hr(), searchLayout1, searchLayout2, searchButton, new Hr());
        searchLayout.setSpacing(true);
        searchLayout.setVisible(false);

        expandButton = new Button("Radīt/Slēpt meklēšanas rīkus");
        expandButton.setStyleName(Reindeer.BUTTON_LINK);
        expandButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                searchLayout.setVisible(!searchLayout.isVisible());
            }
        });

        pdfButton = new Button("PDF export");
        pdfButton.setIcon(new ThemeResource("img/pdf.png"));
        pdfButton.setWidth("150");
        StreamResource sr = getPDFStream();
        sr.setMIMEType("application/pdf");
        FileDownloader fileDownloader = new FileDownloader(sr);
        fileDownloader.extend(pdfButton);

        sumLabel = new Label();
        sumLabel.setContentMode(ContentMode.HTML);

        final HorizontalLayout buttonLayout = new HorizontalLayout(pdfButton, refreshButton);
        buttonLayout.setSpacing(true);
        buttonLayout.setWidth("1200px");
        buttonLayout.setExpandRatio(refreshButton, 1.0f);
        buttonLayout.setComponentAlignment(refreshButton, Alignment.BOTTOM_RIGHT);

        content.addComponent(label);
        content.addComponent(expandButton);
        content.addComponent(searchLayout);
        content.addComponent(buttonLayout);
        content.addComponent(table);
        content.addComponent(sumLabel);
        content.setComponentAlignment(sumLabel, Alignment.BOTTOM_RIGHT);

        refreshView();

    }

    @Override
    public void refreshView() {
        refreshView(null);
    }

    public void refreshView(List<ReportData> records) {

        if (records == null) {
            records = new ArrayList<>();
            records.addAll(ui.getServiceGoodService().findReports(new HashMap<ServiceGoodCriteria, Object>()));
            records.addAll(ui.getStockService().findReports(new HashMap<SoldItemCriteria, Object>()));
        }
        container.removeAllItems();
        container.addAll(records);
        table.refreshRowCache();
        table.sort(new Object[]{"date"}, new boolean[]{false});
        buildSumLabel(records);

        userField.setValue(null);
        storeField.setValue(null);
        fromDateField.setValue(null);
        toDateField.setValue(null);
    }

    @Override
    public void checkAuthority() {

    }

    private void buildSumLabel(List<ReportData> reportDatas) {
        BigDecimal sum = new BigDecimal("0");
        for (ReportData reportData : reportDatas) {
            sum = sum.add(new BigDecimal(reportData.getPrice()));
        }
        sumLabel.setValue("<b>Kopā: </b>" + sum + "€");
    }

    private StreamResource getPDFStream() {
        StreamResource.StreamSource source = new StreamResource.StreamSource() {

            public InputStream getStream() {
                try {
                    PdfUtils pdfCreator = new PdfUtils();
                    pdfCreator.open();
                    //pdfCreator.exportChanges(ui.getCommonService().findChangeRecords(buildMap()));
                    pdfCreator.close();
                    return new ByteArrayInputStream(pdfCreator.getOutputStream().toByteArray());
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        return new StreamResource (source, "report.pdf"); //TODO: dynamic name
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
            List<ReportData> list = new ArrayList<>();
            list.addAll(ui.getStockService().findReports(buildSoldItemMap()));
            list.addAll(ui.getServiceGoodService().findReports(buildServiceGoodMap()));
            refreshView(list);
        }
    }

    private Map<SoldItemCriteria, Object> buildSoldItemMap() {
        Map<SoldItemCriteria, Object> map = new HashMap<>();
        if (userField.getValue() != null) {
            map.put(SoldItemCriteria.USER, userField.getValue());
        }
        if (storeField.getValue() != null) {
            map.put(SoldItemCriteria.STORE, storeField.getValue());
        }
        if (fromDateField.getValue() != null) {
            map.put(SoldItemCriteria.DATE_FROM, DateUtils.truncate(fromDateField.getValue(), Calendar.DATE));
        }
        if (toDateField.getValue() != null) {
            map.put(SoldItemCriteria.DATE_TO, DateUtils.ceiling(toDateField.getValue(), Calendar.DATE));
        }
        return map;
    }

    private Map<ServiceGoodCriteria, Object> buildServiceGoodMap() {
        Map<ServiceGoodCriteria, Object> map = new HashMap<>();
        if (userField.getValue() != null) {
            map.put(ServiceGoodCriteria.USER, userField.getValue());
        }
        if (storeField.getValue() != null) {
            map.put(ServiceGoodCriteria.STORE, storeField.getValue());
        }
        if (fromDateField.getValue() != null) {
            map.put(ServiceGoodCriteria.RETURNED_DATE_FROM, DateUtils.truncate(fromDateField.getValue(), Calendar.DATE));
        }
        if (toDateField.getValue() != null) {
            map.put(ServiceGoodCriteria.RETURNED_DATE_TO, DateUtils.ceiling(toDateField.getValue(), Calendar.DATE));
        }
        return map;
    }
}
