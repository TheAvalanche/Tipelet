package lv.telepit.backend;

import lv.telepit.backend.criteria.ChangeRecordCriteria;
import lv.telepit.backend.dao.CommonDao;
import lv.telepit.backend.dao.CommonDaoImpl;
import lv.telepit.model.Category;
import lv.telepit.model.ChangeRecord;
import lv.telepit.model.Store;
import lv.telepit.model.User;
import lv.telepit.model.dto.RecordData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Alex on 21/02/14.
 */
public class CommonService {

    private CommonDao commonDao;

    public CommonService() {
        this.commonDao = new CommonDaoImpl();
    }

    public void saveUser(User user) throws Exception {
        commonDao.createUser(user);
    }

    public void updateUser(User user) throws Exception {
        commonDao.updateUser(user);
    }

    public void deleteUser(User user) throws Exception {
        user.setDeleted(true);
        commonDao.updateUser(user);
    }

    public User getUser(String username, String password) {
        return commonDao.getUser(username, password);
    }

    public List<User> getAllUsers() {
        return commonDao.getAllUsers();
    }

    public void saveStore(Store store) throws Exception {
        commonDao.createStore(store);
    }

    public void updateStore(Store store) throws Exception {
        commonDao.updateStore(store);
    }

    public void deleteStore(Store store) throws Exception {
        store.setDeleted(true);
        commonDao.updateStore(store);
    }

    public List<Store> getAllStores() {
        return commonDao.getAllStores();
    }

    public void addOrUpdateCategory(Category category) {
        commonDao.addOrUpdateCategory(category);
    }

    public List<Category> getAllCategories() {
        return commonDao.getAllCategories();
    }

    public void removeCategory(Category category) {
        commonDao.removeCategory(category);
    }

    public List<RecordData> findRecords(Map<ChangeRecordCriteria, Object> query) {
        List<ChangeRecord> records = commonDao.findRecords(query);
        List<RecordData> list = new ArrayList<>(records.size());
        for (ChangeRecord cr : records) {
            list.addAll(RecordData.construct(cr));
        }
        return list;
    }

    public List<ChangeRecord> findChangeRecords(Map<ChangeRecordCriteria, Object> query) {
        return commonDao.findRecords(query);
    }

}
