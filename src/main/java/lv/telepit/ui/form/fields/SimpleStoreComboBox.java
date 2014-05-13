package lv.telepit.ui.form.fields;


import com.vaadin.ui.ComboBox;
import com.vaadin.ui.UI;
import lv.telepit.TelepitUI;
import lv.telepit.backend.CommonService;
import lv.telepit.model.Store;

/**
 * SimpleStoreComboBox.
 *
 * @author Alex Kartishev <aleksandrs.kartisevs@point.lv>
 *         Date: 13.5.7
 *         Time: 10:30
 */
@SuppressWarnings("FieldCanBeLocal")
public class SimpleStoreComboBox extends ComboBox {

    /**Fields default width.*/
    private static final int DEFAULT_WIDTH = 200;
    /**Database usage.*/
    private CommonService commonService = ((TelepitUI)UI.getCurrent()).getCommonService();

    /**
     * Constructor.
     * @param caption caption
     * @param required if required
     */
    public SimpleStoreComboBox(final String caption, final boolean required) {
        super(caption);
        setRequired(required);
        setWidth(DEFAULT_WIDTH, Unit.PIXELS);
        try {
            for (Store store : commonService.getAllStores()) {
                addItem(store);
                setItemCaption(store, store.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
