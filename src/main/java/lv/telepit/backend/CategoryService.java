package lv.telepit.backend;

import lv.telepit.backend.dao.CategoryDao;
import lv.telepit.backend.dao.CategoryDaoImpl;

/**
 * Created by Alex on 21/02/14.
 */
public class CategoryService {

    private CategoryDao categoryDao;

    public CategoryService() {
        this.categoryDao = new CategoryDaoImpl();
    }



}
