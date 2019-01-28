package com.mercadopago.android.px.internal.features.providers;

import com.mercadopago.android.px.internal.base.ResourcesProvider;
import com.mercadopago.android.px.internal.callbacks.TaggedCallback;
import com.mercadopago.android.px.model.BankDeal;
import com.mercadopago.android.px.model.CardToken;
import com.mercadopago.android.px.model.IdentificationType;
import com.mercadopago.android.px.model.Token;
import java.util.List;

public interface GuessingCardProvider extends ResourcesProvider {

    void createTokenAsync(CardToken cardToken, final TaggedCallback<Token> taggedCallback);

    void getIdentificationTypesAsync(final TaggedCallback<List<IdentificationType>> taggedCallback);

    void getIdentificationTypesAsync(final String accessToken,
        final TaggedCallback<List<IdentificationType>> taggedCallback);

    void getBankDealsAsync(final TaggedCallback<List<BankDeal>> taggedCallback);

    String getMissingPayerCostsErrorMessage();

    String getMissingIdentificationTypesErrorMessage();

    String getInvalidIdentificationNumberErrorMessage();

    String getInvalidExpiryDateErrorMessage();

    String getInvalidEmptyNameErrorMessage();

    String getSettingNotFoundForBinErrorMessage();

    String getInvalidFieldErrorMessage();
}
