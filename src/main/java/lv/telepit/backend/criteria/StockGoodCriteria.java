package lv.telepit.backend.criteria;

import javax.persistence.Query;

/**
 * Created by Alex on 08/04/2014.
 */
public enum StockGoodCriteria implements Criteria {

    ID, NAME, CATEGORY, USER, STORE;

    public void setQuery(StringBuilder query) {
        switch (this) {

            case ID:
                query.append("lower(cast(sg.customId as text)) like :customId ");
                break;
            case NAME:
                query.append("lower(sg.name) like :name ");
                break;
            case CATEGORY:
                query.append("sg.category.id in :idList");
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

            case ID:
                q.setParameter("customId", value + "%");
                break;
            case NAME:
                q.setParameter("name", "%" + value + "%");
                break;
            case CATEGORY:
                q.setParameter("idList", value);
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
