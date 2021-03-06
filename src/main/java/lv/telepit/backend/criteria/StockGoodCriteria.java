package lv.telepit.backend.criteria;

import javax.persistence.Query;

public enum StockGoodCriteria implements Criteria {

    ID, INCREMENT_ID, NAME, MODEL, CATEGORY, USER, STORE, PRICE;

    public void setQuery(StringBuilder query) {
        switch (this) {

            case ID:
                query.append("lower(cast(sg.id as text)) like :id ");
                break;
            case INCREMENT_ID:
                query.append("lower(cast(sg.incrementId as text)) like :incrementId ");
                break;
            case NAME:
                query.append("lower(sg.name) like :name ");
                break;
            case MODEL:
                query.append("lower(sg.model) like :model ");
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
            case PRICE:
                query.append("lower(cast(sg.price as text)) like :price  ");
                break;

        }

    }

    public void setValue(Query q, Object value) {
        switch (this) {

            case ID:
                q.setParameter("id", value + "%");
                break;
            case INCREMENT_ID:
                q.setParameter("incrementId", value + "%");
                break;
            case NAME:
                q.setParameter("name", "%" + value + "%");
                break;
            case MODEL:
                q.setParameter("model", "%" + value + "%");
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
            case PRICE:
                q.setParameter("price", value + "%");
                break;
        }
    }
}
