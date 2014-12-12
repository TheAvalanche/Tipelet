package lv.telepit.model.dto;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import lv.telepit.model.ChangeRecord;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Alex on 14/05/2014.
 */
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
        this.id = String.valueOf(record.getServiceGood() != null ? record.getServiceGood().getCustomId() : record.getStockGood().getCustomId());
        this.name = String.valueOf(record.getServiceGood() != null ? record.getServiceGood().getName() : record.getStockGood().getName());
        this.propertyChanges.addAll(Collections2.transform(record.getChangeList(), new Function<ChangeRecord.PropertyChange, ChangeRecord.PropertyChange>() {
            @Override
            public ChangeRecord.PropertyChange apply(ChangeRecord.PropertyChange propertyChange) {
                propertyChange.setName(bundle.getString(propertyChange.getName()));
                return propertyChange;
            }
        }));
        this.propertyNames = Joiner.on(",").join(Collections2.transform(this.propertyChanges, new Function<ChangeRecord.PropertyChange, String>() {
            @Override
            public String apply(ChangeRecord.PropertyChange propertyChange) {
                return propertyChange.getName();
            }
        }));
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
