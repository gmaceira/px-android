package com.mercadopago.android.px.internal.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import com.mercadopago.android.px.internal.callbacks.PaymentServiceHandler;
import com.mercadopago.android.px.model.ExpressMetadata;
import com.mercadopago.android.px.model.GenericPayment;
import com.mercadopago.android.px.model.IPayment;
import com.mercadopago.android.px.model.PayerCost;
import com.mercadopago.android.px.model.PaymentData;
import com.mercadopago.android.px.model.PaymentRecovery;
import com.mercadopago.android.px.model.PaymentResult;
import java.util.List;

public interface PaymentRepository {

    void startPayment();

    void startExpressPayment(@NonNull final ExpressMetadata selectedPaymentMethod,
        @Nullable final PayerCost payerCost, final boolean splitPayment);

    boolean isExplodingAnimationCompatible();

    @NonNull
    @Size(min = 1)
    List<PaymentData> getPaymentDataList();

    @NonNull
    PaymentResult createPaymentResult(IPayment genericPayment);

    @NonNull
    PaymentResult createPaymentResult(@NonNull GenericPayment payment);

    int getPaymentTimeout();

    void attach(@NonNull final PaymentServiceHandler handler);

    void detach(@NonNull final PaymentServiceHandler handler);

    void storePayment(@NonNull final IPayment iPayment);

    @Nullable
    IPayment getPayment();

    boolean hasPayment();

    @NonNull
    PaymentRecovery createRecoveryForInvalidESC();

    @NonNull
    PaymentRecovery createPaymentRecovery();
}
