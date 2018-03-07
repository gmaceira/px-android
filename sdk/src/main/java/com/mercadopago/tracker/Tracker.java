package com.mercadopago.tracker;

import android.content.Context;
import android.support.v4.util.Pair;

import com.mercadopago.BuildConfig;
import com.mercadopago.review_and_confirm.models.PaymentModel;
import com.mercadopago.tracking.model.ActionEvent;
import com.mercadopago.tracking.model.ScreenViewEvent;
import com.mercadopago.tracking.utils.TrackingUtil;

import java.util.ArrayList;
import java.util.List;


public class Tracker {

    private static void addMetadata(final ScreenViewEvent.Builder builder,
                                    final List<Pair<String, String>> metadataList) {
        if (metadataList == null) return;
        else {
            for (Pair<String, String> metadata : metadataList) {
                builder.addMetaData(metadata.first, metadata.second);
            }
        }
    }

    private static MPTrackingContext getTrackerContext(final String merchantPublicKey, final Context context) {
        return new MPTrackingContext.Builder(context, merchantPublicKey)
                .setCheckoutVersion(BuildConfig.VERSION_NAME)
                .build();
    }

    public static void trackScreen(final String screenId,
                                   final String screenName,
                                   final Context context,
                                   final String merchantPublicKey,
                                   final List<Pair<String, String>> metadata) {

        MPTrackingContext mpTrackingContext = getTrackerContext(merchantPublicKey, context);

        ScreenViewEvent.Builder builder = new ScreenViewEvent.Builder()
                .setFlowId(FlowHandler.getInstance().getFlowId())
                .setScreenId(screenId)
                .setScreenName(screenName);

        addMetadata(builder, metadata);
        ScreenViewEvent event = builder.build();
        mpTrackingContext.trackEvent(event);
    }

    public static void trackReviewAndConfirmScreen(final Context context,
                                                   final String merchantPublicKey,
                                                   final PaymentModel paymentModel) {

        List<Pair<String, String>> metadata = new ArrayList<>();
        metadata.add(new Pair<>(TrackingUtil.METADATA_SHIPPING_INFO, TrackingUtil.HAS_SHIPPING_DEFAULT_VALUE));
        metadata.add(new Pair<>(TrackingUtil.METADATA_PAYMENT_TYPE_ID, paymentModel.getPaymentType()));
        metadata.add(new Pair<>(TrackingUtil.METADATA_PAYMENT_METHOD_ID, paymentModel.paymentMethodId));
        metadata.add(new Pair<>(TrackingUtil.METADATA_ISSUER_ID, String.valueOf(paymentModel.issuerId)));

        trackScreen(TrackingUtil.SCREEN_ID_REVIEW_AND_CONFIRM,
                TrackingUtil.SCREEN_NAME_REVIEW_AND_CONFIRM,
                context, merchantPublicKey, metadata);

    }


    public static void trackCheckoutConfirm(final Context context, final String merchantPublicKey) {

        final MPTrackingContext mpTrackingContext = getTrackerContext(merchantPublicKey, context);

        final ActionEvent actionEvent = new ActionEvent.Builder()
                .setFlowId(FlowHandler.getInstance().getFlowId())
                .setAction(TrackingUtil.ACTION_CHECKOUT_CONFIRMED)
                .setScreenId(TrackingUtil.SCREEN_ID_REVIEW_AND_CONFIRM)
                .setScreenName(TrackingUtil.SCREEN_NAME_REVIEW_AND_CONFIRM)
                .build();

        mpTrackingContext.trackEvent(actionEvent);
    }
}
