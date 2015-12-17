package lv.telepit.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class ReceiptItem {

    private long id;
    private String name;
    private int count;
    private Double price;
    private BusinessReceipt parent;

    @Id
    @GeneratedValue(generator = "receiptitem_seq")
    @SequenceGenerator(name = "receiptitem_seq", sequenceName = "receiptitem_seq", initialValue = 1, allocationSize = 1)
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @ManyToOne(targetEntity = BusinessReceipt.class)
    public BusinessReceipt getParent() {
        return parent;
    }

    public void setParent(BusinessReceipt parent) {
        this.parent = parent;
    }

    @Transient
    public BigDecimal getTotalPrice() {
        return new BigDecimal(price).multiply(new BigDecimal(count));
    }
}
