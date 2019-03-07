package com.mercadopago.android.px.internal.features.paymentresult.components;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.features.paymentresult.props.BodyErrorProps;
import com.mercadopago.android.px.internal.features.paymentresult.props.InstructionsProps;
import com.mercadopago.android.px.internal.features.paymentresult.props.PaymentResultBodyProps;
import com.mercadopago.android.px.internal.view.ActionDispatcher;
import com.mercadopago.android.px.internal.view.CompactComponent;
import com.mercadopago.android.px.internal.view.Component;
import com.mercadopago.android.px.internal.view.PaymentMethodBodyComponent;
import com.mercadopago.android.px.internal.view.Receipt;
import com.mercadopago.android.px.model.ExternalFragment;
import com.mercadopago.android.px.model.Payment;

public class Body extends Component<PaymentResultBodyProps, Void> {

    public Body(@NonNull final PaymentResultBodyProps props, @NonNull final ActionDispatcher dispatcher) {
        super(props, dispatcher);
    }

    public boolean hasInstructions() {
        return props.instruction != null;
    }

    public Instructions getInstructionsComponent() {
        final InstructionsProps instructionsProps = new InstructionsProps.Builder()
            .setInstruction(props.instruction)
            .setProcessingMode(props.processingMode)
            .build();
        return new Instructions(instructionsProps, getDispatcher());
    }

    public boolean hasBodyError() {
        return props.paymentResult.getPaymentStatus() != null
            && props.paymentResult.getPaymentStatusDetail() != null
            && (isPendingWithBody() || isRejectedWithBody());
    }

    private boolean isPendingWithBody() {
        return (Payment.StatusCodes.STATUS_PENDING.equals(props.paymentResult.getPaymentStatus())
            || Payment.StatusCodes.STATUS_IN_PROCESS.equals(props.paymentResult.getPaymentStatus()))
            &&
            (Payment.StatusDetail.STATUS_DETAIL_PENDING_CONTINGENCY.equals(props.paymentResult.getPaymentStatusDetail())
                || Payment.StatusDetail.STATUS_DETAIL_PENDING_REVIEW_MANUAL
                .equals(props.paymentResult.getPaymentStatusDetail()));
    }

    private boolean isRejectedWithBody() {
        boolean rightStatus = false;
        switch (props.paymentResult.getPaymentStatusDetail()) {
        case Payment.StatusDetail.STATUS_DETAIL_CC_REJECTED_INSUFFICIENT_AMOUNT:
        case Payment.StatusDetail.STATUS_DETAIL_REJECTED_BY_REGULATIONS:
        case Payment.StatusDetail.STATUS_DETAIL_CC_REJECTED_CARD_DISABLED:
        case Payment.StatusDetail.STATUS_DETAIL_CC_REJECTED_CALL_FOR_AUTHORIZE:
        case Payment.StatusDetail.STATUS_DETAIL_REJECTED_HIGH_RISK:
        case Payment.StatusDetail.STATUS_DETAIL_CC_REJECTED_MAX_ATTEMPTS:
        case Payment.StatusDetail.STATUS_DETAIL_CC_REJECTED_DUPLICATED_PAYMENT:
        case Payment.StatusDetail.STATUS_DETAIL_REJECTED_REJECTED_INSUFFICIENT_DATA:
        case Payment.StatusDetail.STATUS_DETAIL_REJECTED_REJECTED_BY_BANK:
        case Payment.StatusDetail.STATUS_DETAIL_CC_REJECTED_OTHER_REASON:
            rightStatus = true;
            break;
        default:
            break;
        }
        return Payment.StatusCodes.STATUS_REJECTED.equals(props.paymentResult.getPaymentStatus()) && rightStatus;
    }

    /* default */ boolean isStatusApproved() {
        return Payment.StatusCodes.STATUS_APPROVED.equals(props.paymentResult.getPaymentStatus());
    }

    public BodyError getBodyErrorComponent() {
        final BodyErrorProps bodyErrorProps = new BodyErrorProps.Builder()
            .setStatus(props.paymentResult.getPaymentStatus())
            .setStatusDetail(props.paymentResult.getPaymentStatusDetail())
            .setPaymentMethodName(props.paymentResult.getPaymentData().getPaymentMethod().getName())
            .build();
        return new BodyError(bodyErrorProps, getDispatcher());
    }

    public boolean hasReceipt() {
        return props.paymentResult.getPaymentId() != null
            && isStatusApproved();
    }

    public Receipt getReceiptComponent() {
        return new Receipt(new Receipt.ReceiptProps(String.valueOf(props.paymentResult.getPaymentId())));
    }

    public boolean hasTopCustomComponent() {
        return props.paymentResultScreenConfiguration.hasTopFragment();
    }

    public boolean hasBottomCustomComponent() {
        return props.paymentResultScreenConfiguration.hasBottomFragment();
    }

    public ExternalFragment topFragment() {
        return props.paymentResultScreenConfiguration.getTopFragment();
    }

    public ExternalFragment bottomFragment() {
        return props.paymentResultScreenConfiguration.getBottomFragment();
    }

    /* default */ CompactComponent getPaymentMethodBody() {
        return new PaymentMethodBodyComponent(PaymentMethodBodyComponent.PaymentMethodBodyProp
            .with(props.paymentResult.getPaymentDataList(), props.currencyId, props.paymentResult.getPaymentStatus()));
    }
}