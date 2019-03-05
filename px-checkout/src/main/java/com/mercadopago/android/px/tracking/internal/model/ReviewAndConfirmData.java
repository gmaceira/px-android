package com.mercadopago.android.px.tracking.internal.model;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.math.BigDecimal;
import java.util.List;

@SuppressWarnings("unused")
@Keep
public class ReviewAndConfirmData extends AvailableMethod {

    @NonNull private final List<ItemInfo> items;
    @NonNull private final BigDecimal preferenceAmount;
    @Nullable private final DiscountInfo discount;

    public ReviewAndConfirmData(@NonNull final AvailableMethod method,
        @NonNull final List<ItemInfo> itemInfos,
        @NonNull final BigDecimal totalAmount,
        @Nullable final DiscountInfo discount) {
        super(method.paymentMethodId, method.paymentMethodType);
        items = itemInfos;
        preferenceAmount = totalAmount;
        this.discount = discount;
    }
}
