package com.mercadopago.android.px.tracking.internal.views;

import android.support.annotation.NonNull;
import java.util.HashMap;
import java.util.Map;

public class TermsAndConditionsViewTracker extends ViewTracker {

    private static final String PATH = BASE_VIEW_PATH + "/payments/terms_and_conditions";
    @NonNull private final String url;

    public TermsAndConditionsViewTracker(@NonNull final String url) {
        this.url = url;
    }

    @NonNull
    @Override
    public Map<String, Object> getData() {
        final Map<String, Object> data = new HashMap<>();
        data.put("url", url);
        return data;
    }

    @NonNull
    @Override
    public String getViewPath() {
        return PATH;
    }
}
