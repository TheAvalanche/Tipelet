package lv.telepit.ui.view.context.actions;

import lv.telepit.model.ServiceGood;
import lv.telepit.model.ServiceStatus;
import lv.telepit.ui.view.AbstractView;
import org.vaadin.dialogs.ConfirmDialog;

import java.util.Date;

public class ServiceCalledAction extends AbstractAction {

    private final ServiceGood good;

    public ServiceCalledAction(ServiceGood good, AbstractView view) {
        super(view);
        if (!good.isCalled()) {
            this.setCaption(bundle.getString("service.status.called"));
        } else {
            this.setCaption(bundle.getString("service.status.noncalled"));
        }
        this.good = good;
    }

    @Override
    public boolean show() {
        return good.getStatus() == ServiceStatus.REPAIRED || good.getStatus() == ServiceStatus.BROKEN;
    }

    @Override
    public void execute() {
        good.setCalled(!good.isCalled());
        good.setCalledDate(new Date());
        view.getUi().getServiceGoodService().updateGood(good);

    }
}
