package lv.telepit.backend.dao;

import lv.telepit.model.Category;

import java.util.List;

/**
 * Created by Alex on 11/03/14.
 */
public interface CategoryDao {
    void addOrUpdateCategory(Category category);

    void removeCategory(Category category);

    List<Category> getAllCategories();
}
