package com.mercadopago.android.px.internal.datasource;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mercadopago.android.px.R;
import com.mercadopago.android.px.configuration.CustomStringConfiguration;

public final class PaymentVaultTitleSolverImpl implements PaymentVaultTitleSolver {

    @NonNull
    private final Context context;

    @NonNull
    private final CustomStringConfiguration stringConfiguration;

    public PaymentVaultTitleSolverImpl(@NonNull final Context context, @NonNull final CustomStringConfiguration stringConfiguration) {
        this.context = context;
        this.stringConfiguration = stringConfiguration;
    }

    @NonNull
    public String solveTitle() {
        if (stringConfiguration.hasCustomPaymentVaultTitle()) {
            return stringConfiguration.getCustomPaymentVaultTitle();
        } else {
            return getTitleWithMainVerb();
        }
    }

    @NonNull
    private String getTitleWithMainVerb() {
        String mainVerb = context.getString(stringConfiguration.getMainVerbStringResourceId());
        return context.getString(R.string.px_title_activity_payment_vault, mainVerb);
    }
}
