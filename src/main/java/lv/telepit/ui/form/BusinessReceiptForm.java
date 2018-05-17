package lv.telepit.ui.form;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import lv.telepit.backend.BusinessReceiptService;
import lv.telepit.model.BusinessReceipt;
import lv.telepit.model.ReceiptItem;
import lv.telepit.model.Store;
import lv.telepit.ui.actions.SaveOnClick;
import lv.telepit.ui.component.Hr;
import lv.telepit.ui.component.SpacedHorizontalLayout;
import lv.telepit.ui.form.converters.StringToDoubleConverter;
import lv.telepit.ui.form.converters.StringToIntConverter;
import lv.telepit.ui.form.fields.FieldFactory;
import lv.telepit.ui.view.AbstractView;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class BusinessReceiptForm extends FormLayout {

	private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");
		
	@PropertyId("store")
	private ComboBox storeField = FieldFactory.getStoreComboBox("businessReceipt.store");

	@PropertyId("providerName")
	private TextField providerNameField = FieldFactory.getTextField("businessReceipt.providerName");

	@PropertyId("providerLegalAddress")
	private TextField providerLegalAddressField = FieldFactory.getTextField("businessReceipt.providerLegalAddress");

	@PropertyId("providerRegNum")
	private TextField providerRegNumField = FieldFactory.getTextField("businessReceipt.providerRegNum");

	@PropertyId("providerBankName")
	private TextField providerBankNameField = FieldFactory.getTextField("businessReceipt.providerBankName");

	@PropertyId("providerBankNum")
	private TextField providerBankNumField = FieldFactory.getTextField("businessReceipt.providerBankNum");

	@PropertyId("providerAddress")
	private TextField providerAddressField = FieldFactory.getTextField("businessReceipt.providerAddress");

	@PropertyId("receiverName")
	private TextField receiverNameField = FieldFactory.getTextField("businessReceipt.receiverName");

	@PropertyId("receiverRegNum")
	private TextField receiverRegNumField = FieldFactory.getTextField("businessReceipt.receiverRegNum");

	@PropertyId("receiverLegalAddress")
	private TextField receiverLegalAddressField = FieldFactory.getTextField("businessReceipt.receiverLegalAddress");

	@PropertyId("receiverBankName")
	private TextField receiverBankNameField = FieldFactory.getTextField("businessReceipt.receiverBankName");

	@PropertyId("receiverBankNum")
	private TextField receiverBankNumField = FieldFactory.getTextField("businessReceipt.receiverBankNum");

	public BusinessReceiptForm(BeanItem<BusinessReceipt> businessReceiptItem, AbstractView view) {

		BusinessReceipt good = businessReceiptItem.getBean();
		if (good.getId() == 0) {
			Store store = view.getUi().getCurrentUser().getStore();
			good.setUser(view.getUi().getCurrentUser());
			good.setStore(view.getUi().getCurrentUser().getStore());
			good.setDate(new Date());
			good.setProviderName(store.getLegalName());
			good.setProviderLegalAddress(store.getLegalAddress());
			good.setProviderAddress(store.getAddress());
			good.setProviderRegNum(store.getLegalRegNum());
			good.setProviderBankName(store.getLegalBankName());
			good.setProviderBankNum(store.getLegalBankNum());
		}
		
		if (view.getUi().getCurrentUser().isAdmin()) {
			storeField.setRequired(true);
			addComponent(storeField);
			storeField.addValueChangeListener((Property.ValueChangeListener) valueChangeEvent -> {
				Store store = (Store) valueChangeEvent.getProperty().getValue();
				if (store == null) {
					providerNameField.setValue("");
					providerRegNumField.setValue("");
					providerLegalAddressField.setValue("");
					providerAddressField.setValue("");
					providerBankNameField.setValue("");
					providerBankNumField.setValue("");
				} else {
					providerNameField.setValue(store.getLegalName());
					providerRegNumField.setValue(store.getLegalRegNum());
					providerLegalAddressField.setValue(store.getLegalAddress());
					providerAddressField.setValue(store.getAddress());
					providerBankNameField.setValue(store.getLegalBankName());
					providerBankNumField.setValue(store.getLegalBankNum());
				}

			});
		}

		FieldGroup binder = new FieldGroup(businessReceiptItem);
		binder.bindMemberFields(this);

		providerNameField.setEnabled(false);
		providerRegNumField.setEnabled(false);
		providerLegalAddressField.setEnabled(false);
		providerAddressField.setEnabled(false);
		providerBankNameField.setEnabled(false);
		providerBankNumField.setEnabled(false);
		
		addComponent(new SpacedHorizontalLayout(providerNameField, providerRegNumField));
		addComponent(new SpacedHorizontalLayout(providerLegalAddressField, providerAddressField));
		addComponent(new SpacedHorizontalLayout(providerBankNameField, providerBankNumField));
		addComponent(new Hr());
		addComponent(new SpacedHorizontalLayout(receiverNameField, receiverRegNumField));
		addComponent(new SpacedHorizontalLayout(receiverBankNameField, receiverBankNumField));
		addComponent(new SpacedHorizontalLayout(receiverLegalAddressField));
		addComponent(new Hr());

		final VerticalLayout subLayout = new VerticalLayout();
		subLayout.setWidth("100%");
		subLayout.setSpacing(true);

		final Label total = new Label("", ContentMode.HTML);

		final List<ReceiptItem> receiptItems = good.getReceiptItems();
		final PriceListener priceListener = new PriceListener(total, receiptItems);

		if (good.getReceiptItems().isEmpty()) {
			ReceiptItem item = new ReceiptItem();
			item.setPrice(0.0);
			item.setCount(1);
			receiptItems.add(item);
			addReceiptItem(receiptItems, item, subLayout, priceListener);
		} else {
			for (ReceiptItem receiptItem : good.getReceiptItems()) {
				addReceiptItem(good.getReceiptItems(), receiptItem, subLayout, priceListener);
			}
		}

		Button addButton = new Button(bundle.getString("default.button.add"));
		addButton.addClickListener((Button.ClickListener) event -> {
			ReceiptItem item = new ReceiptItem();
			item.setPrice(0.0);
			item.setCount(1);
			receiptItems.add(item);
			addReceiptItem(receiptItems, item, subLayout, priceListener);
		});

		addComponent(addButton);
		addComponent(subLayout);
		addComponent(total);

		Button saveButton = new Button(bundle.getString("default.button.save.changes"));
		saveButton.addClickListener(new SaveGood(binder, businessReceiptItem.getBean(), view));

		HorizontalLayout buttonLayout = new SpacedHorizontalLayout();
		buttonLayout.setMargin(true);
		buttonLayout.setWidth("100%");
		buttonLayout.addComponent(saveButton);
		buttonLayout.setComponentAlignment(saveButton, Alignment.BOTTOM_RIGHT);

		addComponent(buttonLayout);

	}

	private void addReceiptItem(final List<ReceiptItem> receiptItems, final ReceiptItem item, final Layout layout, final PriceListener priceListener) {

		final TextField totalPriceField = new TextField("Summa");
		totalPriceField.setImmediate(true);
		totalPriceField.setRequired(true);
		totalPriceField.setReadOnly(true);
		totalPriceField.setWidth(100f, Sizeable.Unit.PIXELS);

		final LocalPriceListener localPriceListener = new LocalPriceListener(totalPriceField, item);

		priceListener.update();
		localPriceListener.update();

		BeanItem<ReceiptItem> beanItem = new BeanItem<>(item);

		TextField nameField = new TextField("Nosaukums", beanItem.getItemProperty("name"));
		nameField.setImmediate(true);
		nameField.setRequired(true);
		nameField.setWidth(100f, Sizeable.Unit.PIXELS);
		nameField.setNullRepresentation("");

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
				item.setPrice(0.0);
				priceField.setValue(String.valueOf(0.0));
			}
			priceListener.update();
			localPriceListener.update();
		});

		final TextField countField = new TextField("Daudzums", beanItem.getItemProperty("count"));
		countField.setImmediate(true);
		countField.setRequired(true);
		countField.setWidth(100f, Sizeable.Unit.PIXELS);
		countField.setConverter(new StringToIntConverter());
		countField.addValueChangeListener((Property.ValueChangeListener) event -> {
			try {
				countField.validate();
			} catch (Validator.InvalidValueException e) {
				Notification.show("Nepareizs daudzums!", Notification.Type.ERROR_MESSAGE);
				item.setCount(1);
				countField.setValue(String.valueOf(1));
			}
			priceListener.update();
			localPriceListener.update();
		});

		Button deleteButton = new Button(bundle.getString("default.button.delete"));
		deleteButton.setStyleName("small");
		if (receiptItems.size() == 1) {
			deleteButton.setEnabled(false);
		}

		final HorizontalLayout subLayout = new SpacedHorizontalLayout(nameField, priceField, countField, totalPriceField, deleteButton);
		subLayout.setWidth("100%");
		subLayout.setComponentAlignment(priceField, Alignment.BOTTOM_RIGHT);
		subLayout.setComponentAlignment(countField, Alignment.BOTTOM_RIGHT);
		subLayout.setComponentAlignment(deleteButton, Alignment.BOTTOM_RIGHT);

		deleteButton.addClickListener((Button.ClickListener) event -> {
			receiptItems.remove(item);
			layout.removeComponent(subLayout);
			priceListener.update();
		});

		layout.addComponent(subLayout);

	}

	private class SaveGood extends SaveOnClick<BusinessReceipt> {

		private SaveGood(FieldGroup binder, BusinessReceipt entity, AbstractView view) {
			super(binder, entity, view);
		}

		@Override
		public void businessMethod() {
			BusinessReceiptService service = view.getUi().getBusinessReceiptService();
			if (entity.getId() == 0) {
				entity.setNumber(view.getUi().getBusinessReceiptService().generateName());
				service.createBusinessReceipt(entity);
			} else {
				service.updateBusinessReceipt(entity);
			}
		}
	}

	private class PriceListener {
		Label label;
		List<ReceiptItem> list;

		private PriceListener(Label label, List<ReceiptItem> list) {
			this.label = label;
			this.list = list;
		}

		private void update() {
			BigDecimal total = new BigDecimal("0");
			for (ReceiptItem receiptItem : list) {
				total = total.add(receiptItem.getTotalPrice());
			}
			label.setValue("Kopā: " + total + "€");
		}
	}

	private class LocalPriceListener {
		TextField field;
		ReceiptItem item;

		private LocalPriceListener(TextField field, ReceiptItem item) {
			this.field = field;
			this.item = item;
		}

		private void update() {
			field.setReadOnly(false);
			field.setValue(item.getTotalPrice().toString());
			field.setReadOnly(true);
		}
	}
	
}
