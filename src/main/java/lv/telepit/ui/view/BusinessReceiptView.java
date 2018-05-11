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
import com.vaadin.ui.themes.Reindeer;
import lv.telepit.TelepitUI;
import lv.telepit.backend.criteria.BusinessReceiptCriteria;
import lv.telepit.model.BusinessReceipt;
import lv.telepit.ui.component.CommonTable;
import lv.telepit.ui.component.Hr;
import lv.telepit.ui.component.SpacedHorizontalLayout;
import lv.telepit.ui.form.BusinessReceiptForm;
import lv.telepit.ui.form.fields.FieldFactory;
import lv.telepit.ui.view.context.BusinessReceiptContext;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusinessReceiptView extends AbstractView {

	private Button addBusinessReceipt;
	private Button updateBusinessReceipt;

	private TextField idField;
	private TextField receiverNameField;
	private ComboBox userField;
	private ComboBox storeField;
	private DateField fromDateField;
	private DateField toDateField;
	private Button expandButton;
	private Button searchButton;
	private Button resetButton;

	private Label label;
	private Button refreshButton;
	private Table table;
	private BeanItemContainer<BusinessReceipt> container;

	private Window subWindow;
	private ResetListener resetListener;

	public BusinessReceiptView(Navigator navigator, TelepitUI ui, String name) {
		super(navigator, ui, name);
	}

	@Override
	public void buildContent() {
		label = new Label(bundle.getString("businessReceipt.label"));
		label.setContentMode(ContentMode.HTML);

		idField = FieldFactory.getTextField("search.businessReceipt.id");
		receiverNameField = FieldFactory.getTextField("search.businessReceipt.receiverName");
		userField = FieldFactory.getUserComboBox("search.user");
		storeField = FieldFactory.getStoreComboBox("search.store");
		fromDateField = FieldFactory.getDateField("search.businessReceipt.fromDate");
		toDateField = FieldFactory.getDateField("search.businessReceipt.toDate");

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

		container = new BeanItemContainer<>(BusinessReceipt.class);
		table = new CommonTable(container, "businessReceipt", "id", "store", "date", "receiverName");
		table.setCellStyleGenerator((Table.CellStyleGenerator) (source, itemId, propertyId) -> {
			BusinessReceipt br = container.getItem(itemId).getBean();
			return "";
		});
		table.addItemClickListener(new EditBusinessReceiptListener());
		table.addValueChangeListener(new EditBusinessReceiptListener());
		table.addActionHandler(new BusinessReceiptContext(this));


		addBusinessReceipt = new Button(bundle.getString("default.button.add"));
		addBusinessReceipt.setIcon(new ThemeResource("img/add.png"));
		addBusinessReceipt.setWidth("150");
		addBusinessReceipt.addClickListener(new EditBusinessReceiptListener());

		updateBusinessReceipt = new Button(bundle.getString("default.button.update"));
		updateBusinessReceipt.setIcon(new ThemeResource("img/update.png"));
		updateBusinessReceipt.setWidth("150");
		updateBusinessReceipt.setEnabled(false);
		updateBusinessReceipt.addClickListener(new EditBusinessReceiptListener());

		final HorizontalLayout searchLayout1 = new SpacedHorizontalLayout(idField, userField, storeField);
		final HorizontalLayout searchLayout2 = new SpacedHorizontalLayout(receiverNameField, fromDateField, toDateField);

		final VerticalLayout searchLayout = new VerticalLayout(new Hr(), searchLayout1, searchLayout2,
				new SpacedHorizontalLayout(searchButton, resetButton), new Hr());
		searchLayout.setSpacing(true);
		searchLayout.setVisible(true);

		expandButton = new Button(bundle.getString("show.hide.search"));
		expandButton.setStyleName(Reindeer.BUTTON_LINK);
		expandButton.addClickListener((Button.ClickListener) event -> searchLayout.setVisible(!searchLayout.isVisible()));

		final HorizontalLayout headerLayout = new SpacedHorizontalLayout(label, expandButton);
		headerLayout.setWidth("1200px");
		headerLayout.setComponentAlignment(expandButton, Alignment.BOTTOM_RIGHT);
		
		final HorizontalLayout buttonLayout = new SpacedHorizontalLayout(addBusinessReceipt, updateBusinessReceipt, refreshButton);
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

	public void refreshView(List<BusinessReceipt> businessReceipts) {

		ui.removeWindow(subWindow);
		updateBusinessReceipt.setEnabled(false);

		if (businessReceipts == null) {
			businessReceipts = ui.getBusinessReceiptService().findBusinessReceipt(buildMap());
		}
		container.removeAllItems();
		container.addAll(businessReceipts);
		container.sort(new Object[]{"id"}, new boolean[]{false});
		table.refreshRowCache();
	}

	@Override
	public void reset() {
		resetListener.buttonClick(null);
	}

	@Override
	public void checkAuthority() {

	}

	private class EditBusinessReceiptListener implements Button.ClickListener, ItemClickEvent.ItemClickListener, Property.ValueChangeListener {
		@Override
		public void buttonClick(Button.ClickEvent clickEvent) {
			if (clickEvent.getButton() == addBusinessReceipt) {
				openBusinessReceiptForm(new BeanItem<>(new BusinessReceipt()));
			} else if (clickEvent.getButton() == updateBusinessReceipt && table.getValue() != null){
				openBusinessReceiptForm(container.getItem(table.getValue()));
			}
		}

		@Override
		public void itemClick(ItemClickEvent event) {

			if (event.isDoubleClick() && event.getItem() != null) {
				openBusinessReceiptForm((BeanItem<BusinessReceipt>) event.getItem());
			}
		}

		@Override
		public void valueChange(Property.ValueChangeEvent event) {
			updateBusinessReceipt.setEnabled(table.getValue() != null);
		}

		private void openBusinessReceiptForm(BeanItem<BusinessReceipt> businessReceipt) {

			subWindow = new Window();
			subWindow.setModal(true);
			subWindow.setHeight("800px");
			subWindow.setWidth("800px");
			subWindow.setClosable(true);
			ui.addWindow(subWindow);

			final VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.setSpacing(true);

			layout.addComponent(new BusinessReceiptForm(businessReceipt, BusinessReceiptView.this));

			subWindow.setContent(layout);
		}
	}


	private class RefreshListener implements Button.ClickListener {
		@Override
		public void buttonClick(Button.ClickEvent event) {
			refreshView();
		}
	}

	private class ResetListener implements Button.ClickListener {
		@Override
		public void buttonClick(Button.ClickEvent clickEvent) {
			idField.setValue(null);
			receiverNameField.setValue(null);
			userField.setValue(null);
			storeField.setValue(ui.getCurrentUser().isAdmin() ? null : ui.getCurrentUser().getStore());
			fromDateField.setValue(DateUtils.addMonths(new Date(), -1));
			toDateField.setValue(null);
		}
	}

	private class SearchListener implements Button.ClickListener {
		@Override
		public void buttonClick(Button.ClickEvent event) {
			List<BusinessReceipt> list = ui.getBusinessReceiptService().findBusinessReceipt(buildMap());
			refreshView(list);
		}
	}

	private Map<BusinessReceiptCriteria, Object> buildMap() {
		Map<BusinessReceiptCriteria, Object> map = new HashMap<>();
		if (!Strings.isNullOrEmpty(idField.getValue())) {
			map.put(BusinessReceiptCriteria.ID, idField.getValue().trim().toLowerCase());
		}
		if (!Strings.isNullOrEmpty(receiverNameField.getValue())) {
			map.put(BusinessReceiptCriteria.RECEIVER_NAME, receiverNameField.getValue().trim().toLowerCase());
		}
		if (userField.getValue() != null) {
			map.put(BusinessReceiptCriteria.USER, userField.getValue());
		}
		if (storeField.getValue() != null) {
			map.put(BusinessReceiptCriteria.STORE, storeField.getValue());
		}
		if (fromDateField.getValue() != null) {
			map.put(BusinessReceiptCriteria.DATE_FROM, DateUtils.truncate(fromDateField.getValue(), java.util.Calendar.DATE));
		}
		if (toDateField.getValue() != null) {
			map.put(BusinessReceiptCriteria.DATE_TO, DateUtils.truncate(toDateField.getValue(), java.util.Calendar.DATE));
		}

		return map;
	}
}
