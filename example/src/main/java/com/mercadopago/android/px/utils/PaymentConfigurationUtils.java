package com.mercadopago.android.px.utils;

import android.support.annotation.NonNull;
import com.mercadopago.SamplePaymentProcessor;
import com.mercadopago.android.px.configuration.PaymentConfiguration;
import com.mercadopago.android.px.core.PaymentProcessor;

public final class PaymentConfigurationUtils {
    private PaymentConfigurationUtils() {
        //Do nothing
    }

    public static PaymentConfiguration create(@NonNull final PaymentProcessor paymentProcessor) {
        return new PaymentConfiguration.Builder(paymentProcessor).build();
    }

    public static PaymentConfiguration create() {
        return create(new SamplePaymentProcessor());
    }
}
