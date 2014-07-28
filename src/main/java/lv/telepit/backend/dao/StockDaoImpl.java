package lv.telepit.backend.dao;

import com.vaadin.ui.UI;
import lv.telepit.TelepitUI;
import lv.telepit.backend.PersistenceProvider;
import lv.telepit.backend.criteria.SoldItemCriteria;
import lv.telepit.backend.criteria.StockGoodCriteria;
import lv.telepit.model.ChangeRecord;
import lv.telepit.model.SoldItem;
import lv.telepit.model.StockGood;
import lv.telepit.model.Store;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.Date;
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

        if (!good.getChange().getChangeList().isEmpty()) {
            ChangeRecord cr = good.getChange();
            cr.setStockGood(good);
            cr.setDate(new Date());
            cr.setUser(((TelepitUI) UI.getCurrent()).getCurrentUser());
            em.persist(cr);
        }

        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void updateGood(StockGood good) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(good);

        if (!good.getChange().getChangeList().isEmpty()) {
            ChangeRecord cr = good.getChange();
            cr.setStockGood(good);
            cr.setDate(new Date());
            cr.setUser(((TelepitUI) UI.getCurrent()).getCurrentUser());
            em.persist(cr);
        }

        em.getTransaction().commit();
        em.close();
    }

    @Override
    public StockGood getByLinkAndStore(String link, Store store) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createNamedQuery("StockGood.findByLinkAndStore");
        q.setParameter("link", link);
        q.setParameter("store", store);
        List<StockGood> goods = q.getResultList();
        em.close();
        if (goods.isEmpty()) {
            return null;
        }
        return goods.get(0);
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
        q.setMaxResults(1000);
        List<StockGood> list = q.getResultList();
        em.close();
        return list;
    }

    @Override
    public List<ChangeRecord> findChanges(StockGood stockGood) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createNamedQuery("ChangeRecord.findForStockGood");
        q.setParameter("stockGood", stockGood);
        q.setMaxResults(100);
        List<ChangeRecord> changeRecords =  q.getResultList();
        em.close();
        return changeRecords;
    }

    @Override
    public List<SoldItem> findSoldItems(Map<SoldItemCriteria, Object> query) {
        EntityManager em = emf.createEntityManager();
        StringBuilder queryBuilder = new StringBuilder("select si from SoldItem si where 1=1");
        if (query != null) {
            for (Map.Entry<SoldItemCriteria, Object> entry : query.entrySet()) {
                queryBuilder.append(" and ");
                entry.getKey().setQuery(queryBuilder);
            }
        }
        final Query q = em.createQuery(queryBuilder.toString());
        if (query != null) {
            for (Map.Entry<SoldItemCriteria, Object> entry : query.entrySet()) {
                entry.getKey().setValue(q, entry.getValue());
            }
        }
        q.setMaxResults(1000);
        List<SoldItem> list = q.getResultList();
        em.close();
        return list;
    }
}
