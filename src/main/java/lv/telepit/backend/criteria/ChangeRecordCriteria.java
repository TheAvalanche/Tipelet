package lv.telepit.backend.criteria;

import javax.persistence.Query;

public enum ChangeRecordCriteria implements Criteria {

    USER, DATE_FROM, DATE_TO, STORE, TYPE_STOCK, TYPE_SERVICE;

    public void setQuery(StringBuilder query) {
        switch (this) {

            case DATE_FROM:
                query.append("cr.date >= :dateFrom ");
                break;
            case DATE_TO:
                query.append("cr.date <= :dateTo ");
                break;
            case USER:
                query.append("cr.user = :user ");
                break;
            case TYPE_STOCK:
                query.append("cr.stockGood is not null ");
                break;
            case TYPE_SERVICE:
                query.append("cr.serviceGood is not null ");
                break;
            case STORE:
                query.append("cr.user.store = :store ");
                break;
        }
    }

    public void setValue(Query q, Object value) {
        switch (this) {

            case DATE_FROM:
                q.setParameter("dateFrom", value);
                break;
            case DATE_TO:
                q.setParameter("dateTo", value);
                break;
            case USER:
                q.setParameter("user", value);
                break;
            case STORE:
                q.setParameter("store", value);
                break;
        }
    }
}
