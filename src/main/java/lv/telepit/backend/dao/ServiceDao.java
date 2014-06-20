package lv.telepit.backend.dao;

import lv.telepit.backend.criteria.ServiceGoodCriteria;
import lv.telepit.model.ChangeRecord;
import lv.telepit.model.ServiceGood;

import java.util.List;
import java.util.Map;

/**
 * Created by Alex on 04/03/14.
 */
public interface ServiceDao {
    void createGood(ServiceGood good);

    void updateGood(ServiceGood good);

    void deleteGood(ServiceGood good);

    List<ServiceGood> findGoods(Map<ServiceGoodCriteria, Object> criteriaMap);

    List<ChangeRecord> findChanges(ServiceGood serviceGood);
}
