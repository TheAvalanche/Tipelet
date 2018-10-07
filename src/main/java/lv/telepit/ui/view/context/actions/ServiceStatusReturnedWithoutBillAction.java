package lv.telepit.ui.view.context.actions;

import lv.telepit.model.ServiceGood;
import lv.telepit.model.ServiceStatus;
import lv.telepit.ui.view.ServiceView;
import org.vaadin.dialogs.ConfirmDialog;

public class ServiceStatusReturnedWithoutBillAction extends AbstractAction<ServiceView> {

    private final ServiceGood good;

    public ServiceStatusReturnedWithoutBillAction(ServiceGood good, ServiceView view) {
        super(view);
        this.setCaption(bundle.getString("service.status.returned.without.bill"));
        this.good = good;
    }

    @Override
    public boolean show() {
        return good.getStatus() == ServiceStatus.REPAIRED || good.getStatus() == ServiceStatus.BROKEN;
    }

    @Override
    public void execute() {
        ConfirmDialog.show(view.getUi(),
                bundle.getString("service.view.changeStatus.header"),
                String.format(bundle.getString("service.view.changeStatus.message"), good.getStatus().toString(), ServiceStatus.RETURNED.toString()),
                bundle.getString("default.button.ok"), bundle.getString("default.button.cancel"), (ConfirmDialog.Listener) dialog -> {
                    if (dialog.isConfirmed()) {
                        good.setWithBill(Boolean.FALSE);
                        view.getUi().getServiceGoodService().changeStatus(good, ServiceStatus.RETURNED);
                        view.refreshView();
                    }
                }
        );

    }
}
