package lv.telepit.backend.criteria;

import javax.persistence.Query;

/**
 * Created by Alex on 21/05/2014.
 */
public enum SoldItemCriteria implements Criteria {

    DATE_FROM, DATE_TO, USER, STORE;

    @Override
    public void setQuery(StringBuilder query) {

    }

    @Override
    public void setValue(Query q, Object value) {

    }
}
