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
import lv.telepit.TelepitUI;
import lv.telepit.model.Store;
import lv.telepit.model.User;
import lv.telepit.ui.component.Hr;
import lv.telepit.ui.component.PropertyFilter;
import lv.telepit.ui.form.UserForm;
import lv.telepit.ui.form.fields.FieldFactory;
import org.vaadin.dialogs.ConfirmDialog;

import java.util.List;

/**
 * Created by Alex on 21/02/14.
 */
@SuppressWarnings("unchecked")
public class UserView extends AbstractView {

    private Button addUser;
    private Button updateUser;
    private Button deleteUser;
    private TextField filterName;
    private TextField filterSurname;
    private ComboBox filterStore;
    private CheckBox filterDeleted;
    private Button filterButton;
    private Button refreshButton;
    private Table table;
    private BeanItemContainer<User> container;
    private Label label;
    private Window subWindow;

    public UserView(Navigator navigator, TelepitUI ui, String name) {

        super(navigator, ui, name);

    }

    public void buildContent() {

        label = new Label(bundle.getString("user.view.label"));
        label.setContentMode(ContentMode.HTML);

        filterStore = FieldFactory.getStoreComboBox("search.store");
        filterName = FieldFactory.getTextField("search.user.name");
        filterSurname = FieldFactory.getTextField("search.user.surname");
        filterDeleted = FieldFactory.getCheckBox("search.user.deleted");

        filterButton = new Button(bundle.getString("default.button.filter"));
        filterButton.addClickListener(new FilterUser());
        filterButton.setIcon(new ThemeResource("img/filter.png"));

        refreshButton = new Button();
        refreshButton.addClickListener(new RefreshListener());
        refreshButton.setIcon(new ThemeResource("img/refresh.png"));

        container = new BeanItemContainer<>(User.class);
        table = new Table();
        table.setWidth("700px");
        table.setImmediate(true);
        table.setContainerDataSource(container);
        table.setVisibleColumns("name", "surname", "login", "phone", "admin", "store");
        table.setColumnHeaders(
                bundle.getString("user.name"),
                bundle.getString("user.surname"),
                bundle.getString("user.login"),
                bundle.getString("user.phone"),
                bundle.getString("user.admin"),
                bundle.getString("user.store"));
        table.setSelectable(true);
        table.setImmediate(true);
        table.addItemClickListener(new EditUserListener());
        table.addValueChangeListener(new EditUserListener());

        addUser = new Button(bundle.getString("default.button.add"));
        addUser.setIcon(new ThemeResource("img/add.png"));
        addUser.setWidth("150");
        addUser.addClickListener(new EditUserListener());

        updateUser = new Button(bundle.getString("default.button.update"));
        updateUser.setIcon(new ThemeResource("img/update.png"));
        updateUser.setWidth("150");
        updateUser.setEnabled(false);
        updateUser.addClickListener(new EditUserListener());

        deleteUser = new Button(bundle.getString("default.button.delete"));
        deleteUser.setIcon(new ThemeResource("img/delete.png"));
        deleteUser.setWidth("150");
        deleteUser.setEnabled(false);
        deleteUser.addClickListener(new EditUserListener());

        final HorizontalLayout searchLayout = new HorizontalLayout(filterName, filterSurname, filterStore, filterDeleted, filterButton);
        searchLayout.setSpacing(true);
        searchLayout.setComponentAlignment(filterDeleted, Alignment.BOTTOM_CENTER);
        searchLayout.setComponentAlignment(filterButton, Alignment.BOTTOM_RIGHT);

        final HorizontalLayout searchButtonLayout = new HorizontalLayout(filterButton, refreshButton);
        searchButtonLayout.setSpacing(true);
        searchButtonLayout.setWidth("700px");
        searchButtonLayout.setComponentAlignment(refreshButton, Alignment.BOTTOM_RIGHT);

        final VerticalLayout buttonLayout = new VerticalLayout(addUser, updateUser, deleteUser);
        buttonLayout.setSpacing(true);

        final HorizontalLayout tableButtonsLayout = new HorizontalLayout(table, buttonLayout);
        tableButtonsLayout.setSpacing(true);

        content.addComponent(label);
        content.addComponent(new Hr());
        content.addComponent(searchLayout);
        content.addComponent(new Hr());
        content.addComponent(searchButtonLayout);
        content.addComponent(tableButtonsLayout);
    }

