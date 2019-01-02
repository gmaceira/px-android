package com.mercadopago.android.px.internal.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mercadopago.android.px.model.commission.ChargeRule;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Set;

public class SummaryAmountBody {

    @NonNull private final String siteId;
    @NonNull private final BigDecimal transactionAmount;
    @NonNull private final String marketplace;
    @NonNull private final String email;
    @NonNull private final String productId;
    @NonNull private final String paymentMethodId;
    @NonNull private final String paymentType;
    @NonNull private final String bin;
    @NonNull private final Long issuerId;
    @NonNull private final Set<String> labels;
    @NonNull private final Integer defaultInstallments;
    @Nullable private final Integer differentialPricingId;
    @Nullable private final String processingMode;
    @Nullable private final Collection<ChargeRule> charges;

    public SummaryAmountBody(@NonNull final String siteId, @NonNull final BigDecimal transactionAmount,
        @NonNull final String marketplace, @NonNull final String email, @NonNull final String productId,
        @NonNull final String paymentMethodId, @NonNull final String paymentType, @NonNull final String bin,
        @NonNull final Long issuerId, @NonNull final Set<String> labels, @NonNull final Integer defaultInstallments,
        @Nullable final Integer differentialPricingId, @Nullable final String processingMode,
        @Nullable final Collection<ChargeRule> charges) {
        this.siteId = siteId;
        this.transactionAmount = transactionAmount;
        this.marketplace = marketplace;
        this.email = email;
        this.productId = productId;
        this.paymentMethodId = paymentMethodId;
        this.paymentType = paymentType;
        this.bin = bin;
        this.issuerId = issuerId;
        this.labels = labels;
        this.defaultInstallments = defaultInstallments;
        this.differentialPricingId = differentialPricingId;
        this.processingMode = processingMode;
        this.charges = charges;
    }
}