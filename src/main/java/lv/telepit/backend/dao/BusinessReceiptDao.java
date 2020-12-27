package lv.telepit.backend.dao;

import lv.telepit.backend.criteria.BusinessReceiptCriteria;
import lv.telepit.model.BusinessReceipt;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BusinessReceiptDao {

	void createBusinessReceipt(BusinessReceipt good);

	void updateBusinessReceipt(BusinessReceipt good);

	List<BusinessReceipt> findBusinessReceipts(Map<BusinessReceiptCriteria, Object> criteriaMap);

    Long lastReceiptNumber(String providerRegNum, Date dateFrom);
}
