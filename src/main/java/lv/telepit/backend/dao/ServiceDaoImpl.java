package lv.telepit.backend.dao;

import com.vaadin.ui.UI;
import lv.telepit.TelepitUI;
import lv.telepit.backend.PersistenceProvider;
import lv.telepit.backend.criteria.ServiceGoodCriteria;
import lv.telepit.model.ChangeRecord;
import lv.telepit.model.ServiceGood;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Alex on 04/03/14.
 */
public class ServiceDaoImpl implements ServiceDao {

    private EntityManagerFactory emf;

    public ServiceDaoImpl() {

        emf = PersistenceProvider.getInstance().getEntityManagerFactory();

    }

    @Override
    public void createGood(ServiceGood good) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(good);

        if (!good.getChange().getChangeList().isEmpty()) {
            ChangeRecord cr = good.getChange();
            cr.setServiceGood(good);
            cr.setDate(new Date());
            cr.setUser(((TelepitUI) UI.getCurrent()).getCurrentUser());
            em.persist(cr);
        }

        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void updateGood(ServiceGood good) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(good);

        if (!good.getChange().getChangeList().isEmpty()) {
            ChangeRecord cr = good.getChange();
            cr.setServiceGood(good);
            cr.setDate(new Date());
            cr.setUser(((TelepitUI) UI.getCurrent()).getCurrentUser());
            em.persist(cr);
        }

        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void deleteGood(ServiceGood good) {
        EntityManager em = emf.createEntityManager();
        good = em.find(ServiceGood.class, good.getId());
        em.getTransaction().begin();
        em.remove(good);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public List<ServiceGood> findGoods(Map<ServiceGoodCriteria, Object> criteriaMap) {
        EntityManager em = emf.createEntityManager();
        StringBuilder queryBuilder = new StringBuilder("select sg from ServiceGood sg where 1=1");
        if (criteriaMap != null) {
            for (Map.Entry<ServiceGoodCriteria, Object> entry : criteriaMap.entrySet()) {
                queryBuilder.append(" and ");
                entry.getKey().setQuery(queryBuilder);
            }
        }
        queryBuilder.append(" order by sg.id desc");
        final Query q = em.createQuery(queryBuilder.toString());
        if (criteriaMap != null) {
            for (Map.Entry<ServiceGoodCriteria, Object> entry : criteriaMap.entrySet()) {
                entry.getKey().setValue(q, entry.getValue());
            }
        }
        q.setMaxResults(1000);
        List<ServiceGood> list = q.getResultList();
        em.close();
        return list;
    }

    @Override
    public List<ChangeRecord> findChanges(ServiceGood serviceGood) {

        EntityManager em = emf.createEntityManager();
        Query q = em.createNamedQuery("ChangeRecord.findForServiceGood");
        q.setParameter("serviceGood", serviceGood);
        q.setMaxResults(50);
        List<ChangeRecord> changeRecords = q.getResultList();
        em.close();
        return changeRecords;

    }


}
