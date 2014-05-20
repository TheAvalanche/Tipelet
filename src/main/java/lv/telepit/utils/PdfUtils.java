package lv.telepit.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lv.telepit.model.ChangeRecord;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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


    public PdfUtils() throws DocumentException {
        try {
            BaseFont arial = BaseFont.createFont("arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            boldFont = new Font(arial, 10, Font.BOLD);
            normalFont = new Font(arial, 10, Font.NORMAL);
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

    public void addMetaData() {
        document.addTitle("My first PDF");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Lars Vogel");
        document.addCreator("Lars Vogel");
    }

    //TODO: Pretty formatting
    public void exportChanges(java.util.List<ChangeRecord> changes) throws DocumentException {

        for (ChangeRecord change : changes) {
            Paragraph header = new Paragraph(composeChengeRecordHeader(change), boldFont);
            document.add(new Paragraph(" "));
            document.add(header);
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

    private String composeChengeRecordHeader(ChangeRecord change) {
        StringBuilder builder = new StringBuilder();
        builder.append(change.getStockGood() != null ? "Noliktavas Prece" : "Servisa prece");
        builder.append(" ");
        builder.append(change.getStockGood() != null ? change.getStockGood().getName() : change.getServiceGood().getName());
        builder.append(" ");
        builder.append(change.getStockGood() != null ? change.getStockGood().getId() : change.getServiceGood().getId());
        builder.append(" : ");
        builder.append(new SimpleDateFormat("dd-MM-YYYY HH:mm").format(change.getDate()));
        builder.append(" : ");
        builder.append(change.getUser().getName());
        builder.append(" ");
        builder.append(change.getUser().getSurname());
        return builder.toString();
    }

    private static void createList(Section subCatPart) {
        List list = new List(true, false, 10);
        list.add(new ListItem("First point"));
        list.add(new ListItem("Second point"));
        list.add(new ListItem("Third point"));
        subCatPart.add(list);
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    public ByteArrayOutputStream getOutputStream() {
        return outputStream;
    }
}
