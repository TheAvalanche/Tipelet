package lv.telepit.model.utils;

import lv.telepit.model.dto.RecordData;

import java.util.Comparator;

/**
 * Created by Alex on 08/06/2014.
 */
public class RecordDataComparator implements Comparator<RecordData> {

    @Override
    public int compare(RecordData o1, RecordData o2) {
        if (o1 == null || o1.getDate() == null || o2 == null || o2.getDate() == null) {
            return 0;
        }
        return o1.getDate().compareTo(o2.getDate());
    }
}
