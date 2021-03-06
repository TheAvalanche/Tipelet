package lv.telepit.backend;

import com.vaadin.ui.Notification;
import lv.telepit.backend.criteria.ServiceGoodCriteria;
import lv.telepit.backend.dao.ServiceDao;
import lv.telepit.backend.dao.ServiceDaoImpl;
import lv.telepit.model.ChangeRecord;
import lv.telepit.model.ServiceGood;
import lv.telepit.model.ServiceStatus;
import lv.telepit.model.dto.ReportData;

import javax.persistence.OptimisticLockException;
import java.util.*;

/**
 * Created by Alex on 04/03/14.
 */
public class ServiceGoodService {

    private ServiceDao serviceDao;

    private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");

    public ServiceGoodService() {
        this.serviceDao = new ServiceDaoImpl();
    }

    public void saveGood(ServiceGood good) {
        try {
            serviceDao.createGood(good);
        } catch (OptimisticLockException e) {
            catchOptimisticLockException();
        }
    }

    public void updateGood(ServiceGood good) {
        try {
            serviceDao.updateGood(good);
        } catch (OptimisticLockException e) {
            catchOptimisticLockException();
        }
    }

    public void deleteGood(ServiceGood good) {
        good.setDeleted(true);
        try {
            serviceDao.updateGood(good);
        } catch (OptimisticLockException e) {
            catchOptimisticLockException();
        }
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
        try {
            serviceDao.updateGood(serviceGood);
        } catch (OptimisticLockException e) {
            catchOptimisticLockException();
        }
    }

    public List<ReportData> findReports(Map<ServiceGoodCriteria, Object> map) {
        if (!map.containsKey(ServiceGoodCriteria.RETURNED_DATE_FROM) && !map.containsKey(ServiceGoodCriteria.RETURNED_DATE_TO)) {
            map.put(ServiceGoodCriteria.RETURNED_DATE_TO, new Date()); //to return where returned date is not null only
        }
        map.put(ServiceGoodCriteria.STATUS, ServiceStatus.RETURNED);
        List<ServiceGood> serviceGoods = serviceDao.findGoods(map);
        List<ReportData> list = new ArrayList<>(serviceGoods.size());
        list.addAll(ReportData.constructFromServiceGoods(serviceGoods));
        return list;
    }

    private void catchOptimisticLockException() {
        Notification.show(bundle.getString("exception.lock.header"), bundle.getString("exception.lock.message"), Notification.Type.ERROR_MESSAGE);
    }
}
