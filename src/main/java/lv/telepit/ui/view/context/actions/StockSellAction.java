package lv.telepit.ui.view.context.actions;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import lv.telepit.model.SoldItem;
import lv.telepit.model.StockGood;
import lv.telepit.ui.component.Hr;
import lv.telepit.ui.component.SpacedHorizontalLayout;
import lv.telepit.ui.form.converters.StringToDoubleConverter;
import lv.telepit.ui.view.AbstractView;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class StockSellAction extends AbstractAction {

    private final StockGood good;

    public StockSellAction(StockGood good, AbstractView view) {
        super(view);
        this.setCaption(bundle.getString("sell.item"));
        this.good = good;
    }

    @Override
    public boolean show() {
        return !good.isOrdered() && good.getCount() > 0;
    }

    @Override
    public void execute() {
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
        addSoldItem(soldItems, subLayout, priceListener);

        Button addButton = new Button(bundle.getString("default.button.add"));
        addButton.addClickListener((Button.ClickListener) event -> {
            if (soldItems.size() < good.getCount()) {
                addSoldItem(soldItems, subLayout, priceListener);
            } else {
                Notification.show(bundle.getString("add.fail"));
            }
        });

        Button sellButton = new Button(bundle.getString("sell.item"));
        sellButton.addClickListener((Button.ClickListener) event -> {
            try {
                validate(soldItems, good);
                view.getUi().getStockService().sell(good, soldItems);
                Notification.show(bundle.getString("save.success"));
                subWindow.close();
                view.refreshView();
            } catch (IllegalStateException e) {
                Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
            }
        });

        HorizontalLayout buttonLayout = new SpacedHorizontalLayout(addButton, sellButton);
        buttonLayout.setWidth("100%");
        buttonLayout.setComponentAlignment(sellButton, Alignment.BOTTOM_RIGHT);

        layout.addComponent(new Label("<h2>" + good.getName() + " (" + good.getModel() + ")</h2>", ContentMode.HTML));
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

    private void addSoldItem(final List<SoldItem> soldItems, final Layout layout, final PriceListener priceListener) {
        final SoldItem item = new SoldItem();
        item.setUser(view.getUi().getCurrentUser());
        item.setStore(view.getUi().getCurrentUser().getStore());
        item.setPrice(good.getPrice());
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
        priceField.addValueChangeListener((Property.ValueChangeListener) event -> {
            try {
                priceField.validate();
            } catch (Validator.InvalidValueException e) {
                Notification.show("Nepareiza cena!", Notification.Type.ERROR_MESSAGE);
                item.setPrice(good.getPrice());
                priceField.setValue(String.valueOf(good.getPrice()));
            }
            priceListener.update();
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

        final HorizontalLayout subLayout = new SpacedHorizontalLayout(codeField, priceField, infoField, billField, deleteButton);
        subLayout.setWidth("100%");
        subLayout.setComponentAlignment(priceField, Alignment.BOTTOM_RIGHT);
        subLayout.setComponentAlignment(infoField, Alignment.BOTTOM_RIGHT);
        subLayout.setComponentAlignment(billField, Alignment.BOTTOM_LEFT);
        subLayout.setComponentAlignment(deleteButton, Alignment.BOTTOM_RIGHT);

        deleteButton.addClickListener((Button.ClickListener) event -> {
            soldItems.remove(item);
            layout.removeComponent(subLayout);
            priceListener.update();
        });

        layout.addComponent(subLayout);

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
}
