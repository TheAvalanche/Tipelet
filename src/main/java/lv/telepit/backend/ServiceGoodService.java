package lv.telepit.backend;

import lv.telepit.backend.dao.ServiceDao;
import lv.telepit.backend.dao.ServiceDaoImpl;
import lv.telepit.backend.dao.StoreDaoImpl;
import lv.telepit.model.ServiceGood;
import lv.telepit.model.Store;

import java.util.List;

/**
 * Created by Alex on 04/03/14.
 */
public class ServiceGoodService {

    private ServiceDao serviceDao;

    public ServiceGoodService() {
        this.serviceDao = new ServiceDaoImpl();
    }

    public void saveGood(ServiceGood good) {
        serviceDao.createGood(good);
    }

    public void updateGood(ServiceGood good) {
        serviceDao.updateGood(good);
    }

    public void deleteGood(ServiceGood good) {
        serviceDao.deleteGood(good);
    }

    public List<ServiceGood> getAllGoods() {
        return serviceDao.getAllGoods();
    }
}
