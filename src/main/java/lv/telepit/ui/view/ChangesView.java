package lv.telepit.ui.view;

import com.google.common.base.Strings;
import com.itextpdf.text.DocumentException;
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
import lv.telepit.model.RecordData;
import lv.telepit.ui.component.Hr;
import lv.telepit.ui.form.fields.FieldFactory;
import lv.telepit.utils.PdfUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alex on 12/05/2014.
 */
public class ChangesView extends AbstractView {

    private TextField nameField;
    private ComboBox userField;
    private ComboBox storeField;
    private DateField fromDateField;
    private DateField toDateField;
    private Button expandButton;
    private Button searchButton;
    private Button refreshButton;
    private Button pdfButton;
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
        nameField = FieldFactory.getTextField("search.change.name");

        searchButton = new Button(bundle.getString("default.button.search"));
        searchButton.addClickListener(new SearchListener());
        searchButton.setIcon(new ThemeResource("img/search.png"));

        refreshButton = new Button();
        refreshButton.addClickListener(new RefreshListener());
        refreshButton.setIcon(new ThemeResource("img/refresh.png"));

        container = new BeanItemContainer<>(RecordData.class);
        table = new Table();
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

        final HorizontalLayout searchLayout1 = new HorizontalLayout(userField, storeField, nameField);
        searchLayout1.setSpacing(true);
        final HorizontalLayout searchLayout2 = new HorizontalLayout(nameField, fromDateField, toDateField);
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

        refreshView();

    }

    @Override
    public void refreshView() {
        refreshView(null);
    }

    public void refreshView(List<RecordData> records) {

        if (records == null) {
            records = ui.getCommonService().findRecords(new HashMap<ChangeRecordCriteria, Object>());
        }
        container.removeAllItems();
        container.addAll(records);
        table.refreshRowCache();
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
                    pdfCreator.exportChanges(ui.getCommonService().findChangeRecords(buildMap()));
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
            List<RecordData> list = ui.getCommonService().findRecords(buildMap());
            refreshView(list);
        }
    }

    private Map<ChangeRecordCriteria, Object> buildMap() {
        Map<ChangeRecordCriteria, Object> map = new HashMap<>();
        if (!Strings.isNullOrEmpty(nameField.getValue())) {
            map.put(ChangeRecordCriteria.NAME, nameField.getValue().trim().toLowerCase());
        }
        if (userField.getValue() != null) {
            map.put(ChangeRecordCriteria.USER, userField.getValue());
        }
        if (storeField.getValue() != null) {
            map.put(ChangeRecordCriteria.STORE, storeField.getValue());
        }
        if (fromDateField.getValue() != null) {
            map.put(ChangeRecordCriteria.DATE_FROM, fromDateField.getValue()); //TODO: floor value
        }
        if (toDateField.getValue() != null) {
            map.put(ChangeRecordCriteria.DATE_TO, toDateField.getValue()); //TODO: truncate date
        }
        return map;
    }

}
