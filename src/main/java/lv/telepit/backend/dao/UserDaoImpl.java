package lv.telepit.backend.dao;

import lv.telepit.backend.PersistenceProvider;
import lv.telepit.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by Alex on 21/02/14.
 */
public class UserDaoImpl implements UserDao {

    private EntityManagerFactory emf;

    public UserDaoImpl() {

        emf = PersistenceProvider.getInstance().getEntityManagerFactory();

    }

    @Override
    public void createUser(User user) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
    }

    @Override
    public void updateUser(User user) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(user);
        em.getTransaction().commit();
    }

    @Override
    public List<User> getAllUsers() {
        EntityManager em = emf.createEntityManager();
        Query q = em.createNamedQuery("User.getAll");
        List<User> users = q.getResultList();
        return users;
    }

    @Override
    public User getUser(String username, String password) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createNamedQuery("User.getByLoginAndPass");
        q.setParameter("login", username);
        q.setParameter("password", password);
        List<User> users = q.getResultList();
        if (users.isEmpty()) {
            return null;
        } else {
            return users.get(0);
        }
    }
}
