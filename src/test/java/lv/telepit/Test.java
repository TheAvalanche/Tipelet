package lv.telepit;

import lv.telepit.backend.PersistenceProvider;
import lv.telepit.model.Category;
import lv.telepit.model.StockGood;
import lv.telepit.model.Store;
import lv.telepit.model.User;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Random;

public class Test {

    static Random randomGenerator;
    static List<Category> categories;
    static List<Store> stores;
    static List<User> users;

    public static void main(String[] args) {
        EntityManagerFactory emf = PersistenceProvider.getInstance().getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        randomGenerator = new Random();
        categories = em.createNamedQuery("Category.getAll").getResultList();
        stores = em.createNamedQuery("Store.getAll").getResultList();
        users = em.createNamedQuery("User.getAll").getResultList();
        for (int i = 0; i <= 10000; i++) {
            System.out.println(i);
            StockGood sg = new StockGood();
            sg.setCount(10);
            sg.setPrice(10.0);
            sg.setCategory(getRandomCategory());
            sg.setCompatibleModels("Test data");
            sg.setLink(RandomStringUtils.randomAlphabetic(10));
            sg.setName("Test data");
            sg.setModel("Test data");
            User user = getRandomUser();
            sg.setStore(user.getStore());
            sg.setUser(user);
            em.persist(sg);
        }
        em.getTransaction().commit();
        em.close();
        emf.close();
    }

    public static Category getRandomCategory() {
        int index = randomGenerator.nextInt(categories.size());
        Category item = categories.get(index);
        return item;
    }

    public static Store getRandomStore() {
        int index = randomGenerator.nextInt(stores.size());
        Store item = stores.get(index);
        return item;
    }

    public static User getRandomUser() {
        int index = randomGenerator.nextInt(users.size());
        User item = users.get(index);
        return item;
    }
}
