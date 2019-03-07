package com.mercadopago.android.px.internal.view;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.features.paymentresult.components.LineSeparator;
import com.mercadopago.android.px.internal.util.ViewUtils;
import com.mercadopago.android.px.model.PaymentData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PaymentMethodBodyComponent
    extends CompactComponent<PaymentMethodBodyComponent.PaymentMethodBodyProp, Void> {

    public static final class PaymentMethodBodyProp implements Parcelable {

        public static final Creator<PaymentMethodBodyComponent.PaymentMethodBodyProp> CREATOR =
            new Creator<PaymentMethodBodyComponent.PaymentMethodBodyProp>() {
                @Override
                public PaymentMethodBodyComponent.PaymentMethodBodyProp createFromParcel(final Parcel in) {
                    return new PaymentMethodBodyComponent.PaymentMethodBodyProp(in);
                }

                @Override
                public PaymentMethodBodyComponent.PaymentMethodBodyProp[] newArray(final int size) {
                    return new PaymentMethodBodyComponent.PaymentMethodBodyProp[size];
                }
            };

        /* default */ List<PaymentMethodComponent.PaymentMethodProps> paymentMethodProps;

        private PaymentMethodBodyProp() {
        }

        /* default */ PaymentMethodBodyProp(final Parcel in) {
            paymentMethodProps = new ArrayList<>();
            in.readList(paymentMethodProps, PaymentMethodComponent.PaymentMethodProps.class.getClassLoader());
        }

        public static PaymentMethodBodyComponent.PaymentMethodBodyProp with(
            @NonNull final Iterable<PaymentData> paymentDataList,
            @NonNull final String currencyId,
            @NonNull final String statementDescription) {
            final PaymentMethodBodyProp instance = new PaymentMethodBodyProp();
            instance.paymentMethodProps = new ArrayList<>();
            for (final PaymentData paymentData : paymentDataList) {
                instance.paymentMethodProps
                    .add(PaymentMethodComponent.PaymentMethodProps.with(paymentData, currencyId, statementDescription));
            }
            return instance;
        }

        public static PaymentMethodBodyProp with(
            final List<PaymentMethodComponent.PaymentMethodProps> paymentMethodProps) {
            final PaymentMethodBodyProp instance = new PaymentMethodBodyProp();
            instance.paymentMethodProps = paymentMethodProps;
            return instance;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(final Parcel dest, final int flags) {
            dest.writeTypedList(paymentMethodProps);
        }
    }

    public PaymentMethodBodyComponent(@NonNull final PaymentMethodBodyProp paymentMethodBodyProp) {
        super(paymentMethodBodyProp);
    }

    @Override
    public View render(@NonNull final ViewGroup parent) {

        final Context context = parent.getContext();
        final LinearLayout linearContainer = ViewUtils.createLinearContainer(context);
        PaymentMethodComponent paymentMethodComponent;
        final Iterator<PaymentMethodComponent.PaymentMethodProps> paymentMethodPropsIterator =
            props.paymentMethodProps.iterator();
        while (paymentMethodPropsIterator.hasNext()) {
            paymentMethodComponent = new PaymentMethodComponent(paymentMethodPropsIterator.next());
            linearContainer.addView(paymentMethodComponent.render(linearContainer));
            if (paymentMethodPropsIterator.hasNext()) {
                addNewSeparator(linearContainer);
            }
        }

        return linearContainer;
    }

    private void addNewSeparator(final LinearLayout view) {
        final LineSeparator lineSeparator = new LineSeparator(new LineSeparator.Props(R.color.px_med_light_gray));
        view.addView(lineSeparator.render(view));
    }
}