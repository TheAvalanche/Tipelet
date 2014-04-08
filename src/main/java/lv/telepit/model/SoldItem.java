package lv.telepit.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Alex on 07/04/2014.
 */
@Entity
public class SoldItem {

    private long id;
    private Store store;
    private User user;
    private Double price;
    private Date soldDate;
    private StockGood parent;

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getSoldDate() {
        return soldDate;
    }

    public void setSoldDate(Date soldDate) {
        this.soldDate = soldDate;
    }

    @ManyToOne(targetEntity = StockGood.class)
    public StockGood getParent() {
        return parent;
    }

    public void setParent(StockGood parent) {
        this.parent = parent;
    }
}
