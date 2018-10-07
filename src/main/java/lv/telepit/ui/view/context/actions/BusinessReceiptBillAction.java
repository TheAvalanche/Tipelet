package lv.telepit.ui.view.context.actions;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import lv.telepit.model.BusinessReceipt;
import lv.telepit.ui.component.SpacedHorizontalLayout;
import lv.telepit.ui.view.BusinessReceiptView;
import lv.telepit.utils.PdfUtils;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BusinessReceiptBillAction extends AbstractAction<BusinessReceiptView> {

    private final BusinessReceipt good;

    public BusinessReceiptBillAction(BusinessReceipt good, BusinessReceiptView view) {
        super(view);
        this.setCaption(bundle.getString("service.generate.bill"));
        this.good = good;
    }

    @Override
    public boolean show() {
        return true;
    }

    @Override
    public void execute() {
        Window subWindow = new Window();
        subWindow.setModal(true);
        subWindow.setHeight("150px");
        subWindow.setWidth("185px");
        subWindow.setClosable(true);
        view.getUi().addWindow(subWindow);

        final Button pdfButton = new Button("Saglabāt rēķinu");
        StreamResource sr = getPDFStream(good);
        sr.setMIMEType("application/pdf");
        FileDownloader fileDownloader = new FileDownloader(sr);
        fileDownloader.extend(pdfButton);

        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        HorizontalLayout buttons = new SpacedHorizontalLayout(pdfButton);
        buttons.setMargin(true);
        layout.addComponent(buttons);

        subWindow.setContent(layout);
    }

    private StreamResource getPDFStream(final BusinessReceipt good) {
        StreamResource.StreamSource source = (StreamResource.StreamSource) () -> {
            try {
                PdfUtils pdfCreator = new PdfUtils();
                pdfCreator.open();
                pdfCreator.createInvoice(good);
                pdfCreator.close();
                return new ByteArrayInputStream(pdfCreator.getOutputStream().toByteArray());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        };
        return new StreamResource (source, createBillName("pdf"));
    }

    private String createBillName(String extension) {
        StringBuilder builder = new StringBuilder();
        builder.append("invoice");
        builder.append(new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()));
        builder.append(".").append(extension);
        return builder.toString();
    }
}
