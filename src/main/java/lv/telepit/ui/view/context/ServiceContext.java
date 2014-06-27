package lv.telepit.ui.view.context;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.Action;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import lv.telepit.model.ChangeRecord;
import lv.telepit.model.ServiceGood;
import lv.telepit.model.ServiceStatus;
import lv.telepit.ui.view.ServiceView;
import org.vaadin.dialogs.ConfirmDialog;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;


/**
 * Created by Alex on 30/03/2014.
 */
public class ServiceContext implements Action.Handler {

    private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");

    private final Action inWaiting = new Action(bundle.getString("service.status.waiting"));
    private final Action inRepair = new Action(bundle.getString("service.status.in.repair"));
    private final Action repaired = new Action(bundle.getString("service.status.repaired"));
    private final Action broken = new Action(bundle.getString("service.status.broken"));
    private final Action returned = new Action(bundle.getString("service.status.returned"));
    private final Action onDetails = new Action(bundle.getString("service.status.on.details"));
    private final Action showHistory = new Action(bundle.getString("show.history"));



    private ServiceView view;

    public ServiceContext(ServiceView view) {
        this.view = view;
    }

    @Override
    public Action[] getActions(Object target, Object sender) {
        if (target == null) return new Action[]{};

        switch (((ServiceGood) target).getStatus()) {

            case WAITING:
                return new Action[] {inRepair, showHistory};
            case IN_REPAIR:
                return new Action[] {repaired, broken, showHistory};
            case REPAIRED:
            case BROKEN:
                return new Action[] {returned, onDetails, showHistory};
            case RETURNED:
            case ON_DETAILS:
                return new Action[] {showHistory};
            default:
                return new Action[]{inWaiting, inRepair, repaired, broken, returned, onDetails, showHistory};
        }
    }

    @Override
    public void handleAction(final Action action, final Object sender, final Object target) {
        if (action == inWaiting) {
            changeStatusIfConfirmed(target, ServiceStatus.WAITING);
        } else if (action == inRepair) {
            changeStatusIfConfirmed(target, ServiceStatus.IN_REPAIR);
        } else if (action == repaired) {
            changeStatusIfConfirmed(target, ServiceStatus.REPAIRED);
        } else if (action == broken) {
            changeStatusIfConfirmed(target, ServiceStatus.BROKEN);
        } else if (action == returned) {
            changeStatusIfConfirmed(target, ServiceStatus.RETURNED);
        } else if (action == onDetails) {
            changeStatusIfConfirmed(target, ServiceStatus.ON_DETAILS);
        } else if (action == showHistory) {
            showHistory((ServiceGood) target);
            view.refreshView();
        }
    }

    private void changeStatusIfConfirmed(final Object fromObject, final ServiceStatus toStatus){
        ConfirmDialog.show(view.getUi(),
                bundle.getString("service.view.changeStatus.header"),
                String.format(bundle.getString("service.view.changeStatus.message"), ((ServiceGood) fromObject).getStatus().toString(), toStatus.toString()),
                bundle.getString("default.button.ok"), bundle.getString("default.button.cancel"), new ConfirmDialog.Listener() {
                    @Override
                    public void onClose(ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            view.getUi().getServiceGoodService().changeStatus((ServiceGood) fromObject, toStatus);
                            view.refreshView();
                        }
                    }
                }
        );
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
            table.setColumnHeaders(bundle.getString("history.property"), bundle.getString("history.old"), bundle.getString("history.new"));
            table.setColumnExpandRatio("name", 0.33f);
            table.setColumnExpandRatio("oldValue", 0.33f);
            table.setColumnExpandRatio("newValue", 0.33f);
            table.setPageLength(0);
            table.setWidth("100%");

            VerticalLayout panelLayout = new VerticalLayout();
            panelLayout.addComponent(new Label("<b>" + new SimpleDateFormat("dd-MM-YYYY HH:mm").format(record.getDate())
                    + ": " + record.getUser().getName() + " "
                    + record.getUser().getSurname() + "</b><br/>", ContentMode.HTML));
            panelLayout.addComponent(table);
            panel.setContent(panelLayout);
            layout.addComponent(panel);
        }

        subWindow.setContent(layout);
    }
}
