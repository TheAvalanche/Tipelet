package lv.telepit.backend.dao;

import lv.telepit.model.StockGood;

/**
 * Created by Alex on 07/04/2014.
 */
public interface StockDao {
    void createGood(StockGood good);

    void updateGood(StockGood good);

    void deleteGood(StockGood good);
}
