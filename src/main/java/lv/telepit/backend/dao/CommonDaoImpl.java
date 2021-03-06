package lv.telepit.backend.dao;

import lv.telepit.backend.PersistenceProvider;
import lv.telepit.backend.criteria.ChangeRecordCriteria;
import lv.telepit.model.Category;
import lv.telepit.model.ChangeRecord;
import lv.telepit.model.Store;
import lv.telepit.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Alex on 21/02/14.
 */
public class CommonDaoImpl implements CommonDao {

    private EntityManagerFactory emf;

    private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");

    public CommonDaoImpl() {

        emf = PersistenceProvider.getInstance().getEntityManagerFactory();

    }

    @Override
    public void createUser(User user) throws Exception {
        EntityManager em = emf.createEntityManager();

        Query q = em.createNamedQuery("User.getByLogin");
        q.setParameter("login", user.getLogin());
        List<User> duplicates = q.getResultList();
        if (!duplicates.isEmpty()) {
            throw new Exception(bundle.getString("exception.user.exists"));
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
        if (!duplicates.isEmpty() && duplicates.get(0).getId() != user.getId()) {
            throw new Exception(bundle.getString("exception.user.exists"));
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

    @Override
    public void createStore(Store store) throws Exception {
        EntityManager em = emf.createEntityManager();

        Query q = em.createNamedQuery("Store.getByName");
        q.setParameter("name", store.getName());
        List<Store> duplicates = q.getResultList();
        if (!duplicates.isEmpty()) {
            throw new Exception(bundle.getString("exception.store.exists"));
        }

        em.getTransaction().begin();
        em.persist(store);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void updateStore(Store store) throws Exception {
        EntityManager em = emf.createEntityManager();

        Query q = em.createNamedQuery("Store.getByName");
        q.setParameter("name", store.getName());
        List<Store> duplicates = q.getResultList();
        if (!duplicates.isEmpty() && duplicates.get(0).getId() != store.getId()) {
            throw new Exception(bundle.getString("exception.store.exists"));
        }

        em.getTransaction().begin();
        em.merge(store);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public List<Store> getAllStores() {
        EntityManager em = emf.createEntityManager();
        Query q = em.createNamedQuery("Store.getAll");
        List<Store> stores = q.getResultList();
        em.close();
        return stores;
    }

    @Override
    public void addOrUpdateCategory(Category category) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(category);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void removeCategory(Category category) {
        EntityManager em = emf.createEntityManager();
        Category c = em.find(Category.class, category.getId());
        loadChildren(c, em);
        removeChildren(c, em);
        em.close();
    }

    @Override
    public List<Category> getAllCategories() {
        EntityManager em = emf.createEntityManager();
        Query q = em.createNamedQuery("Category.getAll");
        List<Category> categories = q.getResultList();
        for (Category category : categories) {
            category.setTreeName(category.getName());
            category.getAllIds().add(category.getId());
            loadChildren(category, em);
        }
        em.close();
        return categories;
    }

    private void loadChildren(Category parent, EntityManager em) {
        Query q = em.createNamedQuery("Category.getChildren");
        q.setParameter("parent", parent);
        List<Category> categories = q.getResultList();
        if (!categories.isEmpty()) {
            parent.setChildren(categories);
            for (Category category : categories) {
                parent.getAllIds().add(category.getId());
                category.setTreeName(parent.getTreeName() + "/" + category.getName());
                category.getAllIds().add(category.getId());
                loadChildren(category, em);
            }
        }
    }

    private void removeChildren(Category parent, EntityManager em) {
        Query q = em.createNamedQuery("Category.getChildren");
        q.setParameter("parent", parent);
        List<Category> categories = q.getResultList();
        if (!categories.isEmpty()) {
            for (Category category : categories) {
                removeChildren(category, em);
            }
        }

        em.getTransaction().begin();
        em.remove(parent);
        em.getTransaction().commit();
    }

    @Override
    public List<ChangeRecord> findRecords(Map<ChangeRecordCriteria, Object> query) {
        EntityManager em = emf.createEntityManager();
        StringBuilder queryBuilder = new StringBuilder("select cr from ChangeRecord cr where 1=1");
        if (query != null) {
            for (Map.Entry<ChangeRecordCriteria, Object> entry : query.entrySet()) {
                queryBuilder.append(" and ");
                entry.getKey().setQuery(queryBuilder);
            }
        }
        queryBuilder.append(" order by cr.id desc");
        final Query q = em.createQuery(queryBuilder.toString());
        if (query != null) {
            for (Map.Entry<ChangeRecordCriteria, Object> entry : query.entrySet()) {
                entry.getKey().setValue(q, entry.getValue());
            }
        }
        q.setMaxResults(1000);
        List<ChangeRecord> list = q.getResultList();
        em.close();
        return list;
    }
}
