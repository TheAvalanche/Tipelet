package lv.telepit.model;

import com.google.common.base.Objects;
import com.sun.corba.se.spi.ior.ObjectAdapterId;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "telepit_user")
@NamedQueries({
        @NamedQuery(name = "User.getAll", query = "select u from User u"),
        @NamedQuery(name = "User.getByLogin", query = "select u from User u where u.login = :login"),
        @NamedQuery(name = "User.getByLoginAndPass", query = "select u from User u where u.login = :login and u.password = :password")
})
public class User {
    private long id;
    private long version;
    private String name;
    private String surname;
    private String login;
    private String password;
    private String phone;
    private Store store;
    private boolean deleted = false;
    private boolean admin = false;
    private boolean accessToAddInStock = false;
    private boolean accessToBillOnly = false;
    private List<ServiceGood> serviceGoodList;

    @Id
    @GeneratedValue(generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", initialValue = 10000, allocationSize = 1)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Version
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @ManyToOne(targetEntity = Store.class)
    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isAccessToAddInStock() {
        return accessToAddInStock;
    }

    public void setAccessToAddInStock(boolean accessToAddInStock) {
        this.accessToAddInStock = accessToAddInStock;
    }

    public boolean isAccessToBillOnly() {
        return accessToBillOnly;
    }

    public void setAccessToBillOnly(boolean accessToBillOnly) {
        this.accessToBillOnly = accessToBillOnly;
    }

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    public List<ServiceGood> getServiceGoodList() {
        return serviceGoodList;
    }

    public void setServiceGoodList(List<ServiceGood> serviceGoodList) {
        this.serviceGoodList = serviceGoodList;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final User other = (User) obj;
        return Objects.equal(this.getId(), other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getId());
    }
}
