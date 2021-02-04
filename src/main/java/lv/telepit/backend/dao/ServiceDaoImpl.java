package lv.telepit.backend.dao;

import com.vaadin.ui.UI;
import lv.telepit.TelepitUI;
import lv.telepit.backend.PersistenceProvider;
import lv.telepit.backend.criteria.ServiceGoodCriteria;
import lv.telepit.model.BusinessReceipt;
import lv.telepit.model.ChangeRecord;
import lv.telepit.model.ServiceGood;
import lv.telepit.model.Store;
import org.apache.commons.lang3.math.NumberUtils;

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
    public Long lastCustomId(Store providerRegNum) {
        EntityManager em = emf.createEntityManager();

        final Query q = em.createQuery("select br from ServiceGood br where br.store = :store and br.deleted <> true order by br.id desc");
        q.setParameter("store", providerRegNum);
        q.setMaxResults(1);
        List<ServiceGood> list = q.getResultList();
        em.close();
        if (list.size() > 0 && list.get(0).getCustomId() != null) {
            return NumberUtils.toLong(list.get(0).getCustomId(), 0L);
        }
        return 0L;
    }

    public boolean isBlackListed(String phoneNumber) {
        EntityManager em = emf.createEntityManager();

        final Query q = em.createQuery("select br.id from ServiceGood br where br.contactPhone = :phoneNumber and br.deleted <> true and br.blacklisted = true order by br.id desc");
        q.setParameter("phoneNumber", phoneNumber);
        q.setMaxResults(1);
        List<Long> list = q.getResultList();
        em.close();
        return list.size() > 0;

    }

    public boolean isFrequentUser(String phoneNumber) {
        EntityManager em = emf.createEntityManager();

        final Query q = em.createQuery("select br.id from ServiceGood br where br.contactPhone = :phoneNumber and br.deleted <> true order by br.id desc");
        q.setParameter("phoneNumber", phoneNumber);
        List<Long> list = q.getResultList();
        em.close();
        return list.size() >= 2;

    }

    public void blacklist(String phoneNumber) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        final Query q = em.createQuery("update ServiceGood br set br.blacklisted = true where br.contactPhone = :phoneNumber and br.deleted <> true");
        q.setParameter("phoneNumber", phoneNumber);
        q.executeUpdate();

        em.getTransaction().commit();
        em.close();
    }

    public void unblacklist(String phoneNumber) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        final Query q = em.createQuery("update ServiceGood br set br.blacklisted = false where br.contactPhone = :phoneNumber and br.deleted <> true");
        q.setParameter("phoneNumber", phoneNumber);
        q.executeUpdate();

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
    public List<ServiceGood> findGoods(Map<ServiceGoodCriteria, Object> criteriaMap) {
        EntityManager em = emf.createEntityManager();
        StringBuilder queryBuilder = new StringBuilder("select sg from ServiceGood sg where 1=1");
        if (criteriaMap != null) {
            for (Map.Entry<ServiceGoodCriteria, Object> entry : criteriaMap.entrySet()) {
                queryBuilder.append(" and ");
                entry.getKey().setQuery(queryBuilder);
            }
        }
        queryBuilder.append(" and sg.deleted <> true order by sg.id desc");
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
