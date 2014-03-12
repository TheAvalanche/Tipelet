package lv.telepit.backend.dao;

import lv.telepit.backend.PersistenceProvider;
import lv.telepit.model.Store;

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
    public void createStore(Store store) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(store);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void updateStore(Store store) {
        EntityManager em = emf.createEntityManager();
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
