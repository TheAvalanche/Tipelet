package lv.telepit.backend.criteria;

import javax.persistence.Query;

/**
 * Created by Alex on 05/03/14.
 */
public enum ServiceGoodCriteria {
    NAME, STATUS, CATEGORY, IMEI, ACCUM_NUM, DELIVERED_DATE_FROM, RETURNED_DATE_FROM, USER, STORE;

    public void setQuery(StringBuilder query) {
        switch (this) {

            case NAME:
                query.append("lower(sg.name) like :name ");
                break;
            case STATUS:
                query.append("sg.status = :status ");
                break;
            case CATEGORY:
                query.append("sg.category.id in :idList");
                break;
            case IMEI:
                query.append("lower(sg.imei) like :imei ");
                break;
            case ACCUM_NUM:
                query.append("lower(sg.accumNum) like :accumNum ");
                break;
            case DELIVERED_DATE_FROM:
                query.append("sg.deliveredDate >= :deliveredDateFrom ");
                break;
            case RETURNED_DATE_FROM:
                query.append("sg.returnedDate >= :returnedDateFrom ");
                break;
            case USER:
                query.append("sg.user = :user ");
                break;
            case STORE:
                query.append("sg.store = :store ");
                break;

        }

    }

    public void setValue(Query q, Object value) {
        switch (this) {

            case NAME:
                q.setParameter("name", "%" + value + "%");
                break;
            case STATUS:
                q.setParameter("status", value);
                break;
            case CATEGORY:
                q.setParameter("idList", value);
                break;
            case IMEI:
                q.setParameter("imei", "%" + value + "%");
                break;
            case ACCUM_NUM:
                q.setParameter("accumNum", "%" + value + "%");
                break;
            case DELIVERED_DATE_FROM:
                q.setParameter("deliveredDateFrom", value);
                break;
            case RETURNED_DATE_FROM:
                q.setParameter("returnedDateFrom", value);
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
