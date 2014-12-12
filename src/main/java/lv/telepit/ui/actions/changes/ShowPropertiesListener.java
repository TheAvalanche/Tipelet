package lv.telepit.ui.actions.changes;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import lv.telepit.model.ChangeRecord;
import lv.telepit.model.dto.RecordData;
import lv.telepit.ui.view.AbstractView;

import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class ShowPropertiesListener implements ItemClickEvent.ItemClickListener {

    private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");

    private final AbstractView view;

    public ShowPropertiesListener(AbstractView view) {
        this.view = view;
    }

    @Override
    public void itemClick(ItemClickEvent event) {
        if (event.isDoubleClick() && event.getItem() != null) {
            RecordData recordData = ((BeanItem<RecordData>) event.getItem()).getBean();
            Window subWindow = new Window();
            subWindow.setModal(true);
            subWindow.setHeight("650px");
            subWindow.setWidth("700px");
            subWindow.setClosable(true);
            view.getUi().addWindow(subWindow);


            final VerticalLayout layout = new VerticalLayout();
            layout.setMargin(true);
            layout.setSpacing(true);

            Panel panel = new Panel();
            BeanItemContainer<ChangeRecord.PropertyChange> container = new BeanItemContainer<>(ChangeRecord.PropertyChange.class, recordData.getPropertyChanges());
            Table table = new Table();
            table.setContainerDataSource(container);
            table.setVisibleColumns("name", "oldValue", "newValue");
            table.setColumnHeaders(bundle.getString("history.property"), bundle.getString("history.old"), bundle.getString("history.new"));
            table.setColumnExpandRatio("name", 0.33f);
            table.setColumnExpandRatio("oldValue", 0.33f);
            table.setColumnExpandRatio("newValue", 0.33f);
            table.setPageLength(0);
            table.setWidth("100%");

            VerticalLayout panelLayout = new VerticalLayout();
            panelLayout.addComponent(new Label("<b>" + new SimpleDateFormat("dd-MM-YYYY HH:mm").format(recordData.getDate())
                    + ": " + recordData.getUser() + "</b><br/>", ContentMode.HTML));
            panelLayout.addComponent(table);
            panel.setContent(panelLayout);
            layout.addComponent(panel);


            subWindow.setContent(layout);
        }
    }
}