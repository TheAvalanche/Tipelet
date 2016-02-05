package lv.telepit.ui.view.context.actions;

import lv.telepit.model.ServiceGood;
import lv.telepit.model.ServiceStatus;
import lv.telepit.ui.view.AbstractView;
import org.vaadin.dialogs.ConfirmDialog;

public class ServiceStatusReturnedWithBillAction extends AbstractAction {

    private final ServiceGood good;

    public ServiceStatusReturnedWithBillAction(ServiceGood good, AbstractView view) {
        super(view);
        if (view.getUi().getCurrentUser().isAccessToBillOnly()) {
            this.setCaption(bundle.getString("service.status.returned"));
        } else {
            this.setCaption(bundle.getString("service.status.returned.with.bill"));
        }
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
                        good.setWithBill(Boolean.TRUE);
                        view.getUi().getServiceGoodService().changeStatus(good, ServiceStatus.RETURNED);
                        view.refreshView();
                    }
                }
        );

    }
}
