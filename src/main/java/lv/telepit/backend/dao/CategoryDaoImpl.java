package lv.telepit.backend.dao;

import lv.telepit.backend.PersistenceProvider;
import lv.telepit.model.Category;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by Alex on 11/03/14.
 */
public class CategoryDaoImpl implements CategoryDao {

    private EntityManagerFactory emf;

    public CategoryDaoImpl() {

        emf = PersistenceProvider.getInstance().getEntityManagerFactory();

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
                category.setTreeName(parent.getTreeName() + "/" + category.getName());
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
}
