package lv.telepit.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 21/05/2014.
 */
public class ReportData {

    private String user;
    private String store;
    private String date;
    private String type;
    private String id;
    private String name;
    private String code;
    private String price;


    public static List<ReportData> constructFromSoldItems(List<SoldItem> soldItems) {
        List<ReportData> list = new ArrayList<>(soldItems.size());
        for (SoldItem si : soldItems) {
            ReportData r = new ReportData();
            r.user = si.getUser().getName() + " " + si.getUser().getSurname();
            r.store = si.getStore().getName();
            r.date = new SimpleDateFormat("dd-MM-YYYY HH:mm").format(si.getSoldDate());
            r.type = "Noliktavas prece";
            r.id = String.valueOf(si.getParent().getId());
            r.name = si.getParent().getName();
            r.code = si.getCode();
            r.price = String.valueOf(si.getPrice());
            list.add(r);
        }
        return list;
    }

    public static List<ReportData> constructFromServiceGoods(List<ServiceGood> serviceGoods) {
        List<ReportData> list = new ArrayList<>(serviceGoods.size());
        for (ServiceGood sg : serviceGoods) {
            ReportData r = new ReportData();
            r.user = sg.getUser().getName() + " " + sg.getUser().getSurname();
            r.store = sg.getStore().getName();
            r.date = new SimpleDateFormat("dd-MM-YYYY HH:mm").format(sg.getReturnedDate());
            r.type = "Servisa prece";
            r.id = String.valueOf(sg.getId());
            r.name = sg.getName();
            r.code = sg.getImei();
            r.price = String.valueOf(sg.getPrice());
            list.add(r);
        }
        return list;
    }

    public String getUser() {
        return user;
    }

    public String getStore() {
        return store;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getPrice() {
        return price;
    }
}
