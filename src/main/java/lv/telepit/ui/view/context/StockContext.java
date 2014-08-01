package lv.telepit.ui.view.context;

import com.itextpdf.text.DocumentException;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.Action;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Sizeable;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import lv.telepit.model.*;
import lv.telepit.ui.component.Hr;
import lv.telepit.ui.form.converters.StringToDoubleConverter;
import lv.telepit.ui.form.fields.FieldFactory;
import lv.telepit.ui.view.StockView;
import lv.telepit.utils.PdfUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Alex on 12/05/2014.
 */
public class StockContext implements Action.Handler {

    private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");

    private final Action checkAsBestseller = new Action(bundle.getString("popular.item"));
    private final Action uncheckAsBestseller = new Action(bundle.getString("unpopular.item"));
    private final Action uncheckAsOrdered = new Action(bundle.getString("ordered.item"));
    private final Action checkAttention = new Action(bundle.getString("attention.item"));
    private final Action uncheckAttention = new Action(bundle.getString("unattention.item"));
    private final Action sell = new Action(bundle.getString("sell.item"));
    private final Action move = new Action(bundle.getString("move.item"));
    private final Action createWarrancy = new Action(bundle.getString("create.warranty"));
    private final Action showHistory = new Action(bundle.getString("show.history"));

    private StockView view;

    public StockContext(StockView view) {
        this.view = view;
    }

    @Override
    public Action[] getActions(Object target, Object sender) {
        if (target == null) return new Action[]{};

        if (((StockGood) target).isOrdered()) {
            return new Action[]{uncheckAsOrdered, createWarrancy, showHistory};
        }

        if (view.getUi().getCurrentUser().isAdmin()) {
            return new Action[]{sell,
                    ((StockGood) target).isBestseller() ? uncheckAsBestseller : checkAsBestseller,
                    ((StockGood) target).isAttention() ? uncheckAttention : checkAttention,
                    move,
                    createWarrancy,
                    showHistory};
        } else {
            return new Action[]{sell,
                    ((StockGood) target).isBestseller() ? uncheckAsBestseller : checkAsBestseller,
                    ((StockGood) target).isAttention() ? uncheckAttention : checkAttention,
                    createWarrancy,
                    showHistory};
        }


    }

    @Override
    public void handleAction(Action action, Object sender, Object target) {
         if (action == sell) {
             showSell((StockGood) target);
             view.refreshView();
         } else if (action == checkAsBestseller) {
             StockGood good = (StockGood) target;
             good.setBestseller(true);
             view.getUi().getStockService().updateGood(good);
             view.refreshView();
         } else if (action == uncheckAsBestseller) {
             StockGood good = (StockGood) target;
             good.setBestseller(false);
             view.getUi().getStockService().updateGood(good);
             view.refreshView();
         } else if (action == checkAttention) {
             StockGood good = (StockGood) target;
             good.setAttention(true);
             view.getUi().getStockService().updateGood(good);
             view.refreshView();
         } else if (action == uncheckAttention) {
             StockGood good = (StockGood) target;
             good.setAttention(false);
             view.getUi().getStockService().updateGood(good);
             view.refreshView();
         } else if (action == createWarrancy) {
             generateWarranty();
             view.refreshView();
         } else if (action == showHistory) {
             showHistory((StockGood) target);
             view.refreshView();
         } else if (action == uncheckAsOrdered) {
             StockGood good = (StockGood) target;
             good.setOrdered(false);
             good.setAdvance(null);
             view.getUi().getStockService().updateGood(good);
             view.refreshView();
         } else if (action == move) {
             move((StockGood) target);
             view.refreshView();
         }
    }

