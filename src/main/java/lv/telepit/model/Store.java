package lv.telepit.model;

import com.google.common.base.Objects;

import javax.persistence.*;
import java.util.List;

/**
 * Store
 *
 * @author Alex Kartishev (alexkartishev@gmail.com))
 *         Date: 14.14.2.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "Store.getAll", query = "select s from Store s"),
        @NamedQuery(name = "Store.getByName", query = "select s from Store s where s.name=:name")
})
public class Store {

    private long id;
    private String name;
    private String city;
    private String address;
    private boolean deleted;
    private List<User> userList;
    private List<ServiceGood> serviceGoodList;

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    public List<ServiceGood> getServiceGoodList() {
        return serviceGoodList;
    }

    public void setServiceGoodList(List<ServiceGood> serviceGoodList) {
        this.serviceGoodList = serviceGoodList;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Store other = (Store) obj;
        return Objects.equal(this.getId(), other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getId());
    }
}
