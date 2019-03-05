package com.mercadopago.android.px.model.internal;

import com.mercadopago.android.px.model.ExitAction;

public final class SecondaryExitAction extends ExitAction {

    public SecondaryExitAction(final ExitAction exitActionPrimary) {
        super(exitActionPrimary.getName(), exitActionPrimary.getResCode());
    }
}
