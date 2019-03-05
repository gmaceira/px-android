package com.mercadopago.android.px.tracking.internal.mapper;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.viewmodel.mappers.Mapper;
import com.mercadopago.android.px.model.AccountMoneyMetadata;
import com.mercadopago.android.px.model.CardMetadata;
import com.mercadopago.android.px.model.ExpressMetadata;
import com.mercadopago.android.px.tracking.internal.model.AccountMoneyExtraInfo;
import com.mercadopago.android.px.tracking.internal.model.AvailableMethod;
import com.mercadopago.android.px.tracking.internal.model.CardExtraExpress;
import java.util.Set;

public class FromExpressMetadataToAvailableMethods extends Mapper<ExpressMetadata, AvailableMethod> {

    @NonNull private final Set<String> cardsWithEsc;
    @NonNull private final Set<String> cardsWithSplit;

    public FromExpressMetadataToAvailableMethods(@NonNull final Set<String> cardsWithEsc,
        @NonNull final Set<String> cardsWithSplit) {
        this.cardsWithEsc = cardsWithEsc;
        this.cardsWithSplit = cardsWithSplit;
    }

    @Override
    public AvailableMethod map(@NonNull final ExpressMetadata expressMetadata) {
        if (expressMetadata.isCard()) {
            final CardMetadata card = expressMetadata.getCard();
            return new AvailableMethod(expressMetadata.getPaymentMethodId(), expressMetadata.getPaymentTypeId(),
                CardExtraExpress
                    .expressSavedCard(card, cardsWithEsc.contains(card.getId()), cardsWithSplit.contains(card.getId()))
                    .toMap());
        } else if (expressMetadata.getAccountMoney() != null) {
            final AccountMoneyMetadata accountMoney = expressMetadata.getAccountMoney();
            return new AvailableMethod(expressMetadata.getPaymentMethodId(), expressMetadata.getPaymentTypeId(),
                new AccountMoneyExtraInfo(accountMoney.getBalance(), accountMoney.isInvested()).toMap());
        } else {
            return new AvailableMethod(expressMetadata.getPaymentMethodId(), expressMetadata.getPaymentTypeId());
        }
    }
}
