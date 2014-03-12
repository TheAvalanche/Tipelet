package lv.telepit.model;


import javax.persistence.*;
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

    private ChangeRecord change = new ChangeRecord("service.good");

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
                this.deliveredDate != null ? this.deliveredDate.toString() : "-",
                deliveredDate != null ? deliveredDate.toString() : "-");
        this.deliveredDate = deliveredDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        change.addChange("startDate",
                this.startDate != null ? this.startDate.toString() : "-",
                startDate != null ? startDate.toString() : "-");
        this.startDate = startDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        change.addChange("finishDate",
                this.finishDate != null ? this.finishDate.toString() : "-",
                finishDate != null ? finishDate.toString() : "-");
        this.finishDate = finishDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(Date returnedDate) {
        change.addChange("returnedDate",
                this.returnedDate != null ? this.returnedDate.toString() : "-",
                returnedDate != null ? returnedDate.toString() : "-");
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

    @Transient
    public ChangeRecord getChange() {
        return change;
    }

    @PostLoad
    public void postLoad() {
        change = new ChangeRecord("service.good");
    }
}
