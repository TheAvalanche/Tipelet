package lv.telepit.ui.view;

import com.google.common.base.Strings;
import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import lv.telepit.TelepitUI;
import lv.telepit.model.Category;

import java.util.List;
import java.util.Random;

/**
 * Created by Alex on 11/03/14.
 */
public class CategoryView extends AbstractView {

    private TextField nameField;
    private Tree tree;
    private Button addRoot;
    private Button addChildren;

    public CategoryView(Navigator navigator, TelepitUI ui) {
        super(navigator, ui);
    }

    @Override
    public void buildContent() {

        nameField = new TextField("Kategorijas Nosaukums");
        nameField.setImmediate(true);
        nameField.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.EAGER);

        tree = new Tree("Kategorijas");

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
                ui.getCategoryService().addOrUpdateCategory(c);
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
                ui.getCategoryService().addOrUpdateCategory(c);
                refreshView();
            }
        });

        tree.addValueChangeListener(new EditCategoryListener());
        tree.addItemClickListener(new EditCategoryListener());
        nameField.addTextChangeListener(new EditCategoryListener());
        nameField.addValueChangeListener(new EditCategoryListener());

        content.addComponent(tree);
        content.addComponent(nameField);
        content.addComponent(addRoot);
        content.addComponent(addChildren);

        refreshView();
    }

    @Override
    public void refreshView() {
        List<Category> categories = ui.getCategoryService().getAllCategories();
        for (Category category : categories) {
            tree.addItem(category);
            tree.setItemCaption(category, category.getName());

            loadChildren(category);
            tree.expandItemsRecursively(category);
        }
        nameField.setValue("");
        addRoot.setEnabled(false);
        addChildren.setEnabled(false);

    }

    private void loadChildren(Category parent) {

        if (parent.getChildren().isEmpty()) {
            tree.setChildrenAllowed(parent, false);
            return;
        }

        for (Category category : parent.getChildren()) {
            tree.addItem(category);
            tree.setParent(category, parent);
            tree.setItemCaption(category, category.getName());
            loadChildren(category);
        }
        tree.setChildrenAllowed(parent, true);
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
                addChildren.setEnabled(true);
            } else {
                addChildren.setEnabled(false);
            }
        }
    }
}
