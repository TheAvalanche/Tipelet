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
import lv.telepit.backend.criteria.ChangeRecordCriteria;
import lv.telepit.model.ChangeRecord;
import lv.telepit.model.dto.RecordData;
import lv.telepit.model.utils.ChangesComparator;
import lv.telepit.model.utils.RecordDataComparator;
import lv.telepit.ui.component.Hr;
import lv.telepit.ui.form.fields.FieldFactory;
import lv.telepit.ui.form.fields.SimpleTypeComboBox;
import lv.telepit.utils.ExcelUtils;
import lv.telepit.utils.PdfUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;

/**
 * Created by Alex on 12/05/2014.
 */
public class ChangesView extends AbstractView {

    private ComboBox userField;
    private ComboBox storeField;
    private DateField fromDateField;
    private DateField toDateField;
    private ComboBox typeField;
    private Button expandButton;
    private Button searchButton;
    private Button refreshButton;
    private Button pdfButton;
    private Button xlsButton;
    private Table table;
    private BeanItemContainer<RecordData> container;
    private Label label;


    public ChangesView(Navigator navigator, TelepitUI ui, String name) {
        super(navigator, ui, name);
    }

    @Override
    public void buildContent() {

        label = new Label(bundle.getString("changes.view.label"));
        label.setContentMode(ContentMode.HTML);

        userField = FieldFactory.getUserComboBox("search.user");
        storeField = FieldFactory.getStoreComboBox("search.store");
        fromDateField = FieldFactory.getDateField("search.change.fromDate");
        toDateField = FieldFactory.getDateField("search.change.toDate");
        typeField = FieldFactory.getTypeComboBox("search.type");
        typeField.setNullSelectionAllowed(false);
        typeField.setValue(SimpleTypeComboBox.Type.ALL);

        searchButton = new Button(bundle.getString("default.button.search"));
        searchButton.addClickListener(new SearchListener());
        searchButton.setIcon(new ThemeResource("img/search.png"));

        refreshButton = new Button();
        refreshButton.addClickListener(new RefreshListener());
        refreshButton.setIcon(new ThemeResource("img/refresh.png"));

        container = new BeanItemContainer<>(RecordData.class);
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
        table.setVisibleColumns("store", "user", "date", "type", "name", "id", "propertyName", "oldValue", "newValue");
        table.setColumnHeaders(bundle.getString("record.data.store"),
                bundle.getString("record.data.user"),
                bundle.getString("record.data.date"),
                bundle.getString("record.data.type"),
                bundle.getString("record.data.name"),
                bundle.getString("record.data.id"),
                bundle.getString("record.data.propertyName"),
                bundle.getString("record.data.oldValue"),
                bundle.getString("record.data.newValue"));

        table.setSelectable(true);
        table.setImmediate(true);

        final HorizontalLayout searchLayout1 = new HorizontalLayout(userField, storeField);
        searchLayout1.setSpacing(true);
        final HorizontalLayout searchLayout2 = new HorizontalLayout(fromDateField, toDateField, typeField);
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

        pdfButton = new Button(bundle.getString("pdf.export"));
        pdfButton.setIcon(new ThemeResource("img/pdf.png"));
        pdfButton.setWidth("150");
        StreamResource pdfStream = getPDFStream();
        pdfStream.setMIMEType("application/pdf");
        FileDownloader pdfDownloader = new FileDownloader(pdfStream);
        pdfDownloader.extend(pdfButton);

        xlsButton = new Button(bundle.getString("excel.export"));
        xlsButton.setIcon(new ThemeResource("img/excel.png"));
        xlsButton.setWidth("150");
        StreamResource excelStream = getExcelStream();
        excelStream.setMIMEType("application/vnd.ms-excel");
        FileDownloader excelDownloader = new FileDownloader(excelStream);
        excelDownloader.extend(xlsButton);


        final HorizontalLayout buttonLayout = new HorizontalLayout(pdfButton, xlsButton, refreshButton);
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

    public void refreshView(List<RecordData> records) {

        if (records == null) {
            records = ui.getCommonService().findRecords(new HashMap<ChangeRecordCriteria, Object>());
        }

        Collections.sort(records, Collections.reverseOrder(new RecordDataComparator()));

        container.removeAllItems();
        container.addAll(records);
        table.refreshRowCache();
        table.sort(new Object[]{"date"}, new boolean[]{false});

        userField.setValue(null);
        storeField.setValue(null);
        fromDateField.setValue(null);
        toDateField.setValue(null);
        typeField.setValue(SimpleTypeComboBox.Type.ALL);
    }

    @Override
    public void checkAuthority() {

    }

    private StreamResource getPDFStream() {
        StreamResource.StreamSource source = new StreamResource.StreamSource() {

            public InputStream getStream() {
                try {
                    PdfUtils pdfCreator = new PdfUtils();
                    pdfCreator.open();
                    List<ChangeRecord> changeRecords = new ArrayList<>();
                    changeRecords.addAll(ui.getCommonService().findChangeRecords(buildMap()));
                    Collections.sort(changeRecords, Collections.reverseOrder(new ChangesComparator()));
                    pdfCreator.exportChanges(changeRecords);
                    pdfCreator.close();
                    return new ByteArrayInputStream(pdfCreator.getOutputStream().toByteArray());
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        return new StreamResource (source, createReportName("pdf"));
    }

    private StreamResource getExcelStream() {
        StreamResource.StreamSource source = new StreamResource.StreamSource() {
            @Override
            public InputStream getStream() {
                try {
                    ExcelUtils excelUtils = new ExcelUtils();
                    List<ChangeRecord> changeRecords = new ArrayList<>();
                    changeRecords.addAll(ui.getCommonService().findChangeRecords(buildMap()));
                    Collections.sort(changeRecords, Collections.reverseOrder(new ChangesComparator()));
                    excelUtils.exportChanges(changeRecords);
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
        builder.append("changeReport");
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

    private class SearchListener implements Button.ClickListener {
        @Override
        public void buttonClick(Button.ClickEvent event) {
            List<RecordData> list = ui.getCommonService().findRecords(buildMap());
            refreshView(list);
        }
    }

    private Map<ChangeRecordCriteria, Object> buildMap() {
        Map<ChangeRecordCriteria, Object> map = new HashMap<>();
        if (userField.getValue() != null) {
            map.put(ChangeRecordCriteria.USER, userField.getValue());
        }
        if (storeField.getValue() != null) {
            map.put(ChangeRecordCriteria.STORE, storeField.getValue());
        }
        if (fromDateField.getValue() != null) {
            map.put(ChangeRecordCriteria.DATE_FROM, DateUtils.truncate(fromDateField.getValue(), Calendar.DATE));
        }
        if (toDateField.getValue() != null) {
            map.put(ChangeRecordCriteria.DATE_TO, DateUtils.ceiling(toDateField.getValue(), Calendar.DATE));
        }
        if (typeField.getValue() == SimpleTypeComboBox.Type.SERVICE) {
            map.put(ChangeRecordCriteria.TYPE_SERVICE, null);
        }
        if (typeField.getValue() == SimpleTypeComboBox.Type.STOCK) {
            map.put(ChangeRecordCriteria.TYPE_STOCK, null);
        }
        return map;
    }

}
