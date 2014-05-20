package lv.telepit.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Alex on 14/05/2014.
 */
public class RecordData {
    private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");
    private String user;
    private String store;
    private String date;
    private String type;
    private String id;
    private String name;
    private String propertyName;
    private String oldValue;
    private String newValue;

    public static List<RecordData> construct(ChangeRecord record) {
        List<RecordData> list = new ArrayList<>(record.getChangeList().size());
        for (ChangeRecord.PropertyChange p : record.getChangeList()) {
            RecordData r = new RecordData();
            r.user = record.getUser().getName() + " " + record.getUser().getSurname();
            r.store = record.getUser().getStore().getName();
            r.date = new SimpleDateFormat("dd-MM-YYYY HH:mm").format(record.getDate());
            r.type = record.getServiceGood() != null ? "Servisa Prece" : "Noliktavas prece";
            r.id = String.valueOf(record.getServiceGood() != null ? record.getServiceGood().getId() : record.getStockGood().getId());
            r.name = String.valueOf(record.getServiceGood() != null ? record.getServiceGood().getName() : record.getStockGood().getName());
            r.propertyName = bundle.getString(p.getName());
            r.oldValue = p.getOldValue();
            r.newValue = p.getNewValue();
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

    public String getPropertyName() {
        return propertyName;
    }

    public String getOldValue() {
        return oldValue;
    }

    public String getNewValue() {
        return newValue;
    }
}
