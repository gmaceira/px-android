package com.mercadopago.android.px.tracking.internal.views;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.repository.DiscountRepository;
import com.mercadopago.android.px.internal.util.JsonUtil;
import com.mercadopago.android.px.model.DiscountConfigurationModel;
import com.mercadopago.android.px.preferences.CheckoutPreference;
import com.mercadopago.android.px.tracking.PXTracker;
import com.mercadopago.android.px.tracking.PXTrackingListener;
import com.mercadopago.android.px.tracking.internal.model.DiscountInfo;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OneTapViewTest {

    private static final String EXPECTED_PATH = "/px_checkout/review/one_tap";

    @Mock
    private DiscountConfigurationModel discountModel;

    @Mock private CheckoutPreference checkoutPreference;

    @Test
    public void verifyPath() {
        assertEquals(EXPECTED_PATH,
            new OneTapViewTracker(Collections.EMPTY_LIST, checkoutPreference, discountModel).getViewPath());
    }

    @Test
    public void verifyListenerCalled() {
        final PXTrackingListener listener = mock(PXTrackingListener.class);
        PXTracker.setListener(listener);
        final Map<String, Object> data = emptyOneTapData();
        new OneTapViewTracker(Collections.EMPTY_LIST, checkoutPreference, discountModel).track();
        verify(listener).onView(EXPECTED_PATH, data);
    }

    @NonNull
    private Map<String, Object> emptyOneTapData() {
        final Map<String, Object> data = new HashMap<>();
        data.put("discount", JsonUtil.getInstance().fromJson("{}", DiscountInfo.class).toMap());
        data.put("available_methods", Collections.EMPTY_LIST);
        data.put("items", Collections.EMPTY_LIST);
        data.put("flow", null);
        data.put("preference_amount", null);
        data.put("flow_detail", Collections.EMPTY_MAP);
        return data;
    }
}