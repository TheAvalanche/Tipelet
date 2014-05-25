package lv.telepit.ui.form.fields;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.UI;
import lv.telepit.TelepitUI;
import lv.telepit.backend.CommonService;
import lv.telepit.model.User;

/**
 * Created by Alex on 04/03/14.
 */
public class SimpleUserComboBox extends ComboBox {
    /**Fields default width.*/
    private static final int DEFAULT_WIDTH = 200;
    /**Database usage.*/
    private CommonService commonService = ((TelepitUI) UI.getCurrent()).getCommonService();

    /**
     * Constructor.
     * @param caption caption
     * @param required if required
     */
    public SimpleUserComboBox(final String caption, final boolean required) {
        super(caption);
        setRequired(required);
        setWidth(DEFAULT_WIDTH, Sizeable.Unit.PIXELS);
        try {
            for (User user : commonService.getAllUsers()) {
                addItem(user);
                setItemCaption(user, user.getName() + " " + user.getSurname());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
