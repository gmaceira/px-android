package com.mercadopago.android.px.internal.features.uicontrollers;

import com.mercadopago.android.px.configuration.AdvancedConfiguration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AmountRowControllerTest {

    AmountRowController amountRowController;

    @Mock private AdvancedConfiguration advancedConfiguration;
    @Mock private AmountRowController.AmountRowVisibilityBehaviour amountRow;

    @Before
    public void setUp() {
        amountRowController = new AmountRowController(amountRow, advancedConfiguration);
    }

    @Test
    public void ifAmountRowIsEnabledAmountRowShouldBeShowed(){
        when(advancedConfiguration.isAmountRowEnabled()).thenReturn(true);
        amountRowController.configure();
        verify(amountRow).showAmountRow();
    }

    @Test
    public void ifAmountRowIsNotEnabledAmountRowShouldBeHidden(){
        when(advancedConfiguration.isAmountRowEnabled()).thenReturn(false);
        amountRowController.configure();
        verify(amountRow).hideAmountRow();
    }

}