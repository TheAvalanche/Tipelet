package lv.telepit.model;

import com.google.common.base.Objects;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 11/03/14.
 */
@Entity
@Table(name = "telepit_category")
@NamedQueries({
        @NamedQuery(name = "Category.getAll", query = "select c from Category c where c.parent is null"),
        @NamedQuery(name = "Category.getChildren", query = "select c from Category c where c.parent = :parent")
})
public class Category {
    private long id;
    private long version;
    private List<Category> children = new ArrayList<>();
    private Category parent;
    private String name;
    private String treeName;
    private List<Long> allIds = new ArrayList<>();

    public Category() {

    }

    // private constructor
    private Category(Category parent, String name) {
        this.parent = parent;
        this.name = name;
    }

    @Id
    @GeneratedValue(generator = "category_seq")
    @SequenceGenerator(name = "category_seq", sequenceName = "category_seq", initialValue = 30000, allocationSize = 1)
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

    // adds a category to this category
    public Category addCategory(String name) {
        Category child = new Category(this, name);
        children.add(child);
        return child;
    }

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    public List<Category> getChildren() {
        return children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }

    @ManyToOne
    @JoinColumn(name = "parent_id")
    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Transient
    public String getTreeName() {
        return treeName;
    }

    public void setTreeName(String treeName) {
        this.treeName = treeName;
    }

    @Transient
    public List<Long> getAllIds() {
        return allIds;
    }

    public void setAllIds(List<Long> allIds) {
        this.allIds = allIds;
    }

    // creates and returns a new categories tree
    public static Category createCategories() {
        return new Category(null, "root");
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Category other = (Category) obj;
        return Objects.equal(this.getId(), other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getId());
    }
}
