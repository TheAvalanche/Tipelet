package lv.telepit.ui.view.context.actions;

import lv.telepit.model.BusinessReceipt;
import lv.telepit.ui.view.BusinessReceiptView;

public class BusinessReceiptPaidAction extends AbstractAction<BusinessReceiptView> {

    private final BusinessReceipt good;

    public BusinessReceiptPaidAction(BusinessReceipt good, BusinessReceiptView view) {
        super(view);
        if (!good.isPaid()) {
            this.setCaption(bundle.getString("businessReceipt.paid"));
        } else {
            this.setCaption(bundle.getString("businessReceipt.nonPaid"));
        }
        this.good = good;
    }

    @Override
    public boolean show() {
        return true;
    }

    @Override
    public void execute() {
        good.setPaid(!good.isPaid());
        view.getUi().getBusinessReceiptService().updateBusinessReceipt(good);

    }
}