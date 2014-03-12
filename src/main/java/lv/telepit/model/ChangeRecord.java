package lv.telepit.model;

import com.google.common.base.Strings;

import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Alex on 12/03/14.
 */
public class ChangeRecord {

    private User user;
    private Date date;
    private ServiceGood serviceGood;
    private String json;
    private String baseName;
    private List<PropertyChange> changeList = new ArrayList<>();

    public ChangeRecord() {
    }

    public ChangeRecord(String baseName) {
        this.baseName = baseName;
    }

    @Transient
    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    @ManyToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @ManyToOne
    public ServiceGood getServiceGood() {
        return serviceGood;
    }

    public void setServiceGood(ServiceGood serviceGood) {
        this.serviceGood = serviceGood;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    @Transient
    public List<PropertyChange> getChangeList() {
        return changeList;
    }

    public void setChangeList(List<PropertyChange> changeList) {
        this.changeList = changeList;
    }

    public void addChange(String name, String oldValue, String newValue) {
        if (!Strings.isNullOrEmpty(name)) {
            name = baseName + "." + name;
        }
        PropertyChange change = new PropertyChange();
        change.setName(name);
        change.setOldValue(oldValue);
        change.setNewValue(newValue);
    }

    private class PropertyChange {

        private String name;
        private String oldValue;
        private String newValue;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOldValue() {
            return oldValue;
        }

        public void setOldValue(String oldValue) {
            this.oldValue = oldValue;
        }

        public String getNewValue() {
            return newValue;
        }

        public void setNewValue(String newValue) {
            this.newValue = newValue;
        }
    }
}
