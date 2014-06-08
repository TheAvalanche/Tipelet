package lv.telepit.model.utils;

import lv.telepit.model.dto.ReportData;

import java.util.Comparator;

/**
 * Created by Alex on 08/06/2014.
 */
public class ReportDataComparator implements Comparator<ReportData> {
    @Override
    public int compare(ReportData o1, ReportData o2) {
        if (o1 == null || o1.getDate() == null || o2 == null || o2.getDate() == null) {
            return 0;
        }
        return o1.getDate().compareTo(o2.getDate());
    }
}
