package lv.telepit.ui.view.context.actions;

import lv.telepit.model.ServiceGood;
import lv.telepit.ui.view.AbstractView;

public class ServiceStatusInWaitingAction extends AbstractAction {

    private final ServiceGood good;

    public ServiceStatusInWaitingAction(ServiceGood good, AbstractView view) {
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
