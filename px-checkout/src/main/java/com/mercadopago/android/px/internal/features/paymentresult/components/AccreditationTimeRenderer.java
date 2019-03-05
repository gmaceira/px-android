package com.mercadopago.android.px.internal.features.paymentresult.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.ViewGroup;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.view.MPTextView;
import com.mercadopago.android.px.internal.view.Renderer;
import com.mercadopago.android.px.internal.view.RendererFactory;
import java.util.List;

public class AccreditationTimeRenderer extends Renderer<AccreditationTime> {

    @Override
    public View render(@NonNull final AccreditationTime component,
        @NonNull final Context context, final ViewGroup parent) {

        final View accreditationTimeView = inflate(R.layout.px_accreditation_time, parent);
        final MPTextView messageTextView = accreditationTimeView.findViewById(R.id.mpsdkAccreditationTimeMessage);
        final ViewGroup accreditationCommentsContainer =
            accreditationTimeView.findViewById(R.id.mpsdkAccreditationTimeComments);

        renderAccreditationMessage(messageTextView, component, context);

        if (component.hasAccreditationComments()) {
            renderAccreditationComments(accreditationCommentsContainer, component, context);
        }

        return accreditationTimeView;
    }

    private void renderAccreditationMessage(@NonNull final MPTextView messageTextView,
        @NonNull final AccreditationTime component,
        @NonNull final Context context) {

        final String accreditationMessage = component.props.accreditationMessage;
        if (accreditationMessage == null || accreditationMessage.isEmpty()) {
            messageTextView.setVisibility(View.GONE);
        } else {
            final CharSequence textSpan = new SpannableStringBuilder("  " + accreditationMessage);

            final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.px_time);
            final Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
            final PorterDuff.Mode mode = PorterDuff.Mode.SRC_ATOP;
            drawable.setColorFilter(ContextCompat.getColor(context, R.color.px_warm_grey_with_alpha), mode);

            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

            messageTextView.setText(textSpan);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                messageTextView.setCompoundDrawablesRelative(drawable, null, null, null);
            } else {
                messageTextView.setCompoundDrawables(drawable, null, null, null);
            }
            messageTextView.setVisibility(View.VISIBLE);
        }
    }

    private void renderAccreditationComments(@NonNull final ViewGroup parent,
        @NonNull final AccreditationTime component,
        @NonNull final Context context) {

        final List<AccreditationComment> commentComponents = component.getAccreditationCommentComponents();

        for (final AccreditationComment commentComp : commentComponents) {
            final Renderer commentRenderer = RendererFactory.create(context, commentComp);
            final View accreditationComment = commentRenderer.render();
            parent.addView(accreditationComment);
        }
    }
}
