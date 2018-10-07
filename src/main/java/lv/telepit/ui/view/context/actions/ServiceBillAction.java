package lv.telepit.ui.view.context.actions;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import lv.telepit.model.ServiceGood;
import lv.telepit.ui.component.SpacedHorizontalLayout;
import lv.telepit.ui.view.ServiceView;
import lv.telepit.utils.PdfUtils;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServiceBillAction extends AbstractAction<ServiceView> {

    private final ServiceGood good;

    public ServiceBillAction(ServiceGood good, ServiceView view) {
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
        subWindow.setHeight("180px");
        subWindow.setWidth("280px");
        subWindow.setClosable(true);
        view.getUi().addWindow(subWindow);

        final Button pdfButton = new Button("Tīrs kvīts");
        StreamResource sr = getPDFStream(new ServiceGood());
        sr.setMIMEType("application/pdf");
        FileDownloader fileDownloader = new FileDownloader(sr);
        fileDownloader.extend(pdfButton);

        final Button pdfButton2 = new Button("Aizpildīts kvīts");
        StreamResource sr2 = getPDFStream(good);
        sr2.setMIMEType("application/pdf");
        FileDownloader fileDownloader2 = new FileDownloader(sr2);
        fileDownloader2.extend(pdfButton2);

        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.addComponent(new Label("Izvelēties, kādu kvītu Jūs gribētu izveidot?"));
        HorizontalLayout buttons = new SpacedHorizontalLayout(pdfButton, pdfButton2);
        buttons.setMargin(true);
        layout.addComponent(buttons);

        subWindow.setContent(layout);
    }

    private StreamResource getPDFStream(final ServiceGood good) {
        StreamResource.StreamSource source = (StreamResource.StreamSource) () -> {
            try {
                PdfUtils pdfCreator = new PdfUtils();
                pdfCreator.open();
                pdfCreator.createBill(good);
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
        builder.append("kvits");
        builder.append(new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()));
        builder.append("." + extension);
        return builder.toString();
    }
}
