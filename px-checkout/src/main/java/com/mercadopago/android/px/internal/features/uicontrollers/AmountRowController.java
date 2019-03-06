package com.mercadopago.android.px.internal.features.uicontrollers;

import android.support.annotation.NonNull;

import com.mercadopago.android.px.configuration.AdvancedConfiguration;

public class AmountRowController {

    private final AdvancedConfiguration.AmountRow amountRow;
    private final AdvancedConfiguration advancedConfiguration;

    public AmountRowController(@NonNull final AdvancedConfiguration.AmountRow amountRow, @NonNull final AdvancedConfiguration advancedConfiguration) {
        this.amountRow = amountRow;
        this.advancedConfiguration = advancedConfiguration;
    }

    public void initialize(){
        if(advancedConfiguration.isAmountRowEnabled()){
            amountRow.showAmountRow();
        } else {
            amountRow.hideAmountRow();
        }
    }
}
