package com.mercadopago.android.px.internal.features.uicontrollers;

import android.support.annotation.NonNull;

import com.mercadopago.android.px.configuration.AdvancedConfiguration;

public class AmountRowController {

    private final AmountRowVisibilityBehaviour amountRow;
    private final AdvancedConfiguration advancedConfiguration;

    public AmountRowController(@NonNull final AmountRowVisibilityBehaviour amountRow, @NonNull final AdvancedConfiguration advancedConfiguration) {
        this.amountRow = amountRow;
        this.advancedConfiguration = advancedConfiguration;
    }

    public void configure(){
        if(advancedConfiguration.isAmountRowEnabled()){
            amountRow.showAmountRow();
        } else {
            amountRow.hideAmountRow();
        }
    }

    public interface AmountRowVisibilityBehaviour {
        void showAmountRow();
        void hideAmountRow();
    }
}
