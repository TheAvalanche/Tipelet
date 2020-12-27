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
        @NamedQuery(name = "Store.getAll", query = "select s from Store s where s.deleted <> true"),
        @NamedQuery(name = "Store.getByName", query = "select s from Store s where s.name=:name")
})
public class Store {

    private long id;
    private long version;
    private String name;
    private String city;
    private String address;
    private boolean deleted;

    private String legalName;
    private String legalRegNum;
    private String legalBankName;
    private String legalBankNum;
    private String legalAddress;

    private String invoicePostfix;
    
    private List<User> userList;
    private List<ServiceGood> serviceGoodList;

    @Id
    @GeneratedValue(generator = "store_seq")
    @SequenceGenerator(name = "store_seq", sequenceName = "user_seq", initialValue = 20000, allocationSize = 1)
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

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getLegalRegNum() {
        return legalRegNum;
    }

    public void setLegalRegNum(String legalRegNum) {
        this.legalRegNum = legalRegNum;
    }

    public String getLegalBankName() {
        return legalBankName;
    }

    public void setLegalBankName(String legalBankName) {
        this.legalBankName = legalBankName;
    }

    public String getLegalBankNum() {
        return legalBankNum;
    }

    public void setLegalBankNum(String legalBankNum) {
        this.legalBankNum = legalBankNum;
    }

    public String getLegalAddress() {
        return legalAddress;
    }

    public void setLegalAddress(String legalAddress) {
        this.legalAddress = legalAddress;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getInvoicePostfix() {
        return invoicePostfix;
    }

    public void setInvoicePostfix(String invoicePostfix) {
        this.invoicePostfix = invoicePostfix;
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
