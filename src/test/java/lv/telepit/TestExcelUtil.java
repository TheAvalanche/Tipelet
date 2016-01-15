package lv.telepit;

import lv.telepit.model.*;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TestExcelUtil {

    public static void main(String[] args) throws IOException {
        Store store = new Store();
        store.setAddress("Test Address");

        User user = new User();
        user.setName("Name");
        user.setSurname("Surname");

        BusinessReceipt businessReceipt = new BusinessReceipt();
        businessReceipt.setDate(new Date());
        businessReceipt.setId(1L);
        businessReceipt.setUser(user);
        businessReceipt.setStore(store);
        businessReceipt.setPayTillDate(new Date());
        businessReceipt.setProviderAddress("P. Address");
        businessReceipt.setProviderBankName("P. Bank Name");
        businessReceipt.setProviderBankNum("P. Bank Num");
        businessReceipt.setProviderName("P. Name");
        businessReceipt.setProviderPvnNum("P. Pvn Num");
        businessReceipt.setProviderRegNum("P. Reg Num");
        businessReceipt.setReceiverAddress("R. Address");
        businessReceipt.setReceiverBankName("R. Bank Name");
        businessReceipt.setReceiverName("R. Name");
        businessReceipt.setReceiverRegNum("R. Reg Num");

        ReceiptItem item1 = new ReceiptItem();
        item1.setPrice(20.00);
        item1.setCount(2);
        item1.setName("Test1");

        ReceiptItem item2 = new ReceiptItem();
        item2.setPrice(20.00);
        item2.setCount(1);
        item2.setName("Test2");

        ReceiptItem item3 = new ReceiptItem();
        item3.setPrice(20.00);
        item3.setCount(3);
        item3.setName("Test3");

        ReceiptItem item4 = new ReceiptItem();
        item4.setPrice(15.69);
        item4.setCount(3);
        item4.setName("Test4");

        businessReceipt.getReceiptItems().addAll(Arrays.asList(item1, item2, item3, item4));

        try(InputStream is = TestExcelUtil.class.getResourceAsStream("/test.xls")) {
            try (OutputStream os = new FileOutputStream("target/object_collection_output.xls")) {
                Context context = new Context();
                context.putVar("br", businessReceipt);
                JxlsHelper.getInstance().processTemplate(is, os, context);
            }
        }

        ServiceGood serviceGood1 = new ServiceGood();
        serviceGood1.setId(1L);
        serviceGood1.setName("Test");
        serviceGood1.setStatus(ServiceStatus.IN_REPAIR);

        ServiceGood serviceGood2 = new ServiceGood();
        serviceGood2.setId(2L);
        serviceGood2.setName("Test2");
        serviceGood2.setStatus(ServiceStatus.ON_DETAILS);

        List<ServiceGood> serviceGoods = new ArrayList<>(Arrays.asList(serviceGood1, serviceGood2));

        try(InputStream is = TestExcelUtil.class.getResourceAsStream("/service_report_temp.xls")) {
            try (OutputStream os = new FileOutputStream("target/object_collection_output.xls")) {
                Context context = new Context();
                context.putVar("serviceGoods", serviceGoods);
                JxlsHelper.getInstance().processTemplate(is, os, context);
            }
        }

    }
}
