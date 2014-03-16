package lv.telepit.backend;

import lv.telepit.backend.dao.StoreDao;
import lv.telepit.backend.dao.StoreDaoImpl;
import lv.telepit.model.Store;

import java.util.List;

/**
 * Created by Alex on 24/02/14.
 */
public class StoreService {

    private StoreDao storeDao;

    public StoreService() {
        this.storeDao = new StoreDaoImpl();
    }

    public void saveStore(Store store) throws Exception {
        storeDao.createStore(store);
    }

    public void updateStore(Store store) throws Exception {
        storeDao.updateStore(store);
    }

    public void deleteStore(Store store) throws Exception {
        store.setDeleted(true);
        storeDao.updateStore(store);
    }

    public List<Store> getAllStores() {
        return storeDao.getAllStores();
    }
}
