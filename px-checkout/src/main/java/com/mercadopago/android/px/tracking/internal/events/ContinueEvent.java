package com.mercadopago.android.px.tracking.internal.events;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.tracking.internal.views.ViewTracker;

public final class ContinueEvent extends EventTracker {

    private static final String CONTINUE_PATH = "/continue";

    @NonNull private final ViewTracker viewTracker;

    public ContinueEvent(@NonNull final ViewTracker viewTracker) {
        this.viewTracker = viewTracker;
    }

    @NonNull
    @Override
    public String getEventPath() {
        return viewTracker.getViewPath() + CONTINUE_PATH;
    }
}
