package lv.telepit.backend.criteria;

import javax.persistence.Query;

/**
 * Created by Alex on 21/05/2014.
 */
public interface Criteria {

    public void setQuery(StringBuilder query);

    public void setValue(Query q, Object value);
}