    private void move(final StockGood stockGood) {
        if (stockGood.getCount() == 0) {
            Notification.show("Nav ko pārcelt");
            return;
        }

        final Window subWindow = new Window();
        subWindow.setModal(true);
        subWindow.setHeight("300px");
        subWindow.setWidth("400px");
        subWindow.setClosable(true);
        view.getUi().addWindow(subWindow);

        final VerticalLayout layout = new VerticalLayout();
        layout.setWidth("100%");
        layout.setMargin(true);
        layout.setSpacing(true);

        final Slider slider = new Slider("Daudzums", 0, stockGood.getCount());
        slider.setWidth("100%");
        final ComboBox stores = FieldFactory.getStoreComboBox("search.store");
        layout.addComponent(slider);
        layout.addComponent(stores);

        final Button move = new Button("Pārcelt");
        move.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (slider.getValue().equals(0.0)) {
                    Notification.show("Nav ko pārcelt");
                    return;
                }
                if (stores.getValue() == null) {
                    Notification.show("Veikals nav izvelēts");
                    return;
                }
                if (stores.getValue().equals(stockGood.getStore())) {
                    Notification.show("Izvelies citu veikalu");
                    return;
                }
                view.getUi().getStockService().moveToStore(stockGood, slider.getValue().intValue(), (Store) stores.getValue());
                subWindow.close();
                view.refreshView();
                Notification.show(bundle.getString("save.success"));
            }
        });
        layout.addComponent(move);

        subWindow.setContent(layout);
    }

    private void showSell(final StockGood stockGood) {
        final Window subWindow = new Window();
        subWindow.setModal(true);
        subWindow.setHeight("500px");
        subWindow.setWidth("550px");
        subWindow.setClosable(true);
        view.getUi().addWindow(subWindow);

        final VerticalLayout layout = new VerticalLayout();
        layout.setWidth("100%");
        layout.setMargin(true);
        layout.setSpacing(true);

        final VerticalLayout subLayout = new VerticalLayout();
        subLayout.setWidth("100%");
        subLayout.setSpacing(true);

        final Label total = new Label("", ContentMode.HTML);

        final List<SoldItem> soldItems = new ArrayList<>();
        final PriceListener priceListener = new PriceListener(total, soldItems);
        addSoldItem(soldItems, stockGood, subLayout, priceListener);

        Button addButton = new Button(bundle.getString("default.button.add"));
        addButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (soldItems.size() < stockGood.getCount()) {
                    addSoldItem(soldItems, stockGood, subLayout, priceListener);
                } else {
                    Notification.show(bundle.getString("add.fail"));
                }
            }
        });

        Button sellButton = new Button(bundle.getString("sell.item"));
        sellButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    validate(soldItems, stockGood);
                    view.getUi().getStockService().sell(stockGood, soldItems);
                    Notification.show(bundle.getString("save.success"));
                    subWindow.close();
                    view.refreshView();
                } catch (IllegalStateException e) {
                    Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
                }
            }
        });

        HorizontalLayout buttonLayout = new HorizontalLayout(addButton, sellButton);
        buttonLayout.setWidth("100%");
        buttonLayout.setComponentAlignment(sellButton, Alignment.BOTTOM_RIGHT);

        layout.addComponent(new Label("<h2>" + stockGood.getName() + " (" + stockGood.getModel() + ")</h2>", ContentMode.HTML));
        layout.addComponent(subLayout);
        layout.addComponent(total);
        layout.addComponent(new Hr());
        layout.addComponent(buttonLayout);

        subWindow.setContent(layout);
    }

    private void validate(List<SoldItem> soldItems, StockGood good) throws IllegalStateException {
        for (SoldItem soldItem : soldItems) {
            if (!soldItem.getPrice().equals(good.getPrice()) && StringUtils.isBlank(soldItem.getInfo())) {
                throw new IllegalStateException("Pievienojiet informāciju par cenas izmaiņu.");
            }
        }
    }

    private void addSoldItem(final List<SoldItem> soldItems, final StockGood stockGood, final Layout layout, final PriceListener priceListener) {
        final SoldItem item = new SoldItem();
        item.setUser(view.getUi().getCurrentUser());
        item.setStore(view.getUi().getCurrentUser().getStore());
        item.setPrice(stockGood.getPrice());
        soldItems.add(item);
        priceListener.update();

        BeanItem<SoldItem> beanItem = new BeanItem<>(item);

        TextField codeField = new TextField("Code", beanItem.getItemProperty("code"));
        codeField.setImmediate(true);
        codeField.setWidth(100f, Sizeable.Unit.PIXELS);
        codeField.setNullRepresentation("");

        CheckBox billField = new CheckBox("Ar čeku", beanItem.getItemProperty("withBill"));
        billField.setImmediate(true);

        final TextField priceField = new TextField("Cena", beanItem.getItemProperty("price"));
        priceField.setImmediate(true);
        priceField.setRequired(true);
        priceField.setWidth(100f, Sizeable.Unit.PIXELS);
        priceField.setConverter(new StringToDoubleConverter());
        priceField.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                try {
                    priceField.validate();
                } catch (Validator.InvalidValueException e) {
                    Notification.show("Nepareiza cena!", Notification.Type.ERROR_MESSAGE);
                    item.setPrice(stockGood.getPrice());
                    priceField.setValue(String.valueOf(stockGood.getPrice()));
                }
                priceListener.update();
            }
        });

        TextField infoField = new TextField("Info", beanItem.getItemProperty("info"));
        infoField.setImmediate(true);
        infoField.setNullRepresentation("");
        infoField.setWidth(100f, Sizeable.Unit.PIXELS);

        Button deleteButton = new Button(bundle.getString("default.button.delete"));
        deleteButton.setStyleName("small");
        if (soldItems.size() == 1) {
            deleteButton.setEnabled(false);
        }

        final HorizontalLayout subLayout = new HorizontalLayout(codeField, priceField, infoField, billField, deleteButton);
        subLayout.setWidth("100%");
        subLayout.setSpacing(true);
        subLayout.setComponentAlignment(priceField, Alignment.BOTTOM_RIGHT);
        subLayout.setComponentAlignment(infoField, Alignment.BOTTOM_RIGHT);
        subLayout.setComponentAlignment(billField, Alignment.BOTTOM_LEFT);
        subLayout.setComponentAlignment(deleteButton, Alignment.BOTTOM_RIGHT);

        deleteButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                soldItems.remove(item);
                layout.removeComponent(subLayout);
                priceListener.update();
            }
        });

        layout.addComponent(subLayout);

    }

    private void showHistory(StockGood stockGood) {

        List<ChangeRecord> historyList = view.getUi().getStockService().findChanges(stockGood);
        Window subWindow = new Window();
        subWindow.setModal(true);
        subWindow.setHeight("650px");
        subWindow.setWidth("700px");
        subWindow.setClosable(true);
        view.getUi().addWindow(subWindow);

        final Button pdfButton = new Button(bundle.getString("pdf.export"));
        pdfButton.setIcon(new ThemeResource("img/pdf.png"));
        pdfButton.setWidth("150");
        StreamResource pdfStream = getPDFStream(view.getUi().getStockService().findChanges(stockGood));
        pdfStream.setMIMEType("application/pdf");
        FileDownloader pdfDownloader = new FileDownloader(pdfStream);
        pdfDownloader.extend(pdfButton);

        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.addComponent(pdfButton);

        for (ChangeRecord record : historyList) {
            for (ChangeRecord.PropertyChange p : record.getChangeList()) {
                p.setName(bundle.getString(p.getName()));
            }
            Panel panel = new Panel();
            BeanItemContainer<ChangeRecord.PropertyChange> container = new BeanItemContainer<>(ChangeRecord.PropertyChange.class, record.getChangeList());
            Table table = new Table();
            table.setContainerDataSource(container);
            table.setVisibleColumns("name", "oldValue", "newValue");
            table.setColumnHeaders(bundle.getString("history.property"), bundle.getString("history.old"), bundle.getString("history.new"));
            table.setColumnExpandRatio("name", 0.33f);
            table.setColumnExpandRatio("oldValue", 0.33f);
            table.setColumnExpandRatio("newValue", 0.33f);
            table.setPageLength(0);
            table.setWidth("100%");

            VerticalLayout panelLayout = new VerticalLayout();
            panelLayout.addComponent(new Label("<b>" + new SimpleDateFormat("dd-MM-YYYY HH:mm").format(record.getDate())
                    + ": " + record.getUser().getName() + " "
                    + record.getUser().getSurname() + "</b><br/>", ContentMode.HTML));
            panelLayout.addComponent(table);
            panel.setContent(panelLayout);
            layout.addComponent(panel);
        }

        subWindow.setContent(layout);
    }

    private StreamResource getPDFStream(final List<ChangeRecord> records) {
        StreamResource.StreamSource source = new StreamResource.StreamSource() {

            public InputStream getStream() {
                try {
                    PdfUtils pdfCreator = new PdfUtils();
                    pdfCreator.open();
                    pdfCreator.exportChanges(records);
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

    private String createReportName(String extension) {
        StringBuilder builder = new StringBuilder();
        builder.append("changeReport");
        builder.append(new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()));
        builder.append("." + extension);
        return builder.toString();
    }

    private class PriceListener {
        Label label;
        List<SoldItem> list;

        private PriceListener(Label label, List<SoldItem> list) {
            this.label = label;
            this.list = list;
        }

        private void update() {
            BigDecimal total = new BigDecimal("0");
            for (SoldItem soldItem : list) {
                total = total.add(new BigDecimal(String.valueOf(soldItem.getPrice())));
            }
            label.setValue("Kopā: " + total + "€");
        }
    }

    private void generateWarranty() {
        Window subWindow = new Window();
        subWindow.setModal(true);
        subWindow.setHeight("180px");
        subWindow.setWidth("200px");
        subWindow.setClosable(true);
        view.getUi().addWindow(subWindow);

        final Button pdfButton = new Button("Garantijas kvīts");
        StreamResource sr2 = getPDFStream();
        sr2.setMIMEType("application/pdf");
        FileDownloader fileDownloader2 = new FileDownloader(sr2);
        fileDownloader2.extend(pdfButton);

        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.addComponent(new Label("Izveidot kvītu?"));
        HorizontalLayout buttons = new HorizontalLayout(pdfButton);
        buttons.setSpacing(true);
        buttons.setMargin(true);
        layout.addComponent(buttons);

        subWindow.setContent(layout);
    }

    private StreamResource getPDFStream() {
        StreamResource.StreamSource source = new StreamResource.StreamSource() {

            public InputStream getStream() {
                try {
                    PdfUtils pdfCreator = new PdfUtils();
                    pdfCreator.open();
                    pdfCreator.createWarranty();
                    pdfCreator.close();
                    return new ByteArrayInputStream(pdfCreator.getOutputStream().toByteArray());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        return new StreamResource (source, createWarrantyName("pdf"));
    }

    private String createWarrantyName(String extension) {
        StringBuilder builder = new StringBuilder();
        builder.append("kvits");
        builder.append(new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()));
        builder.append("." + extension);
        return builder.toString();
    }
}
