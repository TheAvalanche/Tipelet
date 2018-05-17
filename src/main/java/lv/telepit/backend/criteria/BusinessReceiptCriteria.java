package lv.telepit.backend.criteria;

import javax.persistence.Query;

public enum BusinessReceiptCriteria implements Criteria {

	ID, NUMBER, STORE, USER, RECEIVER_NAME, DATE_FROM, DATE_TO;

	public void setQuery(StringBuilder query) {
		switch (this) {

			case ID:
				query.append("lower(cast(br.id as text)) like :id ");
				break;
			case STORE:
				query.append("br.user.store = :store ");
				break;
			case USER:
				query.append("br.user = :user ");
				break;
			case RECEIVER_NAME:
				query.append("lower(br.receiverName) like :receiverName ");
				break;
			case DATE_FROM:
				query.append("br.date >= :dateFrom ");
				break;
			case DATE_TO:
				query.append("br.date <= :dateTo ");
				break;
			case NUMBER:
				query.append("lower(br.number) like :number ");
				break;
		}

	}

	public void setValue(Query q, Object value) {
		switch (this) {

			case ID:
				q.setParameter("id", value + "%");
				break;
			case STORE:
				q.setParameter("store", value);
				break;
			case USER:
				q.setParameter("user", value);
				break;
			case RECEIVER_NAME:
				q.setParameter("receiverName", "%" + value + "%");
				break;
			case DATE_FROM:
				q.setParameter("dateFrom", value);
				break;
			case DATE_TO:
				q.setParameter("dateTo", value);
				break;
			case NUMBER:
				q.setParameter("number", value);
				break;
		}
	}
}
