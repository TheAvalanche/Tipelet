package lv.telepit.ui.view.context;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.Action;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import lv.telepit.model.ChangeRecord;
import lv.telepit.model.ServiceGood;
import lv.telepit.model.ServiceStatus;
import lv.telepit.ui.form.ServiceGoodForm;
import lv.telepit.ui.view.ServiceView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;


/**
 * Created by Alex on 30/03/2014.
 */
public class ServiceContext implements Action.Handler {


    private final Action inWaiting = new Action("Gaida remontu");
    private final Action inRepair = new Action("Remontā");
    private final Action repaired = new Action("Saremontēts");
    private final Action broken = new Action("Salauzts");
    private final Action returned = new Action("Atgriezts");
    private final Action onDetails = new Action("Atdots uz detāļiem");
    private final Action showHistory = new Action("Radīt vēsturi");

    private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");

    private ServiceView view;

    public ServiceContext(ServiceView view) {
        this.view = view;
    }

    @Override
    public Action[] getActions(Object target, Object sender) {
        return new Action[]{inWaiting, inRepair, repaired, broken, returned, onDetails, showHistory};
    }

    @Override
    public void handleAction(Action action, Object sender, Object target) {
        if (action == inWaiting) {

        } else if (action == inRepair) {

        } else if (action == repaired) {

        } else if (action == broken) {

        } else if (action == returned) {

        } else if (action == onDetails) {

        } else if (action == showHistory) {
            showHistory((ServiceGood) target);
        }
    }

    private void inRepair(ServiceGood serviceGood) {
        serviceGood.setStatus(ServiceStatus.IN_REPAIR);
        serviceGood.setStartDate(new Date());
        view.getUi().getServiceGoodService().updateGood(serviceGood);
    }

    private void showHistory(ServiceGood serviceGood) {

        List<ChangeRecord> historyList = view.getUi().getServiceGoodService().findChanges(serviceGood);
        Window subWindow = new Window();
        subWindow.setModal(true);
        subWindow.setHeight("650px");
        subWindow.setWidth("700px");
        subWindow.setClosable(true);
        view.getUi().addWindow(subWindow);

        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        for (ChangeRecord record : historyList) {
            for (ChangeRecord.PropertyChange p : record.getChangeList()) {
                p.setName(bundle.getString(p.getName()));
            }
            Panel panel = new Panel();
            BeanItemContainer<ChangeRecord.PropertyChange> container = new BeanItemContainer<>(ChangeRecord.PropertyChange.class, record.getChangeList());
            Table table = new Table();
            table.setContainerDataSource(container);
            table.setVisibleColumns("name", "oldValue", "newValue");
            table.setColumnHeaders("Vertība", "Vecā vertība", "Jaunā vertība");
            table.setColumnExpandRatio("name", 0.33f);
            table.setColumnExpandRatio("oldValue", 0.33f);
            table.setColumnExpandRatio("newValue", 0.33f);
            table.setPageLength(0);
            table.setWidth("100%");

            VerticalLayout panelLayout = new VerticalLayout();
            panelLayout.addComponent(new Label("<b>" + new SimpleDateFormat("dd-MM-YYYY hh:mm").format(record.getDate()) + ": " + record.getUser().getName() + "</b><br/>", ContentMode.HTML));
            panelLayout.addComponent(table);
            panel.setContent(panelLayout);
            layout.addComponent(panel);
        }

        subWindow.setContent(layout);
    }
}
