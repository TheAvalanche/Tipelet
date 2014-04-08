package lv.telepit.backend.dao;

import com.vaadin.ui.UI;
import lv.telepit.TelepitUI;
import lv.telepit.backend.PersistenceProvider;
import lv.telepit.model.ChangeRecord;
import lv.telepit.model.ServiceGood;
import lv.telepit.model.StockGood;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * Created by Alex on 07/04/2014.
 */
public class StockDaoImpl implements StockDao {

    private EntityManagerFactory emf;

    public StockDaoImpl() {
        this.emf = PersistenceProvider.getInstance().getEntityManagerFactory();
    }

    @Override
    public void createGood(StockGood good) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(good);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void updateGood(StockGood good) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(good);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void deleteGood(StockGood good) {
        EntityManager em = emf.createEntityManager();
        good = em.find(StockGood.class, good.getId());
        em.getTransaction().begin();
        em.remove(good);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public List<StockGood> getAllGoods() {

        EntityManager em = emf.createEntityManager();
        Query q = em.createNamedQuery("StockGood.findAll");
        List<StockGood> list = q.getResultList();
        em.close();
        return list;
    }
}
