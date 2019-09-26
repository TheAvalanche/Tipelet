package lv.telepit.backend.criteria;

import javax.persistence.Query;

public enum ServiceGoodCriteria implements Criteria {
    ID, CUSTOM_ID, NAME, STATUS, CATEGORY, IMEI, ACCUM_NUM, DELIVERED_DATE_FROM, DELIVERED_DATE_TO, RETURNED_DATE_FROM, RETURNED_DATE_TO, USER, STORE, WITH_BILL, CONTACT_PHONE, CONTACT_NAME, DIAGNOSTICS;

    public void setQuery(StringBuilder query) {
        switch (this) {

            case ID:
                query.append("lower(cast(sg.id as text)) like :id ");
                break;
            case CUSTOM_ID:
                query.append("lower(cast(sg.customId as text)) like :customId ");
                break;
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
            case DELIVERED_DATE_TO:
                query.append("sg.deliveredDate <= :deliveredDateTo ");
                break;
            case RETURNED_DATE_TO:
                query.append("sg.returnedDate <= :returnedDateTo ");
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
            case WITH_BILL:
                query.append("sg.withBill = :withBill ");
                break;
            case CONTACT_NAME:
                query.append("lower(sg.contactName) like :contactName ");
                break;
            case CONTACT_PHONE:
                query.append("lower(sg.contactPhone) like :contactPhone ");
                break;
            case DIAGNOSTICS:
                query.append("sg.diagnostics > :diagnostics ");
                break;

        }

    }

    public void setValue(Query q, Object value) {
        switch (this) {

            case ID:
                q.setParameter("id", value + "%");
                break;
            case CUSTOM_ID:
                q.setParameter("customId", value + "%");
                break;
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
            case DELIVERED_DATE_TO:
                q.setParameter("deliveredDateTo", value);
                break;
            case RETURNED_DATE_TO:
                q.setParameter("returnedDateTo", value);
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
            case WITH_BILL:
                q.setParameter("withBill", value);
                break;
            case CONTACT_NAME:
                q.setParameter("contactName", "%" + value + "%");
                break;
            case CONTACT_PHONE:
                q.setParameter("contactPhone", "%" + value + "%");
                break;
            case DIAGNOSTICS:
                q.setParameter("diagnostics", value);
                break;

        }
    }
}
