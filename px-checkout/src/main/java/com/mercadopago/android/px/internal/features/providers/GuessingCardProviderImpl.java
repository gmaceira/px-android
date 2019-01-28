package com.mercadopago.android.px.internal.features.providers;

import android.content.Context;
import android.support.annotation.NonNull;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.callbacks.TaggedCallback;
import com.mercadopago.android.px.internal.datasource.MercadoPagoServicesAdapter;
import com.mercadopago.android.px.internal.di.Session;
import com.mercadopago.android.px.model.BankDeal;
import com.mercadopago.android.px.model.CardToken;
import com.mercadopago.android.px.model.IdentificationType;
import com.mercadopago.android.px.model.Token;
import java.util.List;

public class GuessingCardProviderImpl implements GuessingCardProvider {

    private final Context context;
    private final MercadoPagoServicesAdapter mercadoPago;

    public GuessingCardProviderImpl(@NonNull final Context context) {
        this.context = context;
        final Session session = Session.getSession(context);
        mercadoPago = session.getMercadoPagoServiceAdapter();
    }

    @Override
    public void createTokenAsync(final CardToken cardToken, final TaggedCallback<Token> taggedCallback) {
        mercadoPago.createToken(cardToken, taggedCallback);
    }

    @Override
    public void getIdentificationTypesAsync(final TaggedCallback<List<IdentificationType>> taggedCallback) {
        mercadoPago.getIdentificationTypes(taggedCallback);
    }

    @Override
    public void getIdentificationTypesAsync(final String accessToken,
        final TaggedCallback<List<IdentificationType>> taggedCallback) {
        mercadoPago.getIdentificationTypes(accessToken, taggedCallback);
    }

    @Override
    public void getBankDealsAsync(final TaggedCallback<List<BankDeal>> taggedCallback) {
        mercadoPago.getBankDeals(taggedCallback);
    }

    @Override
    public String getMissingPayerCostsErrorMessage() {
        return context.getString(R.string.px_error_message_missing_payer_cost);
    }

    @Override
    public String getMissingIdentificationTypesErrorMessage() {
        return context.getString(R.string.px_error_message_missing_identification_types);
    }

    @Override
    public String getInvalidIdentificationNumberErrorMessage() {
        return context.getString(R.string.px_invalid_identification_number);
    }

    @Override
    public String getInvalidExpiryDateErrorMessage() {
        return context.getString(R.string.px_invalid_expiry_date);
    }

    @Override
    public String getInvalidEmptyNameErrorMessage() {
        return context.getString(R.string.px_invalid_empty_name);
    }

    @Override
    public String getSettingNotFoundForBinErrorMessage() {
        return context.getString(R.string.px_error_message_missing_setting_for_bin);
    }

    @Override
    public String getInvalidFieldErrorMessage() {
        return context.getString(R.string.px_invalid_field);
    }
}
