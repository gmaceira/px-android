package com.mercadopago.android.px.tracking.internal.events;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.tracking.internal.views.ViewTracker;

public final class SecondaryActionEvent extends EventTracker {

    private static final String ACTION_PATH = "/secondary_action";

    @NonNull private final ViewTracker viewTracker;

    public SecondaryActionEvent(@NonNull final ViewTracker viewTracker) {
        this.viewTracker = viewTracker;
    }

    @NonNull
    @Override
    public String getEventPath() {
        return viewTracker.getViewPath() + ACTION_PATH;
    }
}
