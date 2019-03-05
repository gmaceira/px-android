package com.mercadopago.android.px.internal.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import com.mercadopago.android.px.R;

/**
 * Created by nfortuna on 11/1/17.
 */

public class LoadingRenderer extends Renderer<LoadingComponent> {

    @Override
    public View render(@NonNull final LoadingComponent component, @NonNull final Context context, final ViewGroup parent) {
        return inflate(R.layout.px_view_progress_bar, parent);
    }
}
