package com.mercadopago.android.px.internal.features.checkout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.mercadolibre.android.cardform.internal.LifecycleListener;
import com.mercadopago.android.px.internal.base.MvpView;
import com.mercadopago.android.px.internal.experiments.Variant;
import com.mercadopago.android.px.model.Card;
import com.mercadopago.android.px.model.Payment;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;

/* default */ interface Checkout {
    /* default */ interface View extends MvpView {

        void showError(final MercadoPagoError error);

        void showProgress();

        void showReviewAndConfirm(final boolean isUniquePaymentMethod);

        void showPaymentMethodSelection();

        void finishWithPaymentResult(@Nullable final Integer customResultCode, @Nullable final Payment payment);

        void cancelCheckout();

        void cancelCheckout(final MercadoPagoError mercadoPagoError);

        void cancelCheckout(final Integer customResultCode, final Boolean paymentMethodEdited);

        void showPaymentProcessorWithAnimation();

        boolean isActive();

        void showOneTap(@NonNull final Variant variant);

        void hideProgress();

        void transitionOut();

        void showSavedCardFlow(final Card card);

        void showNewCardFlow();

        void goToLink(@NonNull final String link);

        void openInWebView(@NonNull final String link);
    }

    /* default */ interface Actions {
        void initialize();

        void onErrorCancel(@Nullable final MercadoPagoError mercadoPagoError);

        void onPaymentMethodSelectionError(final MercadoPagoError mercadoPagoError);

        void onPaymentMethodSelectionCancel();

        void onReviewAndConfirmCancel();

        void onReviewAndConfirmError(final MercadoPagoError mercadoPagoError);

        void onCardFlowResponse();

        void onTerminalError(@NonNull final MercadoPagoError mercadoPagoError);

        void onCardFlowCancel();

        void onCustomReviewAndConfirmResponse(final Integer customResultCode);

        void recoverFromFailure();

        void onPaymentResultResponse(@Nullable final Integer customResultCode, @Nullable final String backUrl,
            @Nullable final String redirectUrl);

        void cancelCheckout();

        boolean isUniquePaymentMethod();

        void onChangePaymentMethod();

        void onCardAdded(@NonNull final String cardId, @NonNull final LifecycleListener.Callback callback);
    }
}
