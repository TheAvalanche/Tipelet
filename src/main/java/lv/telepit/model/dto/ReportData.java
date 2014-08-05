package lv.telepit.model.dto;

import lv.telepit.model.ServiceGood;
import lv.telepit.model.SoldItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Alex on 21/05/2014.
 */
public class ReportData {

    private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");

    private String user;
    private String store;
    private Date date;
    private String type;
    private String id;
    private String name;
    private String code;
    private Double price;
    private String info;


    public static List<ReportData> constructFromSoldItems(List<SoldItem> soldItems) {
        List<ReportData> list = new ArrayList<>(soldItems.size());
        for (SoldItem si : soldItems) {
            ReportData r = new ReportData();
            r.user = si.getUser().getName() + " " + si.getUser().getSurname();
            r.store = si.getStore().getName();
            r.date = si.getSoldDate();
            r.type = bundle.getString("stock.type");
            r.id = String.valueOf(si.getParent().getId());
            r.name = si.getParent().getName();
            r.code = si.getCode();
            r.price = si.getPrice();
            r.info = si.getInfo();
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
            r.date = sg.getReturnedDate();
            r.type = bundle.getString("service.type");
            r.id = String.valueOf(sg.getId());
            r.name = sg.getName();
            r.code = sg.getImei();
            r.price = sg.getPrice();
            r.info = "";
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

    public Date getDate() {
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

    public Double getPrice() {
        return price;
    }

    public String getInfo() {
        return info;
    }
}
