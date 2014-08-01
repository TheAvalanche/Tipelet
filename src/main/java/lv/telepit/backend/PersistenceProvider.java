package lv.telepit.backend;

import lv.telepit.utils.PropertyUtil;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * PersistenceProvider
 *
 * @author Alex Kartishev (alexkartishev@gmail.com))
 *         Date: 14.14.2.
 */
public class PersistenceProvider {
    private static Logger logger =
            Logger.getLogger(PersistenceProvider.class.getName());

    private static final PersistenceProvider singleton = new PersistenceProvider();

    protected EntityManagerFactory emf;

    public static synchronized PersistenceProvider getInstance() {

        return singleton;
    }

    private PersistenceProvider() {
    }

    public EntityManagerFactory getEntityManagerFactory() {

        if (emf == null)
            createEntityManagerFactory();
        return emf;
    }

    public void closeEntityManagerFactory() {

        if (emf != null) {
            emf.close();
            emf = null;
            logger.info("Persistence finished at " + new java.util.Date());
        }
    }

    protected void createEntityManagerFactory() {

        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.url", PropertyUtil.get("database.url"));
        properties.put("javax.persistence.jdbc.user", PropertyUtil.get("database.username"));
        properties.put("javax.persistence.jdbc.password", PropertyUtil.get("database.password"));
        this.emf = Persistence.createEntityManagerFactory("telepit-persistence", properties);

        logger.info("Persistence started at " + new java.util.Date());
    }
}
