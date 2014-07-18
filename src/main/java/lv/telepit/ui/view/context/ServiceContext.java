package lv.telepit.ui.view.context;

import com.itextpdf.text.DocumentException;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.Action;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import lv.telepit.model.ChangeRecord;
import lv.telepit.model.ServiceGood;
import lv.telepit.model.ServiceStatus;
import lv.telepit.ui.view.ServiceView;
import lv.telepit.utils.PdfUtils;
import org.vaadin.dialogs.ConfirmDialog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;


/**
 * Created by Alex on 30/03/2014.
 */
public class ServiceContext implements Action.Handler {

    private static ResourceBundle bundle = ResourceBundle.getBundle("bundle");

    private final Action inWaiting = new Action(bundle.getString("service.status.waiting"));
    private final Action inRepair = new Action(bundle.getString("service.status.in.repair"));
    private final Action repaired = new Action(bundle.getString("service.status.repaired"));
    private final Action broken = new Action(bundle.getString("service.status.broken"));
    private final Action returned = new Action(bundle.getString("service.status.returned"));
    private final Action onDetails = new Action(bundle.getString("service.status.on.details"));
    private final Action printBill = new Action(bundle.getString("service.generate.bill"));
    private final Action showHistory = new Action(bundle.getString("show.history"));



    private ServiceView view;

    public ServiceContext(ServiceView view) {
        this.view = view;
    }

    @Override
    public Action[] getActions(Object target, Object sender) {
        if (target == null) return new Action[]{};

        switch (((ServiceGood) target).getStatus()) {

            case WAITING:
                return new Action[] {inRepair, printBill, showHistory};
            case IN_REPAIR:
                return new Action[] {repaired, broken, printBill, showHistory};
            case REPAIRED:
            case BROKEN:
                return new Action[] {returned, onDetails, printBill, showHistory};
            case RETURNED:
            case ON_DETAILS:
                return new Action[] {printBill, showHistory};
            default:
                return new Action[]{inWaiting, inRepair, repaired, broken, returned, onDetails, printBill, showHistory};
        }
    }

    @Override
    public void handleAction(final Action action, final Object sender, final Object target) {
        if (action == inWaiting) {
            changeStatusIfConfirmed(target, ServiceStatus.WAITING);
        } else if (action == inRepair) {
            changeStatusIfConfirmed(target, ServiceStatus.IN_REPAIR);
        } else if (action == repaired) {
            changeStatusIfConfirmed(target, ServiceStatus.REPAIRED);
        } else if (action == broken) {
            changeStatusIfConfirmed(target, ServiceStatus.BROKEN);
        } else if (action == returned) {
            changeStatusIfConfirmed(target, ServiceStatus.RETURNED);
        } else if (action == onDetails) {
            changeStatusIfConfirmed(target, ServiceStatus.ON_DETAILS);
        } else if (action == printBill) {
            generateBill((ServiceGood) target);
        } else if (action == showHistory) {
            showHistory((ServiceGood) target);
            view.refreshView();
        }
    }

