package com.mercadopago.android.px.internal.datasource;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.callbacks.MPCall;
import com.mercadopago.android.px.internal.repository.IdentificationRepository;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.model.IdentificationType;
import java.util.List;

public class IdentificationService implements IdentificationRepository {

    @NonNull private final com.mercadopago.android.px.internal.services.IdentificationService identificationService;
    @NonNull private final PaymentSettingRepository paymentSettingRepository;

    public IdentificationService(
        @NonNull final com.mercadopago.android.px.internal.services.IdentificationService identificationService,
        @NonNull final PaymentSettingRepository paymentSettingRepository) {
        this.identificationService = identificationService;
        this.paymentSettingRepository = paymentSettingRepository;
    }

    @Override
    public MPCall<List<IdentificationType>> getIdentificationTypes() {
        return identificationService
            .getIdentificationTypes(paymentSettingRepository.getPublicKey(), paymentSettingRepository.getPrivateKey());
    }

    @Override
    public MPCall<List<IdentificationType>> getIdentificationTypesAsync(@NonNull final String accessToken) {
        return identificationService.getIdentificationTypes(null, accessToken);
    }
}
