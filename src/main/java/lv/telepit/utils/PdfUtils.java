package lv.telepit.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lv.telepit.model.BusinessReceipt;
import lv.telepit.model.ChangeRecord;
import lv.telepit.model.ReceiptItem;
import lv.telepit.model.ServiceGood;
import lv.telepit.model.dto.ReportData;
import org.apache.commons.lang3.StringUtils;
import pl.allegro.finance.tradukisto.MoneyConverters;

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

    private Font headerFont;
    private Font header2Font;
    private Font boldFont;
    private Font normalFont;
    private Font boldBlueFont;
    private Font smallBoldFont;
    private Font smallFont;


    public PdfUtils() throws DocumentException {
        try {
            BaseFont arial = BaseFont.createFont("arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            headerFont = new Font(arial, 14, Font.BOLD);
            header2Font = new Font(arial, 12, Font.BOLD);
            boldFont = new Font(arial, 10, Font.BOLD);
            normalFont = new Font(arial, 10, Font.NORMAL);
            boldBlueFont = new Font(arial, 12, Font.NORMAL, BaseColor.BLUE);
            smallBoldFont = new Font(arial, 8, Font.BOLD);
            smallFont = new Font(arial, 6, Font.NORMAL);

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
        if (changes == null || changes.isEmpty()) return;

        for (ChangeRecord change : changes) {
            Paragraph headerOne = new Paragraph(composeHeaderOne(change), boldBlueFont);
            headerOne.setAlignment(Element.ALIGN_CENTER);
            Paragraph headerTwo = new Paragraph(composeHeaderTwo(change), normalFont);
            headerTwo.setAlignment(Element.ALIGN_CENTER);
            document.add(new Paragraph(" "));
            document.add(headerOne);
            document.add(headerTwo);
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(3);

            PdfPCell h1 = new PdfPCell(new Phrase(bundle.getString("history.property"), boldFont));
            h1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(h1);

            PdfPCell h2 = new PdfPCell(new Phrase(bundle.getString("history.old"), boldFont));
            h2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(h2);

            PdfPCell h3 = new PdfPCell(new Phrase(bundle.getString("history.new"), boldFont));
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
        builder.append(change.getStockGood() != null ? bundle.getString("stock.type") : bundle.getString("service.type"));
        builder.append(" ");
        builder.append(change.getStockGood() != null ? change.getStockGood().getName() : change.getServiceGood().getName());
        builder.append(" (ID=");
        builder.append(change.getStockGood() != null ? change.getStockGood().getIncrementId() : change.getServiceGood().getCustomId());
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
        if (reports == null || reports.isEmpty()) return;

        Paragraph headerOne = new Paragraph(composeHeaderOne(reports), boldBlueFont);
        headerOne.setAlignment(Element.ALIGN_CENTER);
        document.add(new Paragraph(" "));
        document.add(headerOne);
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setSpacingBefore(0f);
        table.setSpacingAfter(0f);

        PdfPCell h1 = new PdfPCell(new Phrase(bundle.getString("report.data.store"), boldFont));
        h1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(h1);

        PdfPCell h2 = new PdfPCell(new Phrase(bundle.getString("report.data.date"), boldFont));
        h2.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(h2);

        PdfPCell h3 = new PdfPCell(new Phrase(bundle.getString("report.data.id"), boldFont));
        h3.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(h3);

        PdfPCell h4 = new PdfPCell(new Phrase(bundle.getString("report.data.code"), boldFont));
        h4.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(h4);

        PdfPCell h5 = new PdfPCell(new Phrase(bundle.getString("report.data.name"), boldFont));
        h5.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(h5);

        PdfPCell h6 = new PdfPCell(new Phrase(bundle.getString("report.data.price"), boldFont));
        h6.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(h6);

        PdfPCell h7 = new PdfPCell(new Phrase(bundle.getString("report.data.info"), boldFont));
        h7.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(h7);

        table.setHeaderRows(1);

        BigDecimal sum = new BigDecimal("0");

        for (ReportData report : reports) {
            table.addCell(new Phrase(report.getStore(), normalFont));
            table.addCell(new Phrase(new SimpleDateFormat().format(report.getDate()), normalFont));
            table.addCell(new Phrase(report.getId(), normalFont));
            table.addCell(new Phrase(report.getCode(), normalFont));
            table.addCell(new Phrase(report.getName(), normalFont));
            table.addCell(new Phrase(String.format("%.2f", report.getPrice()) + "€", normalFont));
            table.addCell(new Phrase(report.getInfo(), normalFont));

            sum = sum.add(new BigDecimal(String.valueOf(report.getPrice())));
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

    public void createBill(ServiceGood serviceGood) throws DocumentException, IOException {
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setSpacingBefore(0f);
        headerTable.setSpacingAfter(0f);
        headerTable.setWidths(new int[]{2, 5});
        PdfPCell headerLeft = new PdfPCell();
        PdfPCell headerRight = new PdfPCell();
        PdfPTable headerInnerLeft = new PdfPTable(1);
        headerInnerLeft.setWidthPercentage(100);
        headerInnerLeft.setSpacingBefore(0f);
        headerInnerLeft.setSpacingAfter(0f);
        headerInnerLeft.addCell(new Phrase("Pasūtīšanas forma", smallFont));
        PdfPCell actor = new PdfPCell(new Phrase("IZPILDĪTĀJS", smallBoldFont));
        actor.setFixedHeight(80f);
        headerInnerLeft.addCell(actor);

        PdfPTable headerInnerRight = new PdfPTable(1);
        headerInnerRight.setWidthPercentage(100);
        headerInnerRight.setSpacingBefore(0f);
        headerInnerRight.setSpacingAfter(0f);
        PdfPCell phrase = new PdfPCell(new Phrase("Iekārtas Nosaukums: " + StringUtils.defaultIfEmpty(serviceGood.getName(), ""), smallBoldFont));
        phrase.setFixedHeight(15f);
        headerInnerRight.addCell(phrase);
        PdfPCell phrase1 = new PdfPCell(new Phrase("IMEI vai S/N: " + StringUtils.defaultIfEmpty(serviceGood.getImei(), ""), smallFont));
        phrase1.setFixedHeight(15f);
        headerInnerRight.addCell(phrase1);
        PdfPCell phrase2 = new PdfPCell(new Phrase("Komplektācija:" +
                "                 " +
                "\u25a1 Akumulators" +
                "                 " +
                "\u25a1 Lādētājs", smallFont));
        phrase2.setFixedHeight(20f);
        headerInnerRight.addCell(phrase2);
        PdfPCell phrase3 = new PdfPCell(new Phrase("Vizuālie defekti:", smallFont));
        phrase3.setFixedHeight(20f);
        headerInnerRight.addCell(phrase3);
        PdfPCell phrase4 = new PdfPCell(new Phrase("Remonts:" +
                "                 " +
                "\u25a1 Maksas" +
                "                 " +
                "\u25a1 Garantijas" +
                "                 " +
                "\u25a1 Atkārtots", smallFont));
        phrase4.setFixedHeight(20f);
        headerInnerRight.addCell(phrase4);

        headerLeft.addElement(headerInnerLeft);
        headerRight.addElement(headerInnerRight);
        headerTable.addCell(headerLeft);
        headerTable.addCell(headerRight);

        PdfPTable footerTable = new PdfPTable(2);
        footerTable.setWidthPercentage(100);
        footerTable.setSpacingBefore(0f);
        footerTable.setSpacingAfter(0f);
        footerTable.setWidths(new int[]{3, 4});
        PdfPCell footerLeft = new PdfPCell();
        PdfPCell footerRight = new PdfPCell();
        PdfPTable footerInnerLeft = new PdfPTable(1);
        footerInnerLeft.setWidthPercentage(100);
        footerInnerLeft.setSpacingBefore(0f);
        footerInnerLeft.setSpacingAfter(0f);
        PdfPCell phrase5 = new PdfPCell(new Phrase("Pieņemšanas datums: " + (serviceGood.getDeliveredDate() != null ? new SimpleDateFormat("dd-MM-YYYY HH:mm").format(serviceGood.getDeliveredDate()) : ""), smallFont));
        phrase5.setFixedHeight(20f);
        footerInnerLeft.addCell(phrase5);
        PdfPCell phrase6 = new PdfPCell(new Phrase("Izpildes termiņš:", smallFont));
        phrase6.setFixedHeight(20f);
        footerInnerLeft.addCell(phrase6);
        PdfPCell phrase7 = new PdfPCell(new Phrase("Orientējoša remonta cena: " + (serviceGood.getPrice() != null ? String.format("%.2f", serviceGood.getPrice()) : ""), smallFont));
        phrase7.setFixedHeight(20f);
        footerInnerLeft.addCell(phrase7);
        PdfPCell phrase8 = new PdfPCell(new Phrase("Pieņēma: " + (serviceGood.getUser() != null ? serviceGood.getUser().getName() + " " + serviceGood.getUser().getSurname() : "") + "\n\n\n(datums, paraksts)", smallFont));
        phrase8.setFixedHeight(35f);
        footerInnerLeft.addCell(phrase8);
        PdfPCell phrase9 = new PdfPCell(new Phrase("PASŪTĪTĀJS", smallBoldFont));
        phrase9.setFixedHeight(15f);
        footerInnerLeft.addCell(phrase9);
        PdfPCell phrase10 = new PdfPCell(new Phrase("Vārds/Uzvārds: " + StringUtils.defaultIfEmpty(serviceGood.getContactName(), ""), smallFont));
        phrase10.setFixedHeight(20f);
        footerInnerLeft.addCell(phrase10);
        PdfPCell phrase11 = new PdfPCell(new Phrase("Kontakttālrunis: " + StringUtils.defaultIfEmpty(serviceGood.getContactPhone(), ""), smallFont));
        phrase11.setFixedHeight(20f);
        footerInnerLeft.addCell(phrase11);
        PdfPCell phrase12 = new PdfPCell(new Phrase("Klienta e-pasts: " + StringUtils.defaultIfEmpty(serviceGood.getContactMail(), ""), smallFont));
        phrase12.setFixedHeight(20f);
        footerInnerLeft.addCell(phrase12);
        PdfPCell phrase13 = new PdfPCell(new Phrase("Iekārtas nodevu:\n\n\n\n(datums, paraksts)", smallFont));
        phrase13.setFixedHeight(35f);
        footerInnerLeft.addCell(phrase13);
        PdfPCell phrase14 = new PdfPCell(new Phrase("Iekārtas saņēmu, pretenziju nav:\n\n\n\n(datums, paraksts)", smallFont));
        phrase14.setFixedHeight(35f);
        footerInnerLeft.addCell(phrase14);


        PdfPTable footerInnerRight = new PdfPTable(1);
        footerInnerRight.setWidthPercentage(100);
        footerInnerRight.setSpacingBefore(0f);
        footerInnerRight.setSpacingAfter(0f);
        PdfPCell pasCell = new PdfPCell(new Phrase("Pasūtījums: (klienta sūdzības)\n\n " + StringUtils.defaultIfEmpty(serviceGood.getProblem(), ""), smallBoldFont));
        pasCell.setFixedHeight(120f);
        footerInnerRight.addCell(pasCell);
        PdfPCell pakCell = new PdfPCell(new Phrase("Pakalpojums: (veiktie darbi)", smallBoldFont));
        pakCell.setFixedHeight(120f);
        footerInnerRight.addCell(pakCell);

        footerLeft.addElement(footerInnerLeft);
        footerRight.addElement(footerInnerRight);
        footerTable.addCell(footerLeft);
        footerTable.addCell(footerRight);

        Image image = Image.getInstance(getClass().getResource("/kvits.png"));
        float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                - document.rightMargin()) / image.getWidth()) * 100;
        image.scalePercent(scaler);
        image.setSpacingAfter(0);

        document.add(headerTable);
        document.add(footerTable);
        document.add(new Paragraph("\n\n"));
        document.add(image);

    }

    public void createWarranty() throws DocumentException, IOException {
        Image image = Image.getInstance(getClass().getResource("/garantija.png"));
        float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                - document.rightMargin()) / image.getWidth()) * 100;
        image.scalePercent(scaler);
        image.setSpacingAfter(0);
        document.add(image);

        Paragraph footer = new Paragraph(
                "         "
                        + "Prece: "
                        + "                                                   "
                        + "Izsniedzējs:"
                        + "                                                   "
                        + "Datums:", normalFont);
        Paragraph footerLines = new Paragraph(
                "        "
                        + "_________________"
                        + "                  "
                        + "_________________"
                        + "                        "
                        + "________________");
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(footer);
        document.add(footerLines);
    }

    public void createInvoice(BusinessReceipt businessReceipt) throws DocumentException, IOException {
        Paragraph headerOne = new Paragraph("Rēķins Nr. " + businessReceipt.getNumber(), headerFont);
        headerOne.setAlignment(Element.ALIGN_CENTER);

        PdfPTable dateTable = new PdfPTable(2);
        dateTable.setWidthPercentage(100);
        dateTable.setWidths(new int[]{2, 5});

        dateTable.addCell(noBCell("Izrakstīšanas datums", normalFont));
        dateTable.addCell(noBCell(new SimpleDateFormat("dd/MM/yyyy").format(businessReceipt.getDate()), boldFont));
        
        PdfPTable providerTable = new PdfPTable(2);
        providerTable.setWidthPercentage(100);
        providerTable.setWidths(new int[]{2, 5});

        providerTable.addCell(noBCell("Piegādātājs", normalFont));
        providerTable.addCell(noBCell(businessReceipt.getProviderName(), header2Font));
        providerTable.addCell(noBCell("Reg.Nr.", normalFont));
        providerTable.addCell(noBCell(businessReceipt.getProviderRegNum(), boldFont));
        providerTable.addCell(noBCell("Jurid. adrese", normalFont));
        providerTable.addCell(noBCell(businessReceipt.getProviderLegalAddress(), boldFont));
        providerTable.addCell(noBCell("Norēķinu rekvizīti", normalFont));
        providerTable.addCell(noBCell(businessReceipt.getProviderBankName(), boldFont));
        providerTable.addCell(noBCell("Konts", normalFont));
        providerTable.addCell(noBCell(businessReceipt.getProviderBankNum(), boldFont));
        providerTable.addCell(noBCell("Izsniegšanas vieta", normalFont));
        providerTable.addCell(noBCell(businessReceipt.getProviderAddress(), boldFont));
                
        providerTable.addCell(empty2());
        providerTable.addCell(hr());
        providerTable.addCell(empty2());

        PdfPTable receiverTable = new PdfPTable(2);
        receiverTable.setWidthPercentage(100);
        receiverTable.setWidths(new int[]{2, 5});

        receiverTable.addCell(noBCell("Maksātājs", normalFont));
        receiverTable.addCell(noBCell(businessReceipt.getReceiverName(), header2Font));
        receiverTable.addCell(noBCell("Reg.Nr.", normalFont));
        receiverTable.addCell(noBCell(businessReceipt.getReceiverRegNum(), boldFont));
        receiverTable.addCell(noBCell("Jurid. adrese", normalFont));
        receiverTable.addCell(noBCell(businessReceipt.getReceiverLegalAddress(), boldFont));
        receiverTable.addCell(noBCell("Norēķinu rekvizīti", normalFont));
        receiverTable.addCell(noBCell(businessReceipt.getReceiverBankName(), boldFont));
        receiverTable.addCell(noBCell("Konts", normalFont));
        receiverTable.addCell(noBCell(businessReceipt.getReceiverBankNum(), boldFont));

        receiverTable.addCell(empty2());
        receiverTable.addCell(hr());
        receiverTable.addCell(empty2());

        PdfPTable itemsTable = new PdfPTable(6);
        itemsTable.setWidthPercentage(100);
        itemsTable.setWidths(new int[]{1, 5, 2, 2, 2, 2});
        
        itemsTable.addCell(new PdfPCell(new Phrase("№", boldFont)));
        itemsTable.addCell(new PdfPCell(new Phrase("Preču nosaukums", boldFont)));
        itemsTable.addCell(new PdfPCell(new Phrase("Mērv.", boldFont)));
        itemsTable.addCell(new PdfPCell(new Phrase("Daudz.", boldFont)));
        itemsTable.addCell(new PdfPCell(new Phrase("Cena", boldFont)));
        itemsTable.addCell(new PdfPCell(new Phrase("Summa", boldFont)));
        

        int i = 0;
        for (ReceiptItem receiptItem : businessReceipt.getReceiptItems()) {
            itemsTable.addCell(new PdfPCell(new Phrase(String.valueOf(++i), normalFont)));
            itemsTable.addCell(new PdfPCell(new Phrase(receiptItem.getName(), boldFont)));
            itemsTable.addCell(new PdfPCell(new Phrase("gb", normalFont)));
            itemsTable.addCell(new PdfPCell(new Phrase(String.valueOf(receiptItem.getCount()), normalFont)));
            itemsTable.addCell(new PdfPCell(new Phrase(String.valueOf(receiptItem.getPrice()), normalFont)));
            itemsTable.addCell(new PdfPCell(new Phrase(receiptItem.getTotalPrice().toString(), normalFont)));
        }
        
        itemsTable.addCell(empty());
        itemsTable.addCell(noBCell("Kopā", normalFont));
        itemsTable.addCell(empty());
        itemsTable.addCell(new PdfPCell(new Phrase(String.valueOf(businessReceipt.totalAmount()), normalFont)));
        itemsTable.addCell(empty());
        itemsTable.addCell(new PdfPCell(new Phrase("EUR " + String.valueOf(businessReceipt.getTotalPrice()), normalFont)));
        
        itemsTable.addCell(empty());
        itemsTable.addCell(noBCell("Pievienotās vērtības nodoklis", normalFont));
        itemsTable.addCell(empty());
        itemsTable.addCell(empty());
        itemsTable.addCell(noBCell("0%", normalFont));
        itemsTable.addCell(new PdfPCell(new Phrase(" ", normalFont)));

        itemsTable.addCell(empty());
        itemsTable.addCell(noBCell("Pavisam apmaksai", normalFont));
        itemsTable.addCell(empty());
        itemsTable.addCell(empty());
        itemsTable.addCell(empty());
        itemsTable.addCell(new PdfPCell(new Phrase("EUR " + String.valueOf(businessReceipt.getTotalPrice()), boldFont)));

        MoneyConverters converter = MoneyConverters.LATVIAN_BANKING_MONEY_VALUE;
        String moneyAsWords = converter.asWords(businessReceipt.getTotalPrice());
        moneyAsWords = moneyAsWords.replace("EUR", "eiro").replace("/100", " centi");
        
        PdfPTable sumTable = new PdfPTable(2);
        sumTable.setWidthPercentage(100);
        sumTable.setWidths(new int[]{1, 5});

        sumTable.addCell(noBCell("Vārdiem:", normalFont));
        sumTable.addCell(noBCell(moneyAsWords, boldFont));
        
        document.add(headerOne);
        document.add(new Paragraph(" "));
        document.add(dateTable);
        document.add(new Paragraph(" "));
        document.add(providerTable);
        document.add(receiverTable);
        document.add(new Paragraph(" "));
        document.add(itemsTable);
        document.add(new Paragraph(" "));
        document.add(sumTable);
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Rēķins ir sagatavots elektroniski un ir derīgs bez paraksta.", boldFont));
        
        
    }

    private PdfPCell hr() {
        PdfPCell cell = new PdfPCell(new Phrase(" ", normalFont));
        cell.setBorder(Rectangle.BOTTOM);
        cell.setColspan(2);
        return cell;
    }

    private PdfPCell empty() {
        PdfPCell empty = new PdfPCell(new Phrase(" ", normalFont));
        empty.setBorder(Rectangle.NO_BORDER);
        return empty;
    }
    
    private PdfPCell empty2() {
        PdfPCell empty = new PdfPCell(new Phrase(" ", normalFont));
        empty.setBorder(Rectangle.NO_BORDER);
        empty.setColspan(2);
        return empty;
    }

    private PdfPCell noBCell(String phrase, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(phrase, font));
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    public ByteArrayOutputStream getOutputStream() {
        return outputStream;
    }
}
