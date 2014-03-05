package lv.telepit.backend.dao;

import lv.telepit.model.Store;

import java.util.List;

/**
 * Created by Alex on 24/02/14.
 */
public interface StoreDao {
    void createStore(Store store);

    void updateStore(Store store);

    List<Store> getAllStores();
}
