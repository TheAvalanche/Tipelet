package lv.telepit.backend.dao;

import lv.telepit.backend.PersistenceProvider;
import lv.telepit.model.Store;
import lv.telepit.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by Alex on 24/02/14.
 */
public class StoreDaoImpl implements StoreDao {

    private EntityManagerFactory emf;

    public StoreDaoImpl() {

        emf = PersistenceProvider.getInstance().getEntityManagerFactory();

    }

    @Override
    public void createStore(Store store) throws Exception {
        EntityManager em = emf.createEntityManager();

        Query q = em.createNamedQuery("Store.getByName");
        q.setParameter("name", store.getName());
        List<Store> duplicates = q.getResultList();
        if (!duplicates.isEmpty()) {
            throw new Exception("Veikals ar tādu nosaukumu jau eksistē datubasē!");
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
            throw new Exception("Veikals ar tādu nosaukumu jau eksistē datubasē!");
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
}
