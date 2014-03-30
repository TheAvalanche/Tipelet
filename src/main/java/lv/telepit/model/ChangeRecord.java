package lv.telepit.model;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.persistence.*;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by Alex on 12/03/14.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "ChangeRecord.findForServiceGood", query = "select cr from ChangeRecord cr where cr.serviceGood = :serviceGood order by cr.date DESC")
})
public class ChangeRecord {

    private long id;
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

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    @Column(length = 4096)
    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    @PrePersist
    public void preTransform() {
        Gson gson = new Gson();
        this.json = gson.toJson(changeList);
    }

    @PostLoad
    public void postTransform() {
        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<PropertyChange>>(){}.getType();
        this.changeList = gson.fromJson(json, collectionType);
    }

    @Transient
    public List<PropertyChange> getChangeList() {
        return changeList;
    }

    public void setChangeList(List<PropertyChange> changeList) {
        this.changeList = changeList;
    }

    public void addChange(String name, String oldValue, String newValue) {
        if (Objects.equals(oldValue, newValue)) {
            return;
        }
        if (!Strings.isNullOrEmpty(name)) {
            name = baseName + "." + name;
        }
        PropertyChange change = new PropertyChange();
        change.setName(name);
        change.setOldValue(oldValue);
        change.setNewValue(newValue);
        changeList.add(change);
    }

    public class PropertyChange {

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
