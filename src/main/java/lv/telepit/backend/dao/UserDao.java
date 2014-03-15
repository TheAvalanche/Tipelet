package lv.telepit.backend.dao;

import lv.telepit.model.User;

import java.util.List;

/**
 * Created by Alex on 21/02/14.
 */
public interface UserDao {
    void createUser(User user) throws Exception;

    void updateUser(User user) throws Exception;

    List<User> getAllUsers();

    User getUser(String username, String password);
}
