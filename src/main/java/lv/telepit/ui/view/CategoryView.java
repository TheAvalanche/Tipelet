package lv.telepit.ui.view;

import com.google.common.base.Strings;
import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.Navigator;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import lv.telepit.TelepitUI;
import lv.telepit.model.Category;
import lv.telepit.ui.component.Hr;
import org.vaadin.dialogs.ConfirmDialog;

import java.util.List;

/**
 * Created by Alex on 11/03/14.
 */
public class CategoryView extends AbstractView {

    private TextField nameField;
    private Tree tree;
    private Label label;
    private Button addRoot;
    private Button addChildren;
    private Button changeChildren;
    private Button removeChildren;

    public CategoryView(Navigator navigator, TelepitUI ui, String name) {
        super(navigator, ui, name);
    }

    @Override
    public void buildContent() {

        label = new Label(bundle.getString("category.view.label"));
        label.setContentMode(ContentMode.HTML);

        nameField = new TextField("Kategorijas Nosaukums");
        nameField.setImmediate(true);
        nameField.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.EAGER);

        tree = new Tree();
        tree.setImmediate(true);

        addRoot = new Button("Pievienot Kategoriju");
        addRoot.setEnabled(false);
        addRoot.setImmediate(true);
        addRoot.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (Strings.isNullOrEmpty(nameField.getValue())) {
                    return;
                }
                Category c = Category.createCategories();
                c.setName(nameField.getValue());
                ui.getCommonService().addOrUpdateCategory(c);
                refreshView();
            }
        });

        addChildren = new Button("Pievienot Apakškategoriju");
        addChildren.setEnabled(false);
        addChildren.setImmediate(true);
        addChildren.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (Strings.isNullOrEmpty(nameField.getValue()) || tree.getValue() == null) {
                    return;
                }
                Category c = ((Category) tree.getValue()).addCategory(nameField.getValue());
                ui.getCommonService().addOrUpdateCategory(c);
                refreshView();
            }
        });

        changeChildren = new Button("Izmainīt nosaukumu");
        changeChildren.setEnabled(false);
        changeChildren.setImmediate(true);
        changeChildren.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (Strings.isNullOrEmpty(nameField.getValue()) || tree.getValue() == null) {
                    return;
                }
                Category c = (Category) tree.getValue();
                c.setName(nameField.getValue());
                ui.getCommonService().addOrUpdateCategory(c);
                refreshView();
            }
        });

        removeChildren = new Button("Nodzēst Kategoriju");
        removeChildren.setEnabled(false);
        removeChildren.setImmediate(true);
        removeChildren.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (tree.getValue() == null) {
                    return;
                }
                ConfirmDialog.show(ui,
                        bundle.getString("category.view.delete.header"),
                        bundle.getString("category.view.delete.message"),
                        bundle.getString("default.button.ok"), bundle.getString("default.button.cancel"), new ConfirmDialog.Listener() {
                            @Override
                            public void onClose(ConfirmDialog dialog) {
                                if (dialog.isConfirmed()) {
                                    Category c = (Category) tree.getValue();
                                    ui.getCommonService().removeCategory(c);
                                    refreshView();
                                }
                            }
                        }
                );
            }
        });

        tree.addValueChangeListener(new EditCategoryListener());
        tree.addItemClickListener(new EditCategoryListener());
        nameField.addTextChangeListener(new EditCategoryListener());
        nameField.addValueChangeListener(new EditCategoryListener());

        VerticalLayout controlLayout = new VerticalLayout(nameField, addRoot, addChildren, changeChildren, removeChildren);
        controlLayout.setSpacing(true);

        Panel treePanel = new Panel(tree);
        treePanel.setWidth("300px");
        treePanel.setHeight("100%");

        HorizontalLayout treeAndButtons = new HorizontalLayout(treePanel, controlLayout);
        treeAndButtons.setSpacing(true);
        treeAndButtons.setHeight("100%");

        content.addComponent(label);
        content.addComponent(new Hr());
        content.addComponent(treeAndButtons);

        refreshView();
    }

    @Override
    public void refreshView() {
        tree.removeAllItems();
        List<Category> categories = ui.getCommonService().getAllCategories();
        for (Category category : categories) {
            tree.addItem(category);
            tree.setItemCaption(category, category.getName());

            loadChildren(category);
            tree.expandItemsRecursively(category);
        }
        nameField.setValue("");
        addRoot.setEnabled(false);
        addChildren.setEnabled(false);
        removeChildren.setEnabled(false);
        changeChildren.setEnabled(false);
    }

    @Override
    public void checkAuthority() {

    }

    private void loadChildren(Category parent) {

        if (parent.getChildren().isEmpty()) {
            tree.setChildrenAllowed(parent, false);
            return;
        }

        tree.setChildrenAllowed(parent, true);
        for (Category category : parent.getChildren()) {
            tree.addItem(category);
            tree.setParent(category, parent);
            tree.setItemCaption(category, category.getName());
            loadChildren(category);
        }

    }

    private class EditCategoryListener implements Property.ValueChangeListener, ItemClickEvent.ItemClickListener, FieldEvents.TextChangeListener {
        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            editEnabled();
        }

        @Override
        public void textChange(FieldEvents.TextChangeEvent event) {
            nameField.setValue(event.getText());
            editEnabled();
        }

        @Override
        public void itemClick(ItemClickEvent event) {
            editEnabled();
        }

        private void editEnabled() {
            if (!Strings.isNullOrEmpty(nameField.getValue())) {
                addRoot.setEnabled(true);
            } else {
                addRoot.setEnabled(false);
            }

            if (tree.getValue() != null && !Strings.isNullOrEmpty(nameField.getValue())) {
                changeChildren.setEnabled(true);
                addChildren.setEnabled(true);
            } else {
                changeChildren.setEnabled(false);
                addChildren.setEnabled(false);
            }

            if (tree.getValue() != null) {
                removeChildren.setEnabled(true);
            } else {
                removeChildren.setEnabled(false);
            }
        }
    }
}
