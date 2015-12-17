package lv.telepit.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class BusinessReceipt {

    private long id;
    private User user;
    private Store store;
    private Date date;
    private String providerName;
    private String providerPvnNum;
    private String providerRegNum;
    private String providerBankName;
    private String providerBankNum;
    private String providerAddress;
    private String receiverName;
    private String receiverRegNum;
    private String receiverAddress;
    private String receiverBankName;
    private Date payTillDate;
    private List<ReceiptItem> receiptItems = new ArrayList<>();

    @Id
    @GeneratedValue(generator = "businessreceipt_seq")
    @SequenceGenerator(name = "businessreceipt_seq", sequenceName = "businessreceipt_seq", initialValue = 1, allocationSize = 1)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = User.class)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(targetEntity = Store.class)
    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderPvnNum() {
        return providerPvnNum;
    }

    public void setProviderPvnNum(String providerPvnNum) {
        this.providerPvnNum = providerPvnNum;
    }

    public String getProviderRegNum() {
        return providerRegNum;
    }

    public void setProviderRegNum(String providerRegNum) {
        this.providerRegNum = providerRegNum;
    }

    public String getProviderBankName() {
        return providerBankName;
    }

    public void setProviderBankName(String providerBankName) {
        this.providerBankName = providerBankName;
    }

    public String getProviderBankNum() {
        return providerBankNum;
    }

    public void setProviderBankNum(String providerBankNum) {
        this.providerBankNum = providerBankNum;
    }

    public String getProviderAddress() {
        return providerAddress;
    }

    public void setProviderAddress(String providerAddress) {
        this.providerAddress = providerAddress;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverRegNum() {
        return receiverRegNum;
    }

    public void setReceiverRegNum(String receiverRegNum) {
        this.receiverRegNum = receiverRegNum;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverBankName() {
        return receiverBankName;
    }

    public void setReceiverBankName(String receiverBankName) {
        this.receiverBankName = receiverBankName;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getPayTillDate() {
        return payTillDate;
    }

    public void setPayTillDate(Date payTillDate) {
        this.payTillDate = payTillDate;
    }

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    public List<ReceiptItem> getReceiptItems() {
        return receiptItems;
    }

    public void setReceiptItems(List<ReceiptItem> receiptItems) {
        this.receiptItems = receiptItems;
    }
}
