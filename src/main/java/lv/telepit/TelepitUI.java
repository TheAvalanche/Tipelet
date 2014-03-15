package lv.telepit;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import lv.telepit.backend.CategoryService;
import lv.telepit.backend.ServiceGoodService;
import lv.telepit.backend.StoreService;
import lv.telepit.backend.UserService;
import lv.telepit.model.Category;
import lv.telepit.model.User;
import lv.telepit.ui.view.*;

import java.util.Locale;

@Theme("mytheme")
@SuppressWarnings("serial")
public class TelepitUI extends UI {


    private UserService userService;
    private StoreService storeService;
    private ServiceGoodService serviceGoodService;
    private CategoryService categoryService;

    private User currentUser;

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = TelepitUI.class, widgetset = "lv.telepit.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {

        setLocale(new Locale("lv"));
        Locale.setDefault(new Locale("lv"));

        userService = new UserService();
        storeService = new StoreService();
        serviceGoodService = new ServiceGoodService();
        categoryService = new CategoryService();

        Navigator navigator = new Navigator(this, this);

        StartView startView = new StartView(this);
        StockView stockView = new StockView(navigator, this, "stock");
        ServiceView serviceView = new ServiceView(navigator, this, "service");
        UserView userView = new UserView(navigator, this, "user");
        StoreView storeView = new StoreView(navigator, this, "store");
        CategoryView categoryView = new CategoryView(navigator, this, "category");

        navigator.addView("", startView);
        navigator.addView("stock", stockView);
        navigator.addView("service", serviceView);
        navigator.addView("user", userView);
        navigator.addView("store", storeView);
        navigator.addView("category", categoryView);


    }

    public UserService getUserService() {
        return userService;
    }
    public StoreService getStoreService() {
        return storeService;
    }

    public ServiceGoodService getServiceGoodService() {
        return serviceGoodService;
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
