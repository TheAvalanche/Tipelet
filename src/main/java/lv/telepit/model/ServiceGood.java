package lv.telepit.model;


import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ServiceGood
 *
 * @author Alex Kartishev (alexkartishev@gmail.com))
 *         Date: 14.14.2.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "ServiceGood.getAll", query = "select sg from ServiceGood sg")
})
public class ServiceGood {
    private long id;
    private long version;
    private String customId = "";
    private Store store;
    private User user;
    private ServiceStatus status;
    private Category category;
    private String name;
    private String imei;
    private String accumNum;
    private String problem;
    private Double price;
    private Date deliveredDate;
    private Date startDate;
    private Date finishDate;
    private Date returnedDate;
    private String contactName;
    private String contactPhone;
    private String contactMail;
    private String additionalDescription;
    private boolean warranty = false;
    private boolean deleted = false;
    private boolean withBill = true;
    private boolean called = false;
    private Date calledDate;

    private ChangeRecord change = new ChangeRecord("service.good");

    @Id
    @GeneratedValue(generator = "servicegood_seq")
    @SequenceGenerator(name = "servicegood_seq", sequenceName = "servicegood_seq", initialValue = 200000, allocationSize = 1)
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

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        change.addChange("customId",
                this.customId != null ? this.customId : "-",
                customId != null ? customId : "-");
        this.customId = customId;
    }

    @ManyToOne(targetEntity = Store.class)
    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        change.addChange("store",
                this.store != null ? this.store.getName() : "-" ,
                store != null ? store.getName() : "-");
        this.store = store;
    }

    @ManyToOne(targetEntity = User.class)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        change.addChange("user",
                this.user != null ? this.user.getName() + " " + this.user.getSurname() : "-",
                user != null ? user.getName() + " " + user.getSurname() : "-");
        this.user = user;
    }

    @Enumerated(EnumType.STRING)
    public ServiceStatus getStatus() {
        return status;
    }

    public void setStatus(ServiceStatus status) {
        change.addChange("status",
                this.status != null ? this.status.toString() : "-",
                status != null ? status.toString() : "-");
        this.status = status;
    }

    @ManyToOne(targetEntity = Category.class)
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        change.addChange("category",
                this.category != null ? this.category.getName() : "-",
                category != null ? category.getName() : "-");
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        change.addChange("name",
                this.name != null ? this.name : "-",
                name != null ? name : "-");
        this.name = name;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        change.addChange("imei",
                this.imei != null ? this.imei : "-",
                imei != null ? imei : "-");
        this.imei = imei;
    }

    public String getAccumNum() {
        return accumNum;
    }

    public void setAccumNum(String accumNum) {
        change.addChange("accumNum",
                this.accumNum != null ? this.accumNum : "-",
                accumNum != null ? accumNum : "-");
        this.accumNum = accumNum;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        change.addChange("problem",
                this.problem != null ? this.problem : "-",
                problem != null ? problem : "-");
        this.problem = problem;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        change.addChange("price",
                this.price != null ? this.price.toString() : "-",
                price != null ? price.toString() : "-");
        this.price = price;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(Date deliveredDate) {
        change.addChange("deliveredDate",
                this.deliveredDate != null ? new SimpleDateFormat("dd-MM-YYYY HH:mm").format(this.deliveredDate) : "-",
                deliveredDate != null ? new SimpleDateFormat("dd-MM-YYYY HH:mm").format(deliveredDate) : "-");
        this.deliveredDate = deliveredDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        change.addChange("startDate",
                this.startDate != null ? new SimpleDateFormat("dd-MM-YYYY HH:mm").format(this.startDate) : "-",
                startDate != null ? new SimpleDateFormat("dd-MM-YYYY HH:mm").format(startDate) : "-");
        this.startDate = startDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        change.addChange("finishDate",
                this.finishDate != null ? new SimpleDateFormat("dd-MM-YYYY HH:mm").format(this.finishDate) : "-",
                finishDate != null ? new SimpleDateFormat("dd-MM-YYYY HH:mm").format(finishDate) : "-");
        this.finishDate = finishDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(Date returnedDate) {
        change.addChange("returnedDate",
                this.returnedDate != null ? new SimpleDateFormat("dd-MM-YYYY HH:mm").format(this.returnedDate) : "-",
                returnedDate != null ? new SimpleDateFormat("dd-MM-YYYY HH:mm").format(returnedDate) : "-");
        this.returnedDate = returnedDate;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        change.addChange("contactName",
                this.contactName != null ? this.contactName : "-",
                contactName != null ? contactName : "-");
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        change.addChange("contactPhone",
                this.contactPhone != null ? this.contactPhone : "-",
                contactPhone != null ? contactPhone : "-");
        this.contactPhone = contactPhone;
    }

    public String getContactMail() {
        return contactMail;
    }

    public void setContactMail(String contactMail) {
        change.addChange("contactMail",
                this.contactMail != null ? this.contactMail : "-",
                contactMail != null ? contactMail : "-");
        this.contactMail = contactMail;
    }

    public String getAdditionalDescription() {
        return additionalDescription;
    }

    public void setAdditionalDescription(String additionalDescription) {
        change.addChange("additionalDescription",
                this.additionalDescription != null ? this.additionalDescription : "-",
                additionalDescription != null ? additionalDescription : "-");
        this.additionalDescription = additionalDescription;
    }

    public boolean isWarranty() {
        return warranty;
    }

    public void setWarranty(boolean warranty) {
        change.addChange("warranty",
                String.valueOf(this.warranty),
                String.valueOf(warranty));
        this.warranty = warranty;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        change.addChange("deleted",
                String.valueOf(this.deleted),
                String.valueOf(deleted));
        this.deleted = deleted;
    }

    public boolean isWithBill() {
        return withBill;
    }

    public void setWithBill(boolean withBill) {
        change.addChange("withBill",
                String.valueOf(this.withBill),
                String.valueOf(withBill));
        this.withBill = withBill;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getCalledDate() {
        return calledDate;
    }

    public void setCalledDate(Date calledDate) {
        change.addChange("calledDate",
                this.calledDate != null ? new SimpleDateFormat("dd-MM-YYYY HH:mm").format(this.calledDate) : "-",
                calledDate != null ? new SimpleDateFormat("dd-MM-YYYY HH:mm").format(calledDate) : "-");
        this.calledDate = calledDate;
    }

    public boolean isCalled() {
        return called;
    }

    public void setCalled(boolean called) {
        change.addChange("called",
                String.valueOf(this.called),
                String.valueOf(called));
        this.called = called;
    }

    @Transient
    public ChangeRecord getChange() {
        return change;
    }

    @PostLoad
    public void postLoad() {
        change = new ChangeRecord("service.good");
    }
}
