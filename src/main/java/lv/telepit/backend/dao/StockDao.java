package lv.telepit.backend.dao;

import lv.telepit.backend.criteria.SoldItemCriteria;
import lv.telepit.backend.criteria.StockGoodCriteria;
import lv.telepit.model.ChangeRecord;
import lv.telepit.model.SoldItem;
import lv.telepit.model.StockGood;
import lv.telepit.model.Store;

import java.util.List;
import java.util.Map;

/**
 * Created by Alex on 07/04/2014.
 */
public interface StockDao {
    void createGood(StockGood good);

    void updateGood(StockGood good);

    void deleteGood(StockGood good);

    List<StockGood> findGoods(Map<StockGoodCriteria, Object> criteriaMap);

    List<ChangeRecord> findChanges(StockGood stockGood);

    List<SoldItem> findSoldItems(Map<SoldItemCriteria, Object> query);

    StockGood getByLinkAndStore(String link, Store store);
}
