package com.mercadopago.android.px.internal.features.paymentresult.components;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.util.FragmentUtil;
import com.mercadopago.android.px.internal.util.ViewUtils;
import com.mercadopago.android.px.internal.view.Renderer;
import com.mercadopago.android.px.internal.view.RendererFactory;

public class BodyRenderer extends Renderer<Body> {

    @Override
    public View render(@NonNull final Body component, @NonNull final Context context, final ViewGroup parent) {
        final View bodyView = inflate(R.layout.px_payment_result_body, parent);
        final ViewGroup bodyViewGroup = bodyView.findViewById(R.id.mpsdkPaymentResultContainerBody);

        if (component.hasInstructions()) {
            RendererFactory.create(context, component.getInstructionsComponent()).render(bodyViewGroup);
        } else if (component.hasBodyError()) {
            RendererFactory.create(context, component.getBodyErrorComponent()).render(bodyViewGroup);
        } else {

            if (component.hasReceipt()) {
                RendererFactory.create(context, component.getReceiptComponent()).render(bodyViewGroup);
            }

            if (component.hasTopCustomComponent()) {
                FragmentUtil.addFragmentInside(bodyViewGroup,
                    R.id.px_fragment_container_top,
                    component.topFragment());
            }

            if (component.isStatusApproved()) {
                final View pmBody = component.getPaymentMethodBody().render(bodyViewGroup);
                ViewUtils.stretchHeight(pmBody);
                bodyViewGroup.addView(pmBody);
            }

            if (component.hasBottomCustomComponent()) {
                FragmentUtil.addFragmentInside(bodyViewGroup,
                    R.id.px_fragment_container_bottom,
                    component.bottomFragment());
            }
        }

        ViewUtils.stretchHeight(bodyViewGroup);

        return bodyView;
    }
}