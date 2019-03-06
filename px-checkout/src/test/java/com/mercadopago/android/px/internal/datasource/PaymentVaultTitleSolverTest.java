package com.mercadopago.android.px.internal.datasource;


import com.mercadopago.android.px.R;
import com.mercadopago.android.px.configuration.CustomStringConfiguration;


import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PaymentVaultTitleSolverTest {

    private static final String CUSTOM_TITLE = "CUSTOM_TITLE";

    private PaymentVaultTitleSolverImpl paymentVaultTitleSolver;

    @Mock private Context context;
    @Mock private CustomStringConfiguration stringConfiguration;

    private int mainVerbResourceId;

    @Before
    public void setUp() {
        mainVerbResourceId = R.string.px_main_verb;
        paymentVaultTitleSolver = new PaymentVaultTitleSolverImpl(context, stringConfiguration);
        when(stringConfiguration.getCustomPaymentVaultTitle()).thenReturn(CUSTOM_TITLE);
        when(stringConfiguration.getMainVerbStringResourceId()).thenReturn(mainVerbResourceId);
    }

    @Test
    public void ifStringConfigurationHasCustomPaymentVaultTitleCustomTileShouldBeReturned(){
        when(stringConfiguration.hasCustomPaymentVaultTitle()).thenReturn(true);

        String title = paymentVaultTitleSolver.solveTitle();

        assertEquals(CUSTOM_TITLE,title);
    }

    @Test
    public void ifStringConfigurationHasNotCustomPaymentVaultTitleAndMainTitleShouldBeReturned(){
        when(stringConfiguration.hasCustomPaymentVaultTitle()).thenReturn(false);

        String title = paymentVaultTitleSolver.solveTitle();

        verify(stringConfiguration, never()).getCustomPaymentVaultTitle();
        assertEquals(context.getString(R.string.px_title_activity_payment_vault, mainVerbResourceId),title);
    }
}
