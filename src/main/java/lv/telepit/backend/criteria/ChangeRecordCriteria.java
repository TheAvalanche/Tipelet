package lv.telepit.backend.criteria;

import javax.persistence.Query;

/**
 * Created by Alex on 12/05/2014.
 */
public enum ChangeRecordCriteria implements Criteria {

    USER, DATE_FROM, DATE_TO, NAME, STORE;

    public void setQuery(StringBuilder query) {
        switch (this) {

            case DATE_FROM:
                query.append("cr.date >= :dateFrom ");
                break;
            case DATE_TO:
                query.append("cr.date <= :dateTo ");
                break;
            case NAME: //todo working script and filter by type
                query.append("(lower(cr.stockGood.name) like :name or lower(cr.serviceGood.name) like :name) ");
                break;
            case USER:
                query.append("cr.user = :user ");
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
            case NAME:
                q.setParameter("name", "%" + value + "%");
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
