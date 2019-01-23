package com.mercadopago.android.px.internal.repository;

import com.mercadopago.android.px.internal.callbacks.MPCall;
import com.mercadopago.android.px.model.CardToken;
import com.mercadopago.android.px.model.Token;

public interface CardTokenRepository {

    MPCall<Token> createTokenAsync(final CardToken cardToken);
}
