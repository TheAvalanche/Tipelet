package lv.telepit;

import com.itextpdf.text.DocumentException;
import lv.telepit.model.BusinessReceipt;
import lv.telepit.model.ReceiptItem;
import lv.telepit.utils.PdfUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PdfUtilsTest {

	public static void main(String[] args) throws DocumentException, IOException {

		BusinessReceipt br = new BusinessReceipt();
		br.setDate(new Date());
		br.setNumber("08052018-1");

		br.setProviderName("SIA Telepit");
		br.setProviderAddress("Aleksandra Čaka iela 86, Rīga");
		br.setProviderRegNum("40103302498");
		br.setProviderBankName("Swedbank");
		br.setProviderBankNum("LV19UNLA0050015609749");
		br.setProviderLegalAddress("Paula Lejina 22 dz.33 ,Rīga, Latvija, LV-1029");

		br.setReceiverName("A/S Sunningdale tech");
		br.setReceiverRegNum("40103302498");
		br.setReceiverLegalAddress("Šampetera iela 2, LV-1046");
		br.setReceiverBankName("Swedbank");
		br.setReceiverBankNum("LV31HABA0551023365789");
		br.setAgreementNum("20.20.20");
		br.setAgreementDate(new Date());
		br.setPaymentDeadLine(30L);

		List<ReceiptItem> receiptItems = new ArrayList<>();

		ReceiptItem receiptItem = new ReceiptItem();
		receiptItem.setName("Test 1");
		receiptItem.setCount(5);
		receiptItem.setPrice(20.10);

		ReceiptItem receiptItem2 = new ReceiptItem();
		receiptItem2.setName("Test 2");
		receiptItem2.setCount(15);
		receiptItem2.setPrice(1.49);

		receiptItems.add(receiptItem);
		receiptItems.add(receiptItem2);

		br.setReceiptItems(receiptItems);

		PdfUtils pdfCreator = new PdfUtils();
		pdfCreator.open();
		pdfCreator.createInvoice(br);
		pdfCreator.close();
		Files.write(Paths.get("invoice.pdf"), pdfCreator.getOutputStream().toByteArray());

	}
}
