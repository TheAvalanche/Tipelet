package lv.telepit.utils;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.*;
import lv.telepit.model.ChangeRecord;
import lv.telepit.model.ServiceGood;
import lv.telepit.model.StockGood;
import lv.telepit.model.dto.ReportData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ExcelUtils {

    private final ByteArrayOutputStream outputStream;
    private final WritableSheet sheet;
    private final WritableWorkbook workbook;
    private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");
    private final WritableCellFormat boldCellWithBorder;
    private final WritableCellFormat normalCellWithBorder;
    private WritableCellFormat headerCell;
    private final WritableCellFormat headerCell2;

    public ExcelUtils() throws IOException, WriteException {

        outputStream = new ByteArrayOutputStream();
        workbook = Workbook.createWorkbook(outputStream);
        sheet = workbook.createSheet("Report", 0);

        WritableFont boldFont = new WritableFont(WritableFont.ARIAL, 10);
        boldFont.setBoldStyle(WritableFont.BOLD);

        boldCellWithBorder = new WritableCellFormat(boldFont);
        boldCellWithBorder.setBorder(Border.ALL, BorderLineStyle.MEDIUM);

        normalCellWithBorder = new WritableCellFormat();
        normalCellWithBorder.setBorder(Border.ALL, BorderLineStyle.THIN);

        WritableFont headerFont = new WritableFont(WritableFont.ARIAL, 10);
        headerFont.setBoldStyle(WritableFont.BOLD);
        headerFont.setColour(Colour.DARK_BLUE);

        headerCell = new WritableCellFormat(headerFont);

        WritableFont headerFont2 = new WritableFont(WritableFont.ARIAL, 10);
        headerFont2.setColour(Colour.DARK_BLUE2);

        headerCell2 = new WritableCellFormat(headerFont2);

    }

    public void exportChanges(java.util.List<ChangeRecord> changes) throws WriteException {
        if (changes == null || changes.isEmpty()) return;

        int y = 0;
        for (ChangeRecord change : changes) {
            Label headerOne = new Label(0, ++y, composeHeaderOne(change), headerCell);
            sheet.addCell(headerOne);
            sheet.mergeCells(0, y, 2, y);
            Label headerTwo = new Label(0, ++y, composeHeaderTwo(change), headerCell2);
            sheet.addCell(headerTwo);
            sheet.mergeCells(0, y, 2, y);

            ++y;

            Label h1 = new Label(0, y, bundle.getString("history.property"), boldCellWithBorder);
            sheet.addCell(h1);

            Label h2 = new Label(1, y, bundle.getString("history.old"), boldCellWithBorder);
            sheet.addCell(h2);

            Label h3 = new Label(2, y, bundle.getString("history.new"), boldCellWithBorder);
            sheet.addCell(h3);

            for (ChangeRecord.PropertyChange p : change.getChangeList()) {
                ++y;

                sheet.addCell(new Label(0, y, bundle.getString(p.getName()), normalCellWithBorder));
                sheet.addCell(new Label(1, y, p.getOldValue(), normalCellWithBorder));
                sheet.addCell(new Label(2, y, p.getNewValue(), normalCellWithBorder));
            }
            ++y;
        }
    }

    private String composeHeaderOne(ChangeRecord change) {
        StringBuilder builder = new StringBuilder();
        builder.append(change.getStockGood() != null ? bundle.getString("stock.type") : bundle.getString("service.type"));
        builder.append(" ");
        builder.append(change.getStockGood() != null ? change.getStockGood().getName() : change.getServiceGood().getName());
        builder.append(" (ID=");
        builder.append(change.getStockGood() != null ? change.getStockGood().getId() : change.getServiceGood().getId());
        builder.append(")");
        return builder.toString();
    }

    private String composeHeaderTwo(ChangeRecord change) {
        StringBuilder builder = new StringBuilder();
        builder.append(new SimpleDateFormat("dd-MM-YYYY HH:mm").format(change.getDate()));
        builder.append(" / ");
        builder.append(change.getUser().getName());
        builder.append(" ");
        builder.append(change.getUser().getSurname());
        return builder.toString();
    }

    public void exportReports(java.util.List<ReportData> reports) throws WriteException {
        if (reports == null || reports.isEmpty()) return;

        int y = 0;
        Label headerOne = new Label(0, ++y, composeHeaderOne(reports), headerCell);
        sheet.addCell(headerOne);
        sheet.mergeCells(0, y, 6, y);

        ++y;
        ++y;

        Label h1 = new Label(0, y, bundle.getString("report.data.store"), boldCellWithBorder);
        sheet.addCell(h1);

        Label h2 = new Label(1, y, bundle.getString("report.data.date"), boldCellWithBorder);
        sheet.addCell(h2);

        Label h3 = new Label(2, y, bundle.getString("report.data.id"), boldCellWithBorder);
        sheet.addCell(h3);

        Label h4 = new Label(3, y, bundle.getString("report.data.code"), boldCellWithBorder);
        sheet.addCell(h4);

        Label h5 = new Label(4, y, bundle.getString("report.data.name"), boldCellWithBorder);
        sheet.addCell(h5);

        Label h6 = new Label(5, y, bundle.getString("report.data.type"), boldCellWithBorder);
        sheet.addCell(h6);

        Label h7 = new Label(6, y, bundle.getString("report.data.price"), boldCellWithBorder);
        sheet.addCell(h7);

        BigDecimal sum = new BigDecimal("0");

        for (ReportData report : reports) {
            ++y;
            sheet.addCell(new Label(0, y, report.getStore(), normalCellWithBorder));
            sheet.addCell(new Label(1, y, new SimpleDateFormat("dd.MM.YYYY").format(report.getDate()), normalCellWithBorder));
            sheet.addCell(new Label(2, y, report.getId(), normalCellWithBorder));
            sheet.addCell(new Label(3, y, report.getCode(), normalCellWithBorder));
            sheet.addCell(new Label(4, y, report.getName(), normalCellWithBorder));
            sheet.addCell(new Label(5, y, report.getType(), normalCellWithBorder));
            sheet.addCell(new Label(6, y, String.format("%.2f", report.getPrice()) + "€", normalCellWithBorder));

            sum = sum.add(new BigDecimal(String.valueOf(report.getPrice())));
        }

        ++y;
        sheet.addCell(new Label(5, y, "Kopā: ", boldCellWithBorder));
        sheet.addCell(new Label(6, y,  sum + "€", boldCellWithBorder));
    }

    private String composeHeaderOne(java.util.List<ReportData> reports) {
        StringBuilder builder = new StringBuilder();
        builder.append("Finanšu pārskats ");
        builder.append(new SimpleDateFormat("dd.MM.YYYY").format(reports.get(0).getDate()));
        builder.append("-");
        builder.append(new SimpleDateFormat("dd.MM.YYYY").format(reports.get(reports.size() - 1).getDate()));
        return builder.toString();
    }

    public void serviceGoodsToExcel(List<ServiceGood> serviceGoods) throws WriteException {
        int y = 0;
        Label h1 = new Label(0, y, bundle.getString("service.good.id"), boldCellWithBorder);
        sheet.addCell(h1);

        Label h2 = new Label(1, y, bundle.getString("service.good.store"), boldCellWithBorder);
        sheet.addCell(h2);

        Label h3 = new Label(2, y, bundle.getString("service.good.category"), boldCellWithBorder);
        sheet.addCell(h3);

        Label h4 = new Label(3, y, bundle.getString("service.good.name"), boldCellWithBorder);
        sheet.addCell(h4);

        Label h5 = new Label(4, y, bundle.getString("service.good.status"), boldCellWithBorder);
        sheet.addCell(h5);

        Label h6 = new Label(5, y, bundle.getString("service.good.accumNum"), boldCellWithBorder);
        sheet.addCell(h6);

        Label h7 = new Label(6, y, bundle.getString("service.good.imei"), boldCellWithBorder);
        sheet.addCell(h7);

        Label h8 = new Label(7, y, bundle.getString("service.good.problem"), boldCellWithBorder);
        sheet.addCell(h8);

        Label h9 = new Label(8, y, bundle.getString("service.good.deliveredDate"), boldCellWithBorder);
        sheet.addCell(h9);

        Label h10 = new Label(9, y, bundle.getString("service.good.startDate"), boldCellWithBorder);
        sheet.addCell(h10);

        Label h11 = new Label(10, y, bundle.getString("service.good.finishDate"), boldCellWithBorder);
        sheet.addCell(h11);

        Label h12 = new Label(11, y, bundle.getString("service.good.returnedDate"), boldCellWithBorder);
        sheet.addCell(h12);

        Label h13 = new Label(12, y, bundle.getString("service.good.price"), boldCellWithBorder);
        sheet.addCell(h13);

        Label h14 = new Label(13, y, bundle.getString("service.good.contactName"), boldCellWithBorder);
        sheet.addCell(h14);

        Label h15 = new Label(14, y, bundle.getString("service.good.contactPhone"), boldCellWithBorder);
        sheet.addCell(h15);

        Label h16 = new Label(15, y, bundle.getString("service.good.contactMail"), boldCellWithBorder);
        sheet.addCell(h16);

        Label h17 = new Label(16, y, bundle.getString("service.good.additionalDescription"), boldCellWithBorder);
        sheet.addCell(h17);

        y++;

        for (ServiceGood sg : serviceGoods) {
            sheet.addCell(new Label(0, y, getValue(sg.getId()), normalCellWithBorder));
            sheet.addCell(new Label(1, y, getValue(sg.getStore().getName()), normalCellWithBorder));
            sheet.addCell(new Label(2, y, getValue(sg.getCategory()), normalCellWithBorder));
            sheet.addCell(new Label(3, y, getValue(sg.getName()), normalCellWithBorder));
            sheet.addCell(new Label(4, y, getValue(sg.getStatus()), normalCellWithBorder));
            sheet.addCell(new Label(5, y, getValue(sg.getAccumNum()), normalCellWithBorder));
            sheet.addCell(new Label(6, y, getValue(sg.getImei()), normalCellWithBorder));
            sheet.addCell(new Label(7, y, getValue(sg.getProblem()), normalCellWithBorder));
            sheet.addCell(new Label(8, y, getValue(sg.getDeliveredDate()), normalCellWithBorder));
            sheet.addCell(new Label(9, y, getValue(sg.getStartDate()), normalCellWithBorder));
            sheet.addCell(new Label(10, y, getValue(sg.getFinishDate()), normalCellWithBorder));
            sheet.addCell(new Label(11, y, getValue(sg.getReturnedDate()), normalCellWithBorder));
            sheet.addCell(new Label(12, y, getValue(sg.getPrice()), normalCellWithBorder));
            sheet.addCell(new Label(13, y, getValue(sg.getContactName()), normalCellWithBorder));
            sheet.addCell(new Label(14, y, getValue(sg.getContactPhone()), normalCellWithBorder));
            sheet.addCell(new Label(15, y, getValue(sg.getContactMail()), normalCellWithBorder));
            sheet.addCell(new Label(16, y, getValue(sg.getAdditionalDescription()), normalCellWithBorder));
            y++;
        }
    }

    public void stockGoodsToExcel(List<StockGood> stockGoods) throws WriteException {
        int y = 0;
        Label h1 = new Label(0, y, bundle.getString("stock.good.id"), boldCellWithBorder);
        sheet.addCell(h1);

        Label h2 = new Label(1, y, bundle.getString("stock.good.store"), boldCellWithBorder);
        sheet.addCell(h2);

        Label h3 = new Label(2, y, bundle.getString("stock.good.category"), boldCellWithBorder);
        sheet.addCell(h3);

        Label h4 = new Label(3, y, bundle.getString("stock.good.name"), boldCellWithBorder);
        sheet.addCell(h4);

        Label h5 = new Label(4, y, bundle.getString("stock.good.model"), boldCellWithBorder);
        sheet.addCell(h5);

        Label h6 = new Label(5, y, bundle.getString("stock.good.compatibleModels"), boldCellWithBorder);
        sheet.addCell(h6);

        Label h7 = new Label(6, y, bundle.getString("stock.good.lastSoldDate"), boldCellWithBorder);
        sheet.addCell(h7);

        Label h8 = new Label(7, y, bundle.getString("stock.good.price"), boldCellWithBorder);
        sheet.addCell(h8);

        Label h9 = new Label(8, y, bundle.getString("stock.good.count"), boldCellWithBorder);
        sheet.addCell(h9);

        Label h10 = new Label(9, y, bundle.getString("stock.good.total"), boldCellWithBorder);
        sheet.addCell(h10);

        Label h11 = new Label(10, y, bundle.getString("stock.good.sold"), boldCellWithBorder);
        sheet.addCell(h11);

        y++;

        for (StockGood sg : stockGoods) {
            sheet.addCell(new Label(0, y, getValue(sg.getId()), normalCellWithBorder));
            sheet.addCell(new Label(1, y, getValue(sg.getStore().getName()), normalCellWithBorder));
            sheet.addCell(new Label(2, y, getValue(sg.getCategory()), normalCellWithBorder));
            sheet.addCell(new Label(3, y, getValue(sg.getName()), normalCellWithBorder));
            sheet.addCell(new Label(4, y, getValue(sg.getModel()), normalCellWithBorder));
            sheet.addCell(new Label(5, y, getValue(sg.getCompatibleModels()), normalCellWithBorder));
            sheet.addCell(new Label(6, y, getValue(sg.getLastSoldDate()), normalCellWithBorder));
            sheet.addCell(new Label(7, y, getValue(sg.getPrice()), normalCellWithBorder));
            sheet.addCell(new Label(8, y, getValue(sg.getCount()), normalCellWithBorder));
            sheet.addCell(new Label(9, y, getValue(sg.getTotal()), normalCellWithBorder));
            sheet.addCell(new Label(10, y, getValue(sg.getSoldCount()), normalCellWithBorder));
            y++;
        }
    }

    private String getValue(Object o) {
        if (o == null) return "-";
        if (o instanceof Date) {
            return new SimpleDateFormat("dd.MM.YYYY").format((Date) o);
        } else if (o instanceof Double) {
            return String.format("%.2f", (Double) o);
        } else {
            return String.valueOf(o);
        }
    }

    public void close() throws IOException, WriteException {

        for(int x = 0; x <= sheet.getColumns(); x++) {
            CellView cell=sheet.getColumnView(x);
            cell.setAutosize(true);
            sheet.setColumnView(x, cell);
        }
        workbook.write();
        workbook.close();
    }

    public ByteArrayOutputStream getOutputStream() {
        return outputStream;
    }
}
