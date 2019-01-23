package com.mercadopago.android.px.internal.datasource;

import android.content.Context;
import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.callbacks.MPCall;
import com.mercadopago.android.px.internal.repository.CardTokenRepository;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.services.GatewayService;
import com.mercadopago.android.px.model.CardToken;
import com.mercadopago.android.px.model.Token;

public class CardTokenService implements CardTokenRepository {

    @NonNull private final GatewayService gatewayService;
    @NonNull private final Context context;
    @NonNull private final PaymentSettingRepository paymentSettingRepository;

    public CardTokenService(@NonNull final GatewayService gatewayService, @NonNull final Context context,
        @NonNull final PaymentSettingRepository paymentSettingRepository) {
        this.gatewayService = gatewayService;
        this.context = context;
        this.paymentSettingRepository = paymentSettingRepository;
    }

    @Override
    public MPCall<Token> createTokenAsync(final CardToken cardToken) {
        cardToken.setDevice(context);
        return gatewayService
            .createToken(paymentSettingRepository.getPublicKey(), paymentSettingRepository.getPrivateKey(), cardToken);
    }
}
