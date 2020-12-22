package lv.telepit.ui.view.context.actions;

import com.vaadin.data.util.BeanItem;
import lv.telepit.model.BusinessReceipt;
import lv.telepit.ui.view.BusinessReceiptView;

public class BusinessReceiptFromTemplateAction extends AbstractAction<BusinessReceiptView> {

    private final BusinessReceipt good;

    public BusinessReceiptFromTemplateAction(BusinessReceipt good, BusinessReceiptView view) {
        super(view);
        this.setCaption(bundle.getString("businessReceipt.from.template"));
        this.good = good;
    }

    @Override
    public boolean show() {
        return true;
    }

    @Override
    public void execute() {
        BusinessReceipt businessReceipt = new BusinessReceipt();
        businessReceipt.setReceiverBankName(good.getReceiverBankName());
        businessReceipt.setReceiverBankNum(good.getReceiverBankNum());
        businessReceipt.setReceiverLegalAddress(good.getReceiverLegalAddress());
        businessReceipt.setReceiverRealAddress(good.getReceiverRealAddress());
        businessReceipt.setReceiverName(good.getReceiverName());
        businessReceipt.setReceiverRegNum(good.getReceiverRegNum());
        businessReceipt.setReceiverPVNRegNum(good.getReceiverPVNRegNum());
        businessReceipt.setReceiverMail(good.getReceiverMail());
        businessReceipt.setReceiverPhone(good.getReceiverPhone());

        businessReceipt.setAgreementNum(good.getAgreementNum());
        businessReceipt.setAgreementDate(good.getAgreementDate());
        businessReceipt.setPaymentDeadLine(good.getPaymentDeadLine());

        view.openBusinessReceiptForm(new BeanItem<>(businessReceipt));
    }

    @Override
    public boolean refreshViewAfter() {
        return false;
    }
}
