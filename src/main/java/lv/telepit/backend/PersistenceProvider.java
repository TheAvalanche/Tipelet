package lv.telepit.backend;

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


        this.emf = Persistence.createEntityManagerFactory("telepit-persistence");

        logger.info("Persistence started at " + new java.util.Date());
    }
}