    private void changeStatusIfConfirmed(final Object fromObject, final ServiceStatus toStatus){
        ConfirmDialog.show(view.getUi(),
                bundle.getString("service.view.changeStatus.header"),
                String.format(bundle.getString("service.view.changeStatus.message"), ((ServiceGood) fromObject).getStatus().toString(), toStatus.toString()),
                bundle.getString("default.button.ok"), bundle.getString("default.button.cancel"), new ConfirmDialog.Listener() {
                    @Override
                    public void onClose(ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            view.getUi().getServiceGoodService().changeStatus((ServiceGood) fromObject, toStatus);
                            view.refreshView();
                        }
                    }
                }
        );
    }

    private void generateBill(ServiceGood serviceGood) {
        Window subWindow = new Window();
        subWindow.setModal(true);
        subWindow.setHeight("170px");
        subWindow.setWidth("280px");
        subWindow.setClosable(true);
        view.getUi().addWindow(subWindow);

        final Button pdfButton = new Button("Tīrs kvīts");
        StreamResource sr = getPDFStream(new ServiceGood());
        sr.setMIMEType("application/pdf");
        FileDownloader fileDownloader = new FileDownloader(sr);
        fileDownloader.extend(pdfButton);

        final Button pdfButton2 = new Button("Aizpildīts kvīts");
        StreamResource sr2 = getPDFStream(serviceGood);
        sr2.setMIMEType("application/pdf");
        FileDownloader fileDownloader2 = new FileDownloader(sr2);
        fileDownloader2.extend(pdfButton2);

        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.addComponent(new Label("Izvelēties, kādu kvītu Jūs gribētu izveidot?"));
        HorizontalLayout buttons = new HorizontalLayout(pdfButton, pdfButton2);
        buttons.setSpacing(true);
        buttons.setMargin(true);
        layout.addComponent(buttons);

        subWindow.setContent(layout);
    }

    private void showHistory(ServiceGood serviceGood) {

        List<ChangeRecord> historyList = view.getUi().getServiceGoodService().findChanges(serviceGood);
        Window subWindow = new Window();
        subWindow.setModal(true);
        subWindow.setHeight("650px");
        subWindow.setWidth("700px");
        subWindow.setClosable(true);
        view.getUi().addWindow(subWindow);

        final Button pdfButton = new Button(bundle.getString("pdf.export"));
        pdfButton.setIcon(new ThemeResource("img/pdf.png"));
        pdfButton.setWidth("150");
        StreamResource pdfStream = getPDFStream(view.getUi().getServiceGoodService().findChanges(serviceGood));
        pdfStream.setMIMEType("application/pdf");
        FileDownloader pdfDownloader = new FileDownloader(pdfStream);
        pdfDownloader.extend(pdfButton);

        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.addComponent(pdfButton);

        for (ChangeRecord record : historyList) {
            for (ChangeRecord.PropertyChange p : record.getChangeList()) {
                p.setName(bundle.getString(p.getName()));
            }
            Panel panel = new Panel();
            BeanItemContainer<ChangeRecord.PropertyChange> container = new BeanItemContainer<>(ChangeRecord.PropertyChange.class, record.getChangeList());
            Table table = new Table();
            table.setContainerDataSource(container);
            table.setVisibleColumns("name", "oldValue", "newValue");
            table.setColumnHeaders(bundle.getString("history.property"), bundle.getString("history.old"), bundle.getString("history.new"));
            table.setColumnExpandRatio("name", 0.33f);
            table.setColumnExpandRatio("oldValue", 0.33f);
            table.setColumnExpandRatio("newValue", 0.33f);
            table.setPageLength(0);
            table.setWidth("100%");

            VerticalLayout panelLayout = new VerticalLayout();
            panelLayout.addComponent(new Label("<b>" + new SimpleDateFormat("dd-MM-YYYY HH:mm").format(record.getDate())
                    + ": " + record.getUser().getName() + " "
                    + record.getUser().getSurname() + "</b><br/>", ContentMode.HTML));
            panelLayout.addComponent(table);
            panel.setContent(panelLayout);
            layout.addComponent(panel);
        }

        subWindow.setContent(layout);
    }

    private StreamResource getPDFStream(final List<ChangeRecord> records) {
        StreamResource.StreamSource source = new StreamResource.StreamSource() {

            public InputStream getStream() {
                try {
                    PdfUtils pdfCreator = new PdfUtils();
                    pdfCreator.open();
                    pdfCreator.exportChanges(records);
                    pdfCreator.close();
                    return new ByteArrayInputStream(pdfCreator.getOutputStream().toByteArray());
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        return new StreamResource (source, createReportName("pdf"));
    }

    private StreamResource getPDFStream(final ServiceGood good) {
        StreamResource.StreamSource source = new StreamResource.StreamSource() {

            public InputStream getStream() {
                try {
                    PdfUtils pdfCreator = new PdfUtils();
                    pdfCreator.open();
                    pdfCreator.createBill(good);
                    pdfCreator.close();
                    return new ByteArrayInputStream(pdfCreator.getOutputStream().toByteArray());
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        return new StreamResource (source, createReportName("pdf"));
    }

    private String createReportName(String extension) {
        StringBuilder builder = new StringBuilder();
        builder.append("changeReport");
        builder.append(new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()));
        builder.append("." + extension);
        return builder.toString();
    }
}
