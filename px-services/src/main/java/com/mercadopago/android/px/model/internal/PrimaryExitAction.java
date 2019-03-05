package com.mercadopago.android.px.model.internal;

import com.mercadopago.android.px.model.ExitAction;

public final class PrimaryExitAction extends ExitAction {

    public PrimaryExitAction(final ExitAction exitActionPrimary) {
        super(exitActionPrimary.getName(), exitActionPrimary.getResCode());
    }
}
