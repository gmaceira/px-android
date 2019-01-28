package com.mercadopago.android.px.internal.features.providers;

import android.content.Context;
import android.text.TextUtils;

import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.di.Session;

public class PaymentVaultProviderImpl implements PaymentVaultProvider {

    private final Context context;

    public PaymentVaultProviderImpl(final Context context) {
        this.context = context;
    }

    @Override
    public String getTitle() {
        final String mainVerb = context.getString(Session.getSession(context).getMainVerb());
        final String customTitle = Session.getSession(context).getCustomTitle();

        String title;
        if(TextUtils.isEmpty(customTitle)){
            title = context.getString(R.string.px_title_activity_payment_vault, mainVerb);
        } else {
            title = customTitle;
        }

        return title;
    }

    @Override
    public String getAllPaymentTypesExcludedErrorMessage() {
        return context.getString(R.string.px_error_message_excluded_all_payment_type);
    }

    @Override
    public String getInvalidDefaultInstallmentsErrorMessage() {
        return context.getString(R.string.px_error_message_invalid_default_installments);
    }

    @Override
    public String getInvalidMaxInstallmentsErrorMessage() {
        return context.getString(R.string.px_error_message_invalid_max_installments);
    }

    @Override
    public String getStandardErrorMessage() {
        return context.getString(R.string.px_standard_error_message);
    }

    @Override
    public String getEmptyPaymentMethodsErrorMessage() {
        return context.getString(R.string.px_no_payment_methods_found);
    }

}
