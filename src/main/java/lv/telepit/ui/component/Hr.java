package lv.telepit.ui.component;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

/**
 * Created by Alex on 16/03/14.
 */
public class Hr extends Label {
    public Hr() {
        super("<hr/>");
        setContentMode(ContentMode.HTML);
    }
}
