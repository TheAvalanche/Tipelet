package lv.telepit.backend.dao;

import lv.telepit.backend.criteria.ServiceGoodCriteria;
import lv.telepit.model.ChangeRecord;
import lv.telepit.model.ServiceGood;
import lv.telepit.model.Store;

import java.util.List;
import java.util.Map;

/**
 * Created by Alex on 04/03/14.
 */
public interface ServiceDao {
    void createGood(ServiceGood good);

    Long lastCustomId(Store providerRegNum);

    void updateGood(ServiceGood good);

    List<ServiceGood> findGoods(Map<ServiceGoodCriteria, Object> criteriaMap);

    List<ChangeRecord> findChanges(ServiceGood serviceGood);
}
