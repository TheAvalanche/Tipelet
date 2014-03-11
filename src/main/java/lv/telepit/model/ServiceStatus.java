package lv.telepit.model;

import java.util.ResourceBundle;

/**
 * Created by Alex on 11/03/14.
 */
public enum ServiceStatus {

    WAITING, IN_REPAIR, REPAIRED, BROKEN, RETURNED, ON_DETAILS;

    private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");


    @Override
    public String toString() {
        switch (this) {

            case WAITING:
                return bundle.getString("service.status.waiting");
            case IN_REPAIR:
                return bundle.getString("service.status.in.repair");
            case REPAIRED:
                return bundle.getString("service.status.repaired");
            case BROKEN:
                return bundle.getString("service.status.broken");
            case RETURNED:
                return bundle.getString("service.status.returned");
            case ON_DETAILS:
                return bundle.getString("service.status.on.details");
        }
        return name();
    }
}
