package lv.telepit.backend.dao;

import lv.telepit.backend.PersistenceProvider;
import lv.telepit.backend.criteria.StockGoodCriteria;
import lv.telepit.model.ChangeRecord;
import lv.telepit.model.StockGood;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

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

    @Override
    public List<StockGood> findGoods(Map<StockGoodCriteria, Object> criteriaMap) {
        EntityManager em = emf.createEntityManager();
        StringBuilder queryBuilder = new StringBuilder("select sg from StockGood sg where 1=1");
        if (criteriaMap != null) {
            for (Map.Entry<StockGoodCriteria, Object> entry : criteriaMap.entrySet()) {
                queryBuilder.append(" and ");
                entry.getKey().setQuery(queryBuilder);
            }
        }
        final Query q = em.createQuery(queryBuilder.toString());
        if (criteriaMap != null) {
            for (Map.Entry<StockGoodCriteria, Object> entry : criteriaMap.entrySet()) {
                entry.getKey().setValue(q, entry.getValue());
            }
        }
        q.setMaxResults(100);
        List<StockGood> list = q.getResultList();
        em.close();
        return list;
    }

    @Override
    public List<ChangeRecord> findChanges(StockGood stockGood) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createNamedQuery("ChangeRecord.findForStockGood");
        q.setParameter("stockGood", stockGood);
        return q.getResultList();
    }
}
