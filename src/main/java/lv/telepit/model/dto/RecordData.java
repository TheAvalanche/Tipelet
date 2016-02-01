package lv.telepit.model.dto;

import lv.telepit.model.ChangeRecord;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class RecordData {
    private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");
    private String user;
    private String store;
    private Date date;
    private String type;
    private String id;
    private String name;
    private String propertyNames;
    private List<ChangeRecord.PropertyChange> propertyChanges = new ArrayList<>();

    public RecordData(ChangeRecord record) {
        this.user = record.getUser().getName() + " " + record.getUser().getSurname();
        this.store = record.getUser().getStore().getName();
        this.date = record.getDate();
        this.type = record.getServiceGood() != null ? bundle.getString("service.type") : bundle.getString("stock.type");
        this.id = String.valueOf(record.getServiceGood() != null ? record.getServiceGood().getCustomId() : record.getStockGood().getIncrementId());
        this.name = String.valueOf(record.getServiceGood() != null ? record.getServiceGood().getName() : record.getStockGood().getName());
        this.propertyChanges.addAll(record.getChangeList().stream().map(this::transformName).collect(Collectors.toList()));
        this.propertyNames = propertyChanges.stream().map(ChangeRecord.PropertyChange::getName).collect(Collectors.joining(","));
    }

    private ChangeRecord.PropertyChange transformName(ChangeRecord.PropertyChange pc) {
        pc.setName(bundle.getString(pc.getName()));
        return pc;
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

    public String getPropertyNames() {
        return propertyNames;
    }

    public List<ChangeRecord.PropertyChange> getPropertyChanges() {
        return propertyChanges;
    }
}
