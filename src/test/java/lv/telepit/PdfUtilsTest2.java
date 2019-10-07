package lv.telepit;

import com.itextpdf.text.DocumentException;
import lv.telepit.model.ServiceGood;
import lv.telepit.utils.PdfUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PdfUtilsTest2 {

	public static void main(String[] args) throws DocumentException, IOException {

		ServiceGood serviceGood = new ServiceGood();
		serviceGood.setCustomId("4444");

		PdfUtils pdfCreator = new PdfUtils();
		pdfCreator.open();
		pdfCreator.createBill(serviceGood);
		pdfCreator.close();
		Files.write(Paths.get("kvits.pdf"), pdfCreator.getOutputStream().toByteArray());

	}
}
