package lv.telepit.ui.view.context.actions;

import lv.telepit.model.ServiceGood;
import lv.telepit.model.ServiceStatus;
import lv.telepit.ui.view.AbstractView;
import org.vaadin.dialogs.ConfirmDialog;

public class ServiceStatusInRepairAction extends AbstractAction {

    private final ServiceGood good;

    public ServiceStatusInRepairAction(ServiceGood good, AbstractView view) {
        super(view);
        this.setCaption(bundle.getString("service.status.in.repair"));
        this.good = good;
    }

    @Override
    public boolean show() {
        return good.getStatus() == ServiceStatus.WAITING;
    }

    @Override
    public void execute() {

        ConfirmDialog.show(view.getUi(),
                bundle.getString("service.view.changeStatus.header"),
                String.format(bundle.getString("service.view.changeStatus.message"), good.getStatus().toString(), ServiceStatus.IN_REPAIR.toString()),
                bundle.getString("default.button.ok"), bundle.getString("default.button.cancel"), (ConfirmDialog.Listener) dialog -> {
                    if (dialog.isConfirmed()) {
                        view.getUi().getServiceGoodService().changeStatus(good, ServiceStatus.IN_REPAIR);
                        view.refreshView();
                    }
                }
        );

    }
}
