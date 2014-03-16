package lv.telepit.backend.dao;

import lv.telepit.model.Store;

import java.util.List;

/**
 * Created by Alex on 24/02/14.
 */
public interface StoreDao {
    void createStore(Store store) throws Exception;

    void updateStore(Store store) throws Exception;

    List<Store> getAllStores();
}
