package lv.telepit.backend;

import lv.telepit.backend.criteria.ServiceGoodCriteria;
import lv.telepit.backend.criteria.StockGoodCriteria;
import lv.telepit.backend.dao.StockDao;
import lv.telepit.backend.dao.StockDaoImpl;
import lv.telepit.model.ServiceGood;
import lv.telepit.model.StockGood;

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
        stockDao.createGood(good);
    }

    public void updateGood(StockGood good) {
        stockDao.updateGood(good);
    }

    public void deleteGood(StockGood good) {
        stockDao.deleteGood(good);
    }

    public List<StockGood> getAllGoods() {
        return stockDao.getAllGoods();
    }

    public List<StockGood> findGoods(Map<StockGoodCriteria, Object> criteriaMap) {
        return stockDao.findGoods(criteriaMap);
    }
}
