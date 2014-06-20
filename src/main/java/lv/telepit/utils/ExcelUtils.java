package lv.telepit.utils;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import jxl.CellView;
import jxl.Workbook;
import jxl.format.*;
import jxl.format.BoldStyle;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;
import lv.telepit.model.ChangeRecord;
import lv.telepit.model.dto.ReportData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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

            Label h1 = new Label(0, y, "Vertība", boldCellWithBorder);
            sheet.addCell(h1);

            Label h2 = new Label(1, y, "Vecā vertība", boldCellWithBorder);
            sheet.addCell(h2);

            Label h3 = new Label(2, y, "Jaunā vertība", boldCellWithBorder);
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
        builder.append(change.getStockGood() != null ? "Noliktavas Prece:" : "Servisa prece:");
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

        Label h1 = new Label(0, y, "Veikals", boldCellWithBorder);
        sheet.addCell(h1);

        Label h2 = new Label(1, y, "Datums", boldCellWithBorder);
        sheet.addCell(h2);

        Label h3 = new Label(2, y, "ID", boldCellWithBorder);
        sheet.addCell(h3);

        Label h4 = new Label(3, y, "Kods", boldCellWithBorder);
        sheet.addCell(h4);

        Label h5 = new Label(4, y, "Nosaukums", boldCellWithBorder);
        sheet.addCell(h5);

        Label h6 = new Label(5, y, "Avots", boldCellWithBorder);
        sheet.addCell(h6);

        Label h7 = new Label(6, y, "Cena", boldCellWithBorder);
        sheet.addCell(h7);

        BigDecimal sum = new BigDecimal("0");

        for (ReportData report : reports) {
            ++y;
            sheet.addCell(new Label(0, y, report.getStore(), normalCellWithBorder));
            sheet.addCell(new Label(1, y, new SimpleDateFormat().format(report.getDate()), normalCellWithBorder));
            sheet.addCell(new Label(2, y, report.getId(), normalCellWithBorder));
            sheet.addCell(new Label(3, y, report.getCode(), normalCellWithBorder));
            sheet.addCell(new Label(4, y, report.getName(), normalCellWithBorder));
            sheet.addCell(new Label(5, y, report.getType(), normalCellWithBorder));
            sheet.addCell(new Label(6, y, report.getPrice() + "€", normalCellWithBorder));

            sum = sum.add(new BigDecimal(report.getPrice()));
        }

        ++y;
        sheet.addCell(new Label(6, y, "Kopā: ", boldCellWithBorder));
        sheet.addCell(new Label(7, y,  sum + "€", boldCellWithBorder));
    }

    private String composeHeaderOne(java.util.List<ReportData> reports) {
        StringBuilder builder = new StringBuilder();
        builder.append("Finanšu pārskats ");
        builder.append(new SimpleDateFormat("dd.MM.YYYY").format(reports.get(0).getDate()));
        builder.append("-");
        builder.append(new SimpleDateFormat("dd.MM.YYYY").format(reports.get(reports.size() - 1).getDate()));
        return builder.toString();
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
