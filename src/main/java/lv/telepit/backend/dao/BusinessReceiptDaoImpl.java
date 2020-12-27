package lv.telepit.backend.dao;

import lv.telepit.backend.PersistenceProvider;
import lv.telepit.backend.criteria.BusinessReceiptCriteria;
import lv.telepit.model.BusinessReceipt;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class BusinessReceiptDaoImpl implements BusinessReceiptDao {

	private EntityManagerFactory emf;

	private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");

	public BusinessReceiptDaoImpl() {

		emf = PersistenceProvider.getInstance().getEntityManagerFactory();

	}

	@Override
	public void createBusinessReceipt(BusinessReceipt good) {
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();
		em.persist(good);
		em.getTransaction().commit();
		em.close();
	}

	@Override
	public void updateBusinessReceipt(BusinessReceipt good) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.merge(good);
		em.getTransaction().commit();
		em.close();
	}

	@Override
	public List<BusinessReceipt> findBusinessReceipts(Map<BusinessReceiptCriteria, Object> criteriaMap) {
		EntityManager em = emf.createEntityManager();
		StringBuilder queryBuilder = new StringBuilder("select br from BusinessReceipt br where 1=1");
		if (criteriaMap != null) {
			for (Map.Entry<BusinessReceiptCriteria, Object> entry : criteriaMap.entrySet()) {
				queryBuilder.append(" and ");
				entry.getKey().setQuery(queryBuilder);
			}
		}
		queryBuilder.append(" and br.deleted <> true order by br.id desc");
		final Query q = em.createQuery(queryBuilder.toString());
		if (criteriaMap != null) {
			for (Map.Entry<BusinessReceiptCriteria, Object> entry : criteriaMap.entrySet()) {
				entry.getKey().setValue(q, entry.getValue());
			}
		}
		q.setMaxResults(1000);
		List<BusinessReceipt> list = q.getResultList();
		em.close();
		return list;
	}

	@Override
	public Long lastReceiptNumber(String providerRegNum, Date dateFrom) {
		EntityManager em = emf.createEntityManager();

		final Query q = em.createQuery("select br from BusinessReceipt br where br.providerRegNum = :providerRegNum and br.date >= :dateFrom and br.deleted <> true order by br.id desc");
		q.setParameter("providerRegNum", providerRegNum);
		q.setParameter("dateFrom", dateFrom);
		q.setMaxResults(1);
		List<BusinessReceipt> list = q.getResultList();
		em.close();
		if (list.size() > 0 && list.get(0).getNumberIdx() != null) {
			return list.get(0).getNumberIdx();
		}
		return 0L;
	}
}
