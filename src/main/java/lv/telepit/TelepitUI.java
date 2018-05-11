package lv.telepit;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import lv.telepit.backend.BusinessReceiptService;
import lv.telepit.backend.CommonService;
import lv.telepit.backend.ServiceGoodService;
import lv.telepit.backend.StockService;
import lv.telepit.model.User;
import lv.telepit.ui.view.*;

import javax.servlet.annotation.WebServlet;
import java.util.Locale;

@Theme("mytheme")
@SuppressWarnings("serial")
public class TelepitUI extends UI {


    private CommonService commonService;
    private ServiceGoodService serviceGoodService;
    private StockService stockService;
    private BusinessReceiptService businessReceiptService;

    private User currentUser;

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = TelepitUI.class, widgetset = "lv.telepit.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {


        setLocale(new Locale("lv"));
        Locale.setDefault(new Locale("lv"));

        commonService = new CommonService();
        serviceGoodService = new ServiceGoodService();
        stockService = new StockService();
        businessReceiptService = new BusinessReceiptService();

        Navigator navigator = new Navigator(this, this);

        StartView startView = new StartView(this);
        StockView stockView = new StockView(navigator, this, "stock");
        ServiceView serviceView = new ServiceView(navigator, this, "service");
        UserView userView = new UserView(navigator, this, "user");
        StoreView storeView = new StoreView(navigator, this, "store");
        CategoryView categoryView = new CategoryView(navigator, this, "category");
        ReportView reportView = new ReportView(navigator, this, "report");
        ChangesView changesView = new ChangesView(navigator, this, "changes");
        BusinessReceiptView businessReceiptView = new BusinessReceiptView(navigator, this, "businessReceipt");
        
        navigator.addView("", startView);
        navigator.addView("stock", stockView);
        navigator.addView("service", serviceView);
        navigator.addView("user", userView);
        navigator.addView("store", storeView);
        navigator.addView("category", categoryView);
        navigator.addView("report", reportView);
        navigator.addView("changes", changesView);
        navigator.addView("businessReceipt", businessReceiptView);
    }

    public CommonService getCommonService() {
        return commonService;
    }

    public ServiceGoodService getServiceGoodService() {
        return serviceGoodService;
    }

    public BusinessReceiptService getBusinessReceiptService() {
        return businessReceiptService;
    }
    
    public StockService getStockService() {
        return stockService;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
