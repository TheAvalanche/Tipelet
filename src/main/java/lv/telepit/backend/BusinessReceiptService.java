package lv.telepit.backend;

import com.vaadin.ui.Notification;
import lv.telepit.backend.criteria.BusinessReceiptCriteria;
import lv.telepit.backend.dao.BusinessReceiptDao;
import lv.telepit.backend.dao.BusinessReceiptDaoImpl;
import lv.telepit.model.BusinessReceipt;
import lv.telepit.model.ReceiptItem;

import javax.persistence.OptimisticLockException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class BusinessReceiptService {
	private BusinessReceiptDao businessReceiptDao;

	private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");

	public BusinessReceiptService() {
		this.businessReceiptDao = new BusinessReceiptDaoImpl();
	}

	public void createBusinessReceipt(BusinessReceipt good) {
		try {
			for (ReceiptItem receiptItem : good.getReceiptItems()) {
				receiptItem.setParent(good);
			}

			businessReceiptDao.createBusinessReceipt(good);
		} catch (OptimisticLockException e) {
			catchOptimisticLockException();
		}
	}

	public void updateBusinessReceipt(BusinessReceipt good) {
		try {
			for (ReceiptItem receiptItem : good.getReceiptItems()) {
				receiptItem.setParent(good);
			}

			businessReceiptDao.updateBusinessReceipt(good);
		} catch (OptimisticLockException e) {
			catchOptimisticLockException();
		}
	}

	public void deleteBusinessReceipt(BusinessReceipt receipt) {
		receipt.setDeleted(true);
		try {
			businessReceiptDao.updateBusinessReceipt(receipt);
		} catch (OptimisticLockException e) {
			catchOptimisticLockException();
		}
	}

	public List<BusinessReceipt> findBusinessReceipt(Map<BusinessReceiptCriteria, Object> criteriaMap) {
		return businessReceiptDao.findBusinessReceipts(criteriaMap);
	}
	
	public String generateName() {
		return DateTimeFormatter.ofPattern("ddMMyyyy").format(LocalDate.now()) + "-" + (countReceiptsForToday() + 1);
	}
	
	public int countReceiptsForToday() {
		Map<BusinessReceiptCriteria, Object> criteriaMap = new HashMap<>();
		criteriaMap.put(BusinessReceiptCriteria.DATE_FROM, Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		criteriaMap.put(BusinessReceiptCriteria.DATE_TO, Date.from(LocalDate.now().plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		
		return businessReceiptDao.findBusinessReceipts(criteriaMap).size();
	}

	private void catchOptimisticLockException() {
		Notification.show(bundle.getString("exception.lock.header"), bundle.getString("exception.lock.message"), Notification.Type.ERROR_MESSAGE);
	}
}