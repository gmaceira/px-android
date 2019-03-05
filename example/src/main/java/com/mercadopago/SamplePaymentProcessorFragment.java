package com.mercadopago;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mercadopago.android.px.core.PaymentProcessor;
import com.mercadopago.android.px.model.BusinessPayment;
import com.mercadopago.android.px.model.GenericPayment;
import com.mercadopago.example.R;

public class SamplePaymentProcessorFragment extends Fragment {

    private static final long CONST_TIME_MILLIS = 2000;
    public static final String ARG_BUSINESS = "ARG_BUSINESS";
    public static final String ARG_GENERIC = "ARG_GENERIC";

    @Nullable
    private PaymentProcessor.OnPaymentListener paymentListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
        @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.px_view_progress_bar, container, false);
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        if (context instanceof PaymentProcessor.OnPaymentListener) {
            paymentListener = (PaymentProcessor.OnPaymentListener) context;
        }
    }

    @Override
    public void onDetach() {
        paymentListener = null;
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (paymentListener != null) {
                    if (getArguments().containsKey(ARG_BUSINESS)) {
                        paymentListener
                            .onPaymentFinished((BusinessPayment) getArguments().getSerializable(ARG_BUSINESS));
                    } else if (getArguments().containsKey(ARG_GENERIC)) {
                        paymentListener
                            .onPaymentFinished((GenericPayment) getArguments().getSerializable(ARG_GENERIC));
                    }
                }
            }
        }, CONST_TIME_MILLIS);
    }
}
