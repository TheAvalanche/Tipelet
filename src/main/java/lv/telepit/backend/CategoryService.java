package lv.telepit.backend;

import lv.telepit.backend.dao.CategoryDao;
import lv.telepit.backend.dao.CategoryDaoImpl;
import lv.telepit.model.Category;

import java.util.List;

/**
 * Created by Alex on 21/02/14.
 */
public class CategoryService {

    private CategoryDao categoryDao;

    public CategoryService() {
        this.categoryDao = new CategoryDaoImpl();
    }

    public void addOrUpdateCategory(Category category) {
        categoryDao.addOrUpdateCategory(category);
    }

    public List<Category> getAllCategories() {
        return categoryDao.getAllCategories();
    }



}
