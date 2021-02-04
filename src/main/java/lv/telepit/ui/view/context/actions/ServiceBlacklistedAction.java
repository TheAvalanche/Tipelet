package lv.telepit.ui.view.context.actions;

import lv.telepit.model.ServiceGood;
import lv.telepit.model.ServiceStatus;
import lv.telepit.ui.view.ServiceView;

import java.util.Date;

public class ServiceBlacklistedAction extends AbstractAction<ServiceView> {

    private final ServiceGood good;

    public ServiceBlacklistedAction(ServiceGood good, ServiceView view) {
        super(view);
        if (!good.isBlacklisted()) {
            this.setCaption(bundle.getString("service.status.blacklisted"));
        } else {
            this.setCaption(bundle.getString("service.status.nonblacklisted"));
        }
        this.good = good;
    }

    @Override
    public boolean show() {
        return true;
    }

    @Override
    public void execute() {
        if (good.isBlacklisted()) {
            view.getUi().getServiceGoodService().unBlacklist(good.getContactPhone());
        } else {
            view.getUi().getServiceGoodService().blacklist(good.getContactPhone());
        }
    }
}
