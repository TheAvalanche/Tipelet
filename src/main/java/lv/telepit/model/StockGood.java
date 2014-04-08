package lv.telepit.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Alex on 07/04/2014.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "StockGood.findAll", query = "select sg from StockGood sg")
})
public class StockGood {

    private long id;
    private Store store;
    private User user;
    private Category category;
    private Double price;
    private Integer count;
    private String name;
    private Date lastDeliveredDate;
    private Date lastSoldDate;
    private List<SoldItem> soldItemList;

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = Store.class)
    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    @ManyToOne(targetEntity = User.class)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(targetEntity = Category.class)
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastDeliveredDate() {
        return lastDeliveredDate;
    }

    public void setLastDeliveredDate(Date lastDeliveredDate) {
        this.lastDeliveredDate = lastDeliveredDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastSoldDate() {
        return lastSoldDate;
    }

    public void setLastSoldDate(Date lastSoldDate) {
        this.lastSoldDate = lastSoldDate;
    }

    @OneToMany(mappedBy = "parent")
    public List<SoldItem> getSoldItemList() {
        return soldItemList;
    }

    public void setSoldItemList(List<SoldItem> soldItemList) {
        this.soldItemList = soldItemList;
    }
}
