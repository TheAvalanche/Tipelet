package lv.telepit.ui.form.fields;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.UI;
import lv.telepit.TelepitUI;
import lv.telepit.backend.CommonService;
import lv.telepit.model.Category;

/**
 * Created by Alex on 16/03/14.
 */
public class SimpleCategoryComboBox extends ComboBox {

    /**Fields default width.*/
    private static final int DEFAULT_WIDTH = 200;
    /**Database usage.*/
    private CommonService commonService = ((TelepitUI) UI.getCurrent()).getCommonService();

    public SimpleCategoryComboBox(final String caption, final boolean required) {
        super(caption);
        setRequired(required);
        setWidth(DEFAULT_WIDTH, Unit.PIXELS);
        try {
            for (Category category : commonService.getAllCategories()) {
                addChildren(category);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addChildren(Category parent) {
        addItem(parent);
        setItemCaption(parent, parent.getTreeName());
        if (!parent.getChildren().isEmpty()) {
            for (Category child : parent.getChildren()) {
                addChildren(child);
            }
        }
    }
}
