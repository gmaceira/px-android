package com.mercadopago.android.px.internal.repository;

import com.mercadopago.android.px.internal.callbacks.MPCall;
import com.mercadopago.android.px.model.BankDeal;
import java.util.List;

public interface BankDealsRepository {
    MPCall<List<BankDeal>> getBankDealsAsync();
}
