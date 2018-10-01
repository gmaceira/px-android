package com.mercadopago.android.px.internal.features.uicontrollers.paymentmethods.card;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mercadolibre.android.ui.utils.facebook.fresco.FrescoImageController;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.model.PaymentMethod;
import com.mercadopago.android.px.model.Token;

/**
 * Created by mreverter on 12/5/16.
 */
public class PaymentMethodOnEditableRow extends PaymentMethodOnView {

    protected Token mToken;

    public PaymentMethodOnEditableRow(Context context, PaymentMethod paymentMethod, Token token) {
        mContext = context;
        mPaymentMethod = paymentMethod;
        mToken = token;
    }

    @Override
    protected String getLastFourDigits() {
        String lastFourDigits = "";
        if (mToken != null) {
            lastFourDigits = mToken.getLastFourDigits();
        }
        return lastFourDigits;
    }

    @Override
    public View inflateInParent(ViewGroup parent, boolean attachToRoot) {
        mView = LayoutInflater.from(mContext)
            .inflate(R.layout.px_row_payment_method_card, parent, attachToRoot);
        return mView;
    }

    @Override
    public void draw() {
        super.draw();
        FrescoImageController.create().load(R.drawable.px_arrow_right_grey).into(mEditHint);
    }
}
