package lv.telepit.model;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Alex on 07/04/2014.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "StockGood.findAll", query = "select sg from StockGood sg"),
        @NamedQuery(name = "StockGood.findByLinkAndStore", query = "select sg from StockGood sg where sg.link = :link and sg.store = :store"),
        @NamedQuery(name = "StockGood.findByLink", query = "select sg from StockGood sg where sg.link = :link")
})
public class StockGood {

    private long id;
    private long version;
    private String customId = "";
    private boolean bestseller = false;
    private boolean ordered = false;
    private Store store;
    private User user;
    private Category category;
    private Double price;
    private Double advance;
    private Integer count;
    private String name;
    private String model;
    private String compatibleModels;
    private Date lastSoldDate;
    private boolean attention = false;
    private String link;
    private List<SoldItem> soldItemList = new ArrayList<>();

    private ChangeRecord change = new ChangeRecord("stock.good");

    @Id
    @GeneratedValue(generator = "stockgood_seq")
    @SequenceGenerator(name = "stockgood_seq", sequenceName = "stockgood_seq", initialValue = 100000, allocationSize = 1)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    @Version
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public boolean isBestseller() {
        return bestseller;
    }

    public void setBestseller(boolean bestseller) {
        change.addChange("bestseller",
                String.valueOf(this.bestseller),
                String.valueOf(bestseller));
        this.bestseller = bestseller;
    }

    public boolean isOrdered() {
        return ordered;
    }

    public void setOrdered(boolean ordered) {
        change.addChange("ordered",
                String.valueOf(this.ordered),
                String.valueOf(ordered));
        this.ordered = ordered;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        change.addChange("price",
                this.price != null ? this.price.toString() : "-",
                price != null ? price.toString() : "-");
        this.price = price;
    }

    public Double getAdvance() {
        return advance;
    }

    public void setAdvance(Double advance) {
        change.addChange("advance",
                this.advance != null ? this.advance.toString() : "-",
                advance != null ? advance.toString() : "-");
        this.advance = advance;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        change.addChange("count",
                this.count != null ? this.count.toString() : "-",
                count != null ? count.toString() : "-");
        this.count = count;
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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        change.addChange("model",
                this.model != null ? this.model : "-",
                model != null ? model : "-");
        this.model = model;
    }

    public String getCompatibleModels() {
        return compatibleModels;
    }

    public void setCompatibleModels(String compatibleModels) {
        change.addChange("compatibleModels",
                this.model != null ? this.compatibleModels : "-",
                compatibleModels != null ? compatibleModels : "-");
        this.compatibleModels = compatibleModels;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastSoldDate() {
        return lastSoldDate;
    }

    public void setLastSoldDate(Date lastSoldDate) {
        change.addChange("lastSoldDate",
                this.lastSoldDate != null ? new SimpleDateFormat("dd-MM-YYYY HH:mm").format(this.lastSoldDate) : "-",
                lastSoldDate != null ? new SimpleDateFormat("dd-MM-YYYY HH:mm").format(lastSoldDate) : "-");
        this.lastSoldDate = lastSoldDate;
    }

    public boolean isAttention() {
        return attention;
    }

    public void setAttention(boolean attention) {
        change.addChange("attention",
                String.valueOf(this.attention),
                String.valueOf(attention));
        this.attention = attention;
    }

    @OneToMany(mappedBy = "parent")
    public List<SoldItem> getSoldItemList() {
        return soldItemList;
    }

    public void setSoldItemList(List<SoldItem> soldItemList) {
        this.soldItemList = soldItemList;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Transient
    public Double getTotal() {
        return count * price;
    }

    @Transient
    public Integer getSoldCount() {
        return soldItemList.size();
    }

    @Transient
    public ChangeRecord getChange() {
        return change;
    }

    @PostLoad
    public void postLoad() {
        change = new ChangeRecord("stock.good");
    }
}
