package lv.telepit.backend.criteria;

import javax.persistence.Query;

public enum BusinessReceiptCriteria implements Criteria {

	ID, NUMBER, STORE, USER, RECEIVER_NAME, RECEIVER_PHONE, RECEIVER_MAIL, DATE_FROM, DATE_TO, PROVIDER_REG_NUM;

	public void setQuery(StringBuilder query) {
		switch (this) {

			case ID:
				query.append("lower(cast(br.id as text)) like :id ");
				break;
			case STORE:
				query.append("br.store = :store ");
				break;
			case USER:
				query.append("br.user = :user ");
				break;
			case RECEIVER_NAME:
				query.append("lower(br.receiverName) like :receiverName ");
				break;
			case RECEIVER_PHONE:
				query.append("lower(br.receiverPhone) like :receiverPhone ");
				break;
			case RECEIVER_MAIL:
				query.append("lower(br.receiverMail) like :receiverMail ");
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
			case PROVIDER_REG_NUM:
				query.append("lower(br.providerRegNum) like :providerRegNum ");
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
			case RECEIVER_PHONE:
				q.setParameter("receiverPhone", "%" + value + "%");
				break;
			case RECEIVER_MAIL:
				q.setParameter("receiverMail", "%" + value + "%");
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
			case PROVIDER_REG_NUM:
				q.setParameter("providerRegNum", value);
				break;
		}
	}
}
