package lv.telepit.backend.dao;

import lv.telepit.backend.criteria.StockGoodCriteria;
import lv.telepit.model.ChangeRecord;
import lv.telepit.model.StockGood;

import java.util.List;
import java.util.Map;

/**
 * Created by Alex on 07/04/2014.
 */
public interface StockDao {
    void createGood(StockGood good);

    void updateGood(StockGood good);

    void deleteGood(StockGood good);

    List<StockGood> getAllGoods();

    List<StockGood> findGoods(Map<StockGoodCriteria, Object> criteriaMap);

    List<ChangeRecord> findChanges(StockGood stockGood);
}
