package lv.telepit.backend;

import com.vaadin.ui.Notification;
import lv.telepit.backend.criteria.SoldItemCriteria;
import lv.telepit.backend.criteria.StockGoodCriteria;
import lv.telepit.backend.dao.StockDao;
import lv.telepit.backend.dao.StockDaoImpl;
import lv.telepit.model.ChangeRecord;
import lv.telepit.model.SoldItem;
import lv.telepit.model.StockGood;
import lv.telepit.model.dto.ReportData;

import javax.persistence.OptimisticLockException;
import java.util.*;

/**
 * Created by Alex on 07/04/2014.
 */
public class StockService {
    private StockDao stockDao;

    private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");

    public StockService() {
        this.stockDao = new StockDaoImpl();
    }

    public void createGood(StockGood good) {
        try {
            stockDao.createGood(good);
        } catch (OptimisticLockException e) {
            catchOptimisticLockException();
        }
    }

    public void updateGood(StockGood good) {
        try {
            stockDao.updateGood(good);
        } catch (OptimisticLockException e) {
            catchOptimisticLockException();
        }
    }

    public void deleteGood(StockGood good) {
        try {
            stockDao.deleteGood(good);
        } catch (OptimisticLockException e) {
            catchOptimisticLockException();
        }
    }

    public List<StockGood> findGoods(Map<StockGoodCriteria, Object> criteriaMap) {
        return stockDao.findGoods(criteriaMap);
    }

    public List<ChangeRecord> findChanges(StockGood stockGood) {
        return stockDao.findChanges(stockGood);
    }

    public void sell(StockGood stockGood, List<SoldItem> soldItems) {
        for (SoldItem soldItem : soldItems) {
            soldItem.setParent(stockGood);
            soldItem.setSoldDate(new Date());
            stockGood.getChange().addChange("sold",
                    "", soldItem.getPrice().toString() + "€"
                            + " | " + (soldItem.isWithBill() ? "+" : "-")
                            + (soldItem.getCode() != null ? " | " + soldItem.getCode() : "")
                            + (soldItem.getInfo() != null ? " | (" + soldItem.getInfo() + ")" : ""));
        }
        stockGood.getSoldItemList().addAll(soldItems);
        if (!stockGood.isOrdered()) {
            stockGood.setCount(stockGood.getCount() - soldItems.size());
            stockGood.setLastSoldDate(new Date());
        }
        try {
            stockDao.updateGood(stockGood);
        } catch (OptimisticLockException e) {
            catchOptimisticLockException();
        }
    }

    public List<ReportData> findReports(Map<SoldItemCriteria, Object> map) {
        List<SoldItem> soldItems = stockDao.findSoldItems(map);
        List<ReportData> list = new ArrayList<>(soldItems.size());
        list.addAll(ReportData.constructFromSoldItems(soldItems));
        return list;
    }

    private void catchOptimisticLockException() {
        Notification.show(bundle.getString("exception.lock.header"), bundle.getString("exception.lock.message"), Notification.Type.ERROR_MESSAGE);
    }
}
