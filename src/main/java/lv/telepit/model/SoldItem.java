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
    private String code;
    private User user;
    private Double price;
    private Date soldDate;
    private boolean withBill = true;
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

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isWithBill() {
        return withBill;
    }

    public void setWithBill(boolean withBill) {
        this.withBill = withBill;
    }
}