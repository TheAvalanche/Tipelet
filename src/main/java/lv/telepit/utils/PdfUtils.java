package lv.telepit.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lv.telepit.model.ChangeRecord;
import lv.telepit.model.dto.ReportData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

/**
 * Created by Alex on 12/05/2014.
 */
public class PdfUtils {

    private ByteArrayOutputStream outputStream;
    private Document document;
    private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");

    private Font boldFont;
    private Font normalFont;
    private Font boldBlueFont;


    public PdfUtils() throws DocumentException {
        try {
            BaseFont arial = BaseFont.createFont("arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            boldFont = new Font(arial, 10, Font.BOLD);
            normalFont = new Font(arial, 10, Font.NORMAL);
            boldBlueFont = new Font(arial, 12, Font.NORMAL, BaseColor.BLUE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        outputStream = new ByteArrayOutputStream();
        document = new Document();
        PdfWriter.getInstance(document, outputStream);
    }

    public void open() {
        document.open();
    }

    public void close() {
        document.close();
    }

    public void exportChanges(java.util.List<ChangeRecord> changes) throws DocumentException {

        for (ChangeRecord change : changes) {
            Paragraph headerOne = new Paragraph(composeHeaderOne(change), boldBlueFont);
            headerOne.setAlignment(Element.ALIGN_CENTER);
            Paragraph headerTwo = new Paragraph(composeHeaderTwo(change));
            headerTwo.setAlignment(Element.ALIGN_CENTER);
            document.add(new Paragraph(" "));
            document.add(headerOne);
            document.add(headerTwo);
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(3);

            PdfPCell h1 = new PdfPCell(new Phrase("Vertība", boldFont));
            h1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(h1);

            PdfPCell h2 = new PdfPCell(new Phrase("Vecā vertība", boldFont));
            h2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(h2);

            PdfPCell h3 = new PdfPCell(new Phrase("Jaunā vertība", boldFont));
            h3.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(h3);

            table.setHeaderRows(1);

            for (ChangeRecord.PropertyChange p : change.getChangeList()) {
                table.addCell(new Phrase(bundle.getString(p.getName()), normalFont));
                table.addCell(new Phrase(p.getOldValue(), normalFont));
                table.addCell(new Phrase(p.getNewValue(), normalFont));
            }
            document.add(table);
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

    public void exportReports(java.util.List<ReportData> reports) throws DocumentException {
        Paragraph headerOne = new Paragraph(composeHeaderOne(reports), boldBlueFont);
        headerOne.setAlignment(Element.ALIGN_CENTER);
        document.add(new Paragraph(" "));
        document.add(headerOne);
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingBefore(0f);
        table.setSpacingAfter(0f);

        PdfPCell h1 = new PdfPCell(new Phrase("Veikals", boldFont));
        h1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(h1);

        PdfPCell h2 = new PdfPCell(new Phrase("Datums", boldFont));
        h2.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(h2);

        PdfPCell h3 = new PdfPCell(new Phrase("ID", boldFont));
        h3.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(h3);

        PdfPCell h4 = new PdfPCell(new Phrase("Kods", boldFont));
        h4.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(h4);

        PdfPCell h5 = new PdfPCell(new Phrase("Nosaukums", boldFont));
        h5.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(h5);

        PdfPCell h6 = new PdfPCell(new Phrase("Cena", boldFont));
        h6.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(h6);

        table.setHeaderRows(1);

        BigDecimal sum = new BigDecimal("0");

        for (ReportData report : reports) {
            table.addCell(new Phrase(report.getStore(), normalFont));
            table.addCell(new Phrase(new SimpleDateFormat().format(report.getDate()), normalFont));
            table.addCell(new Phrase(report.getId(), normalFont));
            table.addCell(new Phrase(report.getCode(), normalFont));
            table.addCell(new Phrase(report.getName(), normalFont));
            table.addCell(new Phrase(report.getPrice() + "€", normalFont));

            sum = sum.add(new BigDecimal(report.getPrice()));
        }
        document.setMargins(20, 20, 20, 20);
        document.add(table);
        document.add(new Paragraph("Kopā: " + sum + "€", boldFont));
    }

    private String composeHeaderOne(java.util.List<ReportData> reports) {
        StringBuilder builder = new StringBuilder();
        builder.append("Finanšu pārskats ");
        builder.append(new SimpleDateFormat("dd.MM.YYYY").format(reports.get(0).getDate()));
        builder.append("-");
        builder.append(new SimpleDateFormat("dd.MM.YYYY").format(reports.get(reports.size() - 1).getDate()));
        return builder.toString();
    }

    public ByteArrayOutputStream getOutputStream() {
        return outputStream;
    }
}
