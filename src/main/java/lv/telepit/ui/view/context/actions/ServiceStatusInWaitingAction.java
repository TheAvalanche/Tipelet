package lv.telepit.ui.view.context.actions;

import lv.telepit.model.ServiceGood;
import lv.telepit.ui.view.ServiceView;

public class ServiceStatusInWaitingAction extends AbstractAction<ServiceView> {

    private final ServiceGood good;

    public ServiceStatusInWaitingAction(ServiceGood good, ServiceView view) {
        super(view);
        this.setCaption(bundle.getString("service.status.waiting"));
        this.good = good;
    }

    @Override
    public boolean show() {
        return false;
    }

    @Override
    public void execute() {

    }
}
