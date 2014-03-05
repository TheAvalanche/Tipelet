package lv.telepit.backend.criteria;

import javax.persistence.Query;

/**
 * Created by Alex on 05/03/14.
 */
public enum ServiceGoodCriteria {
    NAME, IMEI, ACCUM_NUM, DELIVERED_DATE, RETURNED_DATE, USER, STORE;

    public void setQuery(StringBuilder query) {

    }

    public void setValue(Query q, Object value) {

    }
}