    public void refreshView() {

        filterName.setValue("");
        filterSurname.setValue("");
        filterDeleted.setValue(false);
        filterStore.setValue(null);
        container.removeAllContainerFilters();

        ui.removeWindow(subWindow);
        updateUser.setEnabled(false);
        deleteUser.setEnabled(false);

        List<User> users = ui.getCommonService().getAllUsers();
        container.removeAllItems();

        container.addAll(users);
        table.refreshRowCache();

        container.addContainerFilter(new PropertyFilter<>("deleted", Boolean.FALSE));
    }

    @Override
    public void checkAuthority() {

    }

    private class EditUserListener implements Button.ClickListener, ItemClickEvent.ItemClickListener, Property.ValueChangeListener {
        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {
            if (clickEvent.getButton() == addUser) {
                openUserForm(new BeanItem<>(new User()));
            } else if (clickEvent.getButton() == updateUser && table.getValue() != null){
                openUserForm(container.getItem(table.getValue()));
            } else if (clickEvent.getButton() == deleteUser && table.getValue() != null) {
                final User userToDelete = container.getItem(table.getValue()).getBean();
                ConfirmDialog.show(ui,
                        bundle.getString("user.view.delete.header"),
                        bundle.getString("user.view.delete.message"),
                        bundle.getString("default.button.ok"), bundle.getString("default.button.cancel"), new ConfirmDialog.Listener() {
                    @Override
                    public void onClose(ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            try {
                                ui.getCommonService().deleteUser(userToDelete);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            refreshView();
                        }
                    }
                });
            }
        }

        @Override
        public void itemClick(ItemClickEvent event) {

            if (event.isDoubleClick() && event.getItem() != null) {
                openUserForm((BeanItem<User>) event.getItem());
            }
        }

        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            updateUser.setEnabled(table.getValue() != null);
            deleteUser.setEnabled(table.getValue() != null);
        }

        private void openUserForm(BeanItem<User> user) {

            subWindow = new Window();
            subWindow.setModal(true);
            subWindow.setHeight("400px");
            subWindow.setWidth("450px");
            subWindow.setClosable(true);
            ui.addWindow(subWindow);

            final VerticalLayout layout = new VerticalLayout();
            layout.setMargin(true);
            layout.setSpacing(true);

            layout.addComponent(new UserForm(user, UserView.this));

            subWindow.setContent(layout);
        }
    }

    private class FilterUser implements Button.ClickListener {


        private PropertyFilter<Store> storeFilter;
        private PropertyFilter<String> nameFilter;
        private PropertyFilter<String> surnameFilter;
        private PropertyFilter<Boolean> deletedFilter;


        @Override
        public void buttonClick(Button.ClickEvent event) {
            container.removeAllContainerFilters();

            if (!Strings.isNullOrEmpty(filterName.getValue())) {
                nameFilter = new PropertyFilter<>("name", filterName.getValue());
                container.addContainerFilter(nameFilter);
            }
            if (!Strings.isNullOrEmpty(filterSurname.getValue())) {
                surnameFilter = new PropertyFilter<>("surname", filterSurname.getValue());
                container.addContainerFilter(surnameFilter);
            }
            if (filterStore.getValue() != null) {
                storeFilter = new PropertyFilter<>("store", (Store) filterStore.getValue());
                container.addContainerFilter(storeFilter);
            }
            deletedFilter = new PropertyFilter<>("deleted", filterDeleted.getValue());
            container.addContainerFilter(deletedFilter);

        }
    }

    private class RefreshListener implements Button.ClickListener {
        @Override
        public void buttonClick(Button.ClickEvent event) {
            refreshView();
        }
    }
}
