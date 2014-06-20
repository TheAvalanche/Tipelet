package lv.telepit.backend;

import com.vaadin.ui.Notification;
import lv.telepit.backend.criteria.SoldItemCriteria;
import lv.telepit.backend.criteria.StockGoodCriteria;
import lv.telepit.backend.dao.StockDao;
import lv.telepit.backend.dao.StockDaoImpl;
import lv.telepit.model.ChangeRecord;
import lv.telepit.model.dto.ReportData;
import lv.telepit.model.SoldItem;
import lv.telepit.model.StockGood;

import javax.persistence.OptimisticLockException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Alex on 07/04/2014.
 */
public class StockService {
    private StockDao stockDao;

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
            soldItem.setPrice(stockGood.getPrice());
            soldItem.setSoldDate(new Date());
            stockGood.getChange().addChange("sold",
                    "", (soldItem.getCode() != null ? soldItem.getCode() : "-")
                            + " | " + soldItem.getPrice().toString()
                            + " | " + (soldItem.isWithBill() ? "+" : "-"));
        }
        stockGood.getSoldItemList().addAll(soldItems);
        stockGood.setCount(stockGood.getCount() - soldItems.size());
        stockGood.setLastSoldDate(new Date());
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
        Notification.show("Izmaiņas netiek saglabātas", "Šo priekšmetu tikai izamainīja cits lietotājs. Lūdzu atjaunojiet tabulu un atkārtojiet vēl reiz.", Notification.Type.ERROR_MESSAGE);
    }
}
