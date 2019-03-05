package com.mercadopago.android.px.internal.features.express.animations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.view.View;
import com.mercadopago.android.px.R;

public class SlideAnim {

    private static final String ANIMATOR_AXIS_PROPERTY = "y";
    private final ObjectAnimator slideUpAnim;
    private final ObjectAnimator slideDownAnim;

    /* default */ Position pos;

    private enum Position {
        DOWN,
        UP
    }

    public SlideAnim(@NonNull final View target) {
        pos = Position.UP;
        final long duration = (long) target.getContext().getResources().getInteger(R.integer.px_default_animation_time);
        slideUpAnim = ObjectAnimator.ofFloat(target, ANIMATOR_AXIS_PROPERTY, 0, 0);
        slideDownAnim = ObjectAnimator.ofFloat(target, ANIMATOR_AXIS_PROPERTY, 0, 0);
        slideUpAnim.setDuration(duration);
        slideDownAnim.setDuration(duration);
    }

    public void slideDown(final float initialPosition, final float endPosition) {
        if (pos == Position.UP) {
            slideDownAnim.removeAllListeners();
            slideDownAnim.setFloatValues(initialPosition, endPosition);
            slideDownAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(final Animator animation) {
                    pos = Position.DOWN;
                }
            });
            slideDownAnim.start();
        } else {
            slideUpAnim.cancel();
            slideDownAnim.cancel();
            pos = Position.DOWN;
        }
    }

    public void slideUp(final float initialPosition, final float endPosition) {
        if (pos == Position.DOWN) {
            slideUpAnim.removeAllListeners();
            slideUpAnim.setFloatValues(initialPosition, endPosition);
            slideUpAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(final Animator animation) {
                    pos = Position.UP;
                }
            });
            slideUpAnim.start();
        } else {
            slideUpAnim.cancel();
            slideDownAnim.cancel();
            pos = Position.UP;
        }
    }
}
