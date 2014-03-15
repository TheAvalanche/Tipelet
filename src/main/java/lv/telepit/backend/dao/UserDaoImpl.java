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
    public void createUser(User user) throws Exception {
        EntityManager em = emf.createEntityManager();

        Query q = em.createNamedQuery("User.getByLogin");
        q.setParameter("login", user.getLogin());
        List<User> duplicates = q.getResultList();
        if (!duplicates.isEmpty()) {
            throw new Exception("Lietotājs ar tādu loginu jau eksistē datubasē!");
        }

        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void updateUser(User user) throws Exception {
        EntityManager em = emf.createEntityManager();

        Query q = em.createNamedQuery("User.getByLogin");
        q.setParameter("login", user.getLogin());
        List<User> duplicates = q.getResultList();
        if (!duplicates.isEmpty()) {
            throw new Exception("Lietotājs ar tādu loginu jau eksistē datubasē!");
        }

        em.getTransaction().begin();
        em.merge(user);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public List<User> getAllUsers() {
        EntityManager em = emf.createEntityManager();
        Query q = em.createNamedQuery("User.getAll");
        List<User> users = q.getResultList();
        em.close();
        return users;
    }

    @Override
    public User getUser(String username, String password) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createNamedQuery("User.getByLoginAndPass");
        q.setParameter("login", username);
        q.setParameter("password", password);
        List<User> users = q.getResultList();
        em.close();
        if (users.isEmpty()) {
            return null;
        } else {
            return users.get(0);
        }
    }
}
