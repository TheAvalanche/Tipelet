package lv.telepit.ui.view.context.actions;

import lv.telepit.model.ServiceGood;
import lv.telepit.model.ServiceStatus;
import lv.telepit.ui.view.ServiceView;
import org.vaadin.dialogs.ConfirmDialog;

public class ServiceStatusRepairedAction extends AbstractAction<ServiceView> {

    private final ServiceGood good;

    public ServiceStatusRepairedAction(ServiceGood good, ServiceView view) {
        super(view);
        this.setCaption(bundle.getString("service.status.repaired"));
        this.good = good;
    }

    @Override
    public boolean show() {
        return good.getStatus() == ServiceStatus.IN_REPAIR;
    }

    @Override
    public void execute() {
        ConfirmDialog.show(view.getUi(),
                bundle.getString("service.view.changeStatus.header"),
                String.format(bundle.getString("service.view.changeStatus.message"), good.getStatus().toString(), ServiceStatus.REPAIRED.toString()),
                bundle.getString("default.button.ok"), bundle.getString("default.button.cancel"), (ConfirmDialog.Listener) dialog -> {
                    if (dialog.isConfirmed()) {
                        view.getUi().getServiceGoodService().changeStatus(good, ServiceStatus.REPAIRED);
                        view.refreshView();
                    }
                }
        );
    }
}
