package lv.telepit.backend.dao;

import lv.telepit.model.Category;
import lv.telepit.model.Store;
import lv.telepit.model.User;

import java.util.List;

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
}
