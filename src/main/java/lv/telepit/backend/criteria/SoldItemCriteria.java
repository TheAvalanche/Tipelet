package lv.telepit.backend.criteria;

import javax.persistence.Query;

public enum SoldItemCriteria implements Criteria {

    DATE_FROM, DATE_TO, USER, STORE, WITH_BILL;

    @Override
    public void setQuery(StringBuilder query) {
        switch (this) {

            case DATE_FROM:
                query.append("si.soldDate >= :soldDateFrom ");
                break;
            case DATE_TO:
                query.append("si.soldDate <= :soldDateTo ");
                break;
            case USER:
                query.append("si.user = :user ");
                break;
            case STORE:
                query.append("si.store = :store ");
                break;
            case WITH_BILL:
                query.append("si.withBill = :withBill ");
                break;
        }

    }

    @Override
    public void setValue(Query q, Object value) {
        switch (this) {

            case DATE_FROM:
                q.setParameter("soldDateFrom", value);
                break;
            case DATE_TO:
                q.setParameter("soldDateTo", value);
                break;
            case USER:
                q.setParameter("user", value);
                break;
            case STORE:
                q.setParameter("store", value);
                break;
            case WITH_BILL:
                q.setParameter("withBill", value);
                break;
        }

    }
}
