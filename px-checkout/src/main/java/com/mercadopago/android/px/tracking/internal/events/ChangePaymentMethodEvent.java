package com.mercadopago.android.px.tracking.internal.events;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.tracking.internal.views.ViewTracker;

public final class ChangePaymentMethodEvent extends EventTracker {

    private static final String CHANGE_PATH = "/change_payment_method";

    public static ChangePaymentMethodEvent with(@NonNull final ViewTracker viewTracker) {
        return new ChangePaymentMethodEvent(viewTracker.getViewPath() + CHANGE_PATH);
    }

    public static ChangePaymentMethodEvent create() {
        return new ChangePaymentMethodEvent(BASE_PATH + "/review/traditional" + CHANGE_PATH);
    }

    @NonNull private final String path;

    private ChangePaymentMethodEvent(@NonNull final String path) {
        this.path = path;
    }

    @NonNull
    @Override
    public String getEventPath() {
        return path;
    }
}
