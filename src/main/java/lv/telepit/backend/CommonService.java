package lv.telepit.backend;

import com.vaadin.ui.Notification;
import lv.telepit.backend.criteria.ChangeRecordCriteria;
import lv.telepit.backend.dao.CommonDao;
import lv.telepit.backend.dao.CommonDaoImpl;
import lv.telepit.model.Category;
import lv.telepit.model.ChangeRecord;
import lv.telepit.model.Store;
import lv.telepit.model.User;
import lv.telepit.model.dto.RecordData;

import javax.persistence.OptimisticLockException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Alex on 21/02/14.
 */
public class CommonService {

    private CommonDao commonDao;

    private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");

    public CommonService() {
        this.commonDao = new CommonDaoImpl();
    }

    public void saveUser(User user) throws Exception {
        try {
            commonDao.createUser(user);
        } catch (OptimisticLockException e) {
            catchOptimisticLockException();
        }
    }

    public void updateUser(User user) throws Exception {
        try {
            commonDao.updateUser(user);
        } catch (OptimisticLockException e) {
            catchOptimisticLockException();
        }
    }

    public void deleteUser(User user) throws Exception {
        user.setDeleted(true);
        try {
            commonDao.updateUser(user);
        } catch (OptimisticLockException e) {
            catchOptimisticLockException();
        }
    }

    public User getUser(String username, String password) {
        return commonDao.getUser(username, password);
    }

    public List<User> getAllUsers() {
        return commonDao.getAllUsers();
    }

    public void saveStore(Store store) throws Exception {
        try {
            commonDao.createStore(store);
        } catch (OptimisticLockException e) {
            catchOptimisticLockException();
        }
    }

    public void updateStore(Store store) throws Exception {
        try {
            commonDao.updateStore(store);
        } catch (OptimisticLockException e) {
            catchOptimisticLockException();
        }
    }

    public void deleteStore(Store store) throws Exception {
        store.setDeleted(true);
        try {
            commonDao.updateStore(store);
        } catch (OptimisticLockException e) {
            catchOptimisticLockException();
        }
    }

    public List<Store> getAllStores() {
        return commonDao.getAllStores();
    }

    public void addOrUpdateCategory(Category category) {
        try {
            commonDao.addOrUpdateCategory(category);
        } catch (OptimisticLockException e) {
            catchOptimisticLockException();
        }
    }

    public List<Category> getAllCategories() {
        return commonDao.getAllCategories();
    }

    public void removeCategory(Category category) {
        try {
            commonDao.removeCategory(category);
        } catch (OptimisticLockException e) {
            catchOptimisticLockException();
        }
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

    private void catchOptimisticLockException() {
        Notification.show(bundle.getString("exception.lock.header"), bundle.getString("exception.lock.message"), Notification.Type.ERROR_MESSAGE);
    }

}
