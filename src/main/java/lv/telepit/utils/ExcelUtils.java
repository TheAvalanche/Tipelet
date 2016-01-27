package lv.telepit.utils;

import lv.telepit.model.ServiceGood;
import lv.telepit.model.StockGood;
import lv.telepit.model.dto.RecordData;
import lv.telepit.model.dto.ReportData;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;

import java.io.*;
import java.util.List;

public class ExcelUtils {

    public static ByteArrayOutputStream exportServiceGoods(List<ServiceGood> serviceGoods) throws IOException {
        try(InputStream is = ExcelUtils.class.getResourceAsStream("/service_report_temp.xls")) {
            try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                Context context = new Context();
                context.putVar("serviceGoods", serviceGoods);
                JxlsHelper.getInstance().processTemplate(is, os, context);
                return os;
            }
        }
    }

    public static ByteArrayOutputStream exportStockGoods(List<StockGood> stockGoods) throws IOException {
        try(InputStream is = ExcelUtils.class.getResourceAsStream("/stock_report_temp.xls")) {
            try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                Context context = new Context();
                context.putVar("stockGoods", stockGoods);
                JxlsHelper.getInstance().processTemplate(is, os, context);
                return os;
            }
        }
    }

    public static ByteArrayOutputStream exportFinancialReport(List<ReportData> reportItems) throws IOException {
        try(InputStream is = ExcelUtils.class.getResourceAsStream("/financial_report_temp.xls")) {
            try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                Context context = new Context();
                context.putVar("reportItems", reportItems);
                JxlsHelper.getInstance().processTemplate(is, os, context);
                return os;
            }
        }
    }

    public static ByteArrayOutputStream exportChangesReport(List<RecordData> recordItems) throws IOException {
        try(InputStream is = ExcelUtils.class.getResourceAsStream("/change_report_temp.xls")) {
            try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                Context context = new Context();
                context.putVar("recordItems", recordItems);
                JxlsHelper.getInstance().processTemplate(is, os, context);
                return os;
            }
        }
    }
}
