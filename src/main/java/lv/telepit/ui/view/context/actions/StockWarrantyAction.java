package lv.telepit.ui.view.context.actions;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import lv.telepit.model.StockGood;
import lv.telepit.ui.view.AbstractView;
import lv.telepit.utils.PdfUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StockWarrantyAction extends AbstractAction {

    private final StockGood good;

    public StockWarrantyAction(StockGood good, AbstractView view) {
        super(view);
        this.setCaption(bundle.getString("create.warranty"));
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
        subWindow.setWidth("200px");
        subWindow.setClosable(true);
        view.getUi().addWindow(subWindow);

        final Button pdfButton = new Button("Garantijas kvīts");
        StreamResource sr2 = getPDFStream();
        sr2.setMIMEType("application/pdf");
        FileDownloader fileDownloader2 = new FileDownloader(sr2);
        fileDownloader2.extend(pdfButton);

        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.addComponent(new Label("Izveidot kvītu?"));
        HorizontalLayout buttons = new HorizontalLayout(pdfButton);
        buttons.setSpacing(true);
        buttons.setMargin(true);
        layout.addComponent(buttons);

        subWindow.setContent(layout);
    }

    private StreamResource getPDFStream() {
        StreamResource.StreamSource source = new StreamResource.StreamSource() {

            public InputStream getStream() {
                try {
                    PdfUtils pdfCreator = new PdfUtils();
                    pdfCreator.open();
                    pdfCreator.createWarranty();
                    pdfCreator.close();
                    return new ByteArrayInputStream(pdfCreator.getOutputStream().toByteArray());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        return new StreamResource (source, createWarrantyName("pdf"));
    }

    private String createWarrantyName(String extension) {
        StringBuilder builder = new StringBuilder();
        builder.append("kvits");
        builder.append(new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()));
        builder.append("." + extension);
        return builder.toString();
    }
}
