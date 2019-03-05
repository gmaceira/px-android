package com.mercadopago.android.px.internal.features.express.add_new_card;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mercadolibre.android.ui.widgets.MeliButton;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.viewmodel.drawables.AddNewCardFragmentDrawableFragmentItem;

public class AddNewCardLowResFragment extends AddNewCardFragment {

    private static final String ARG_MODEL = "ARG_MODEL";

    @SuppressWarnings("TypeMayBeWeakened")
    @NonNull
    public static Fragment getInstance(@NonNull final AddNewCardFragmentDrawableFragmentItem drawableItem) {
        final AddNewCardLowResFragment changePaymentMethodFragment = new AddNewCardLowResFragment();
        final Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_MODEL, drawableItem);
        changePaymentMethodFragment.setArguments(bundle);
        return changePaymentMethodFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
        @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.px_fragment_change_payment_method_low_res, container, false);
    }

    @Override
    protected void configureClick(@NonNull final View view) {
        final MeliButton message = view.findViewById(R.id.message);
        message.setText(getString(R.string.px_add_new_card));
        message.setOnClickListener(this);
    }
}
