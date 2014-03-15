package lv.telepit.backend;

import lv.telepit.backend.dao.UserDao;
import lv.telepit.backend.dao.UserDaoImpl;
import lv.telepit.model.User;

import java.util.List;

/**
 * Created by Alex on 21/02/14.
 */
public class UserService {

    private UserDao userDao;

    public UserService() {
        this.userDao = new UserDaoImpl();
    }

    public void saveUser(User user) throws Exception {
        userDao.createUser(user);
    }

    public void updateUser(User user) throws Exception {
        userDao.updateUser(user);
    }

    public void deleteUser(User user) throws Exception {
        user.setDeleted(true);
        userDao.updateUser(user);
    }

    public User getUser(String username, String password) {
        return userDao.getUser(username, password);
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

}
