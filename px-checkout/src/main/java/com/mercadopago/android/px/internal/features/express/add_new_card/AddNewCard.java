package com.mercadopago.android.px.internal.features.express.add_new_card;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.base.MvpView;
import com.mercadopago.android.px.model.PaymentMethodSearchItem;

interface /* default */ AddNewCard {

    interface View extends MvpView {
        void showPaymentMethods();
        void showPaymentMethodsWithSelection(@NonNull final PaymentMethodSearchItem paymentMethodSearchItem);
    }

    interface Actions {
        void onAddNewCardSelected();
    }
}
