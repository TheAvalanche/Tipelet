package lv.telepit.backend.dao;

import lv.telepit.backend.criteria.ChangeRecordCriteria;
import lv.telepit.model.Category;
import lv.telepit.model.ChangeRecord;
import lv.telepit.model.Store;
import lv.telepit.model.User;

import java.util.List;
import java.util.Map;

/**
 * Created by Alex on 21/02/14.
 */
public interface CommonDao {

    void createUser(User user) throws Exception;

    void updateUser(User user) throws Exception;

    List<User> getAllUsers();

    User getUser(String username, String password);

    void createStore(Store store) throws Exception;

    void updateStore(Store store) throws Exception;

    List<Store> getAllStores();

    void addOrUpdateCategory(Category category);

    void removeCategory(Category category);

    List<Category> getAllCategories();

    List<ChangeRecord> findRecords(Map<ChangeRecordCriteria, Object> query);
}
