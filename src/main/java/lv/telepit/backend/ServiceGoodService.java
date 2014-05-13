package lv.telepit.backend;

import lv.telepit.backend.criteria.ServiceGoodCriteria;
import lv.telepit.backend.dao.ServiceDao;
import lv.telepit.backend.dao.ServiceDaoImpl;
import lv.telepit.model.ChangeRecord;
import lv.telepit.model.ServiceGood;
import lv.telepit.model.ServiceStatus;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

    public List<ServiceGood> findGoods(Map<ServiceGoodCriteria, Object> query) {
        return serviceDao.findGoods(query);
    }
    
    public List<ChangeRecord> findChanges(ServiceGood serviceGood) {
        return serviceDao.findChanges(serviceGood);
    }

    public void changeStatus(ServiceGood serviceGood, ServiceStatus status) {
        serviceGood.setStatus(status);
        if (status == ServiceStatus.WAITING) {
            serviceGood.setFinishDate(null);
            serviceGood.setStartDate(null);
            serviceGood.setReturnedDate(null);
        }
        if (status == ServiceStatus.IN_REPAIR) {
            serviceGood.setStartDate(new Date());
        }
        if (status == ServiceStatus.REPAIRED || status == ServiceStatus.BROKEN) {
            serviceGood.setFinishDate(new Date());
        }
        if (status == ServiceStatus.RETURNED || status == ServiceStatus.ON_DETAILS) {
            serviceGood.setReturnedDate(new Date());
            if (serviceGood.getFinishDate() == null) {
                serviceGood.setFinishDate(new Date());
            }
        }
        serviceDao.updateGood(serviceGood);
    }
}
