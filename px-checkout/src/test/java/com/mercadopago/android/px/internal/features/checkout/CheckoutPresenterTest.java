package com.mercadopago.android.px.internal.features.checkout;

import androidx.annotation.NonNull;
import com.mercadopago.android.px.configuration.PaymentConfiguration;
import com.mercadopago.android.px.core.SplitPaymentProcessor;
import com.mercadopago.android.px.internal.configuration.InternalConfiguration;
import com.mercadopago.android.px.internal.features.Constants;
import com.mercadopago.android.px.internal.repository.CongratsRepository;
import com.mercadopago.android.px.internal.repository.ExperimentsRepository;
import com.mercadopago.android.px.internal.repository.InitRepository;
import com.mercadopago.android.px.internal.repository.PaymentRepository;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.repository.PluginRepository;
import com.mercadopago.android.px.internal.repository.UserSelectionRepository;
import com.mercadopago.android.px.internal.viewmodel.CheckoutStateModel;
import com.mercadopago.android.px.mocks.ApiExceptionStubs;
import com.mercadopago.android.px.mocks.InitResponseStub;
import com.mercadopago.android.px.model.BusinessPayment;
import com.mercadopago.android.px.model.Card;
import com.mercadopago.android.px.model.Payment;
import com.mercadopago.android.px.model.PaymentMethod;
import com.mercadopago.android.px.model.PaymentMethodSearch;
import com.mercadopago.android.px.model.Setting;
import com.mercadopago.android.px.model.Site;
import com.mercadopago.android.px.model.Sites;
import com.mercadopago.android.px.model.exceptions.ApiException;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;
import com.mercadopago.android.px.model.internal.InitResponse;
import com.mercadopago.android.px.preferences.CheckoutPreference;
import com.mercadopago.android.px.preferences.PaymentPreference;
import com.mercadopago.android.px.utils.StubFailMpCall;
import com.mercadopago.android.px.utils.StubSuccessMpCall;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.mercadopago.android.px.utils.StubCheckoutPreferenceUtils.stubPreferenceOneItem;
import static com.mercadopago.android.px.utils.StubCheckoutPreferenceUtils.stubPreferenceOneItemAndPayer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutPresenterTest {

    private static final String DEFAULT_CARD_ID = "260077840";
    private static final String DEBIT_CARD_DEBCABAL = "debcabal";
    public static final int CUSTOM_RESULT_CODE = 1;

    @Mock private Checkout.View checkoutView;
    @Mock private PaymentSettingRepository paymentSettingRepository;
    @Mock private UserSelectionRepository userSelectionRepository;
    @Mock private InitRepository initRepository;
    @Mock private PluginRepository pluginRepository;
    @Mock private PaymentRepository paymentRepository;
    @Mock private CongratsRepository congratsRepository;
    @Mock private InternalConfiguration internalConfiguration;
    @Mock private ExperimentsRepository experimentsRepository;
    @Mock private PostPaymentUrlsMapper postPaymentUrlsMapper;

    private CheckoutPresenter presenter;
    private CheckoutStateModel checkoutStateModel;

    @Before
    public void setUp() {
        final Site site = mock(Site.class);
        when(site.getId()).thenReturn(Sites.ARGENTINA.getId());
        when(paymentSettingRepository.getSite()).thenReturn(site);
        when(paymentSettingRepository.getCheckoutPreference()).thenReturn(mock(CheckoutPreference.class));
        when(postPaymentUrlsMapper.map(any(PostPaymentUrlsMapper.Model.class)))
            .thenReturn(mock(PostPaymentUrlsMapper.Response.class));
        checkoutStateModel = new CheckoutStateModel();
        presenter = getPresenter(checkoutStateModel);
    }

    @Test
    public void whenCheckoutInitializedAndPaymentMethodSearchFailsThenShowError() {
        final ApiException apiException = mock(ApiException.class);
        when(initRepository.init()).thenReturn(new StubFailMpCall<>(apiException));

        presenter.initialize();

        verify(checkoutView).showProgress();
        verify(checkoutView).showError(any(MercadoPagoError.class));
        verifyNoMoreInteractions(checkoutView);
    }

    @Test
    public void whenChoHasCompletePrefSetDoNotCallRepositoryToGetPreference() {
        final CheckoutPreference preference = stubPreferenceOneItem();
        when(paymentSettingRepository.getCheckoutPreference()).thenReturn(preference);
        when(initRepository.init()).thenReturn(new StubSuccessMpCall<>(InitResponseStub.FULL.get()));

        presenter.initialize();

        verifyInitializeWithPreference();
        verifyNoMoreInteractions(checkoutView);
    }

    @Test
    public void whenChoHasPreferenceAndPaymentMethodRetrievedShowPaymentMethodSelection() {
        final CheckoutPreference preference = stubPreferenceOneItemAndPayer();
        when(paymentSettingRepository.getCheckoutPreference()).thenReturn(preference);
        when(initRepository.init()).thenReturn(new StubSuccessMpCall<>(InitResponseStub.FULL.get()));

        presenter.initialize();

        verify(initRepository).init();
        verifyInitializeWithPreference();
        verifyNoMoreInteractions(checkoutView);
        verifyNoMoreInteractions(initRepository);
    }

    @Test
    public void whenAPaymentMethodIsSelectedThenShowReviewAndConfirmIfPaymentProcessorShouldNotSkipUserConfirmation() {
        final PaymentConfiguration paymentConfiguration = mock(PaymentConfiguration.class);
        final SplitPaymentProcessor paymentProcessor = mock(SplitPaymentProcessor.class);

        when(paymentSettingRepository.getPaymentConfiguration()).thenReturn(paymentConfiguration);
        when(paymentConfiguration.getPaymentProcessor()).thenReturn(paymentProcessor);
        when(paymentProcessor.shouldSkipUserConfirmation()).thenReturn(false);

        presenter.onPaymentMethodSelected();
        verify(checkoutView).showReviewAndConfirm(false);
        verifyNoMoreInteractions(checkoutView);
    }

    @Test
    public void whenAPaymentMethodIsSelectedThenShowVisualPaymentProcessorIfItShouldSkipShowUserConfirmation() {
        final PaymentConfiguration paymentConfiguration = mock(PaymentConfiguration.class);
        final SplitPaymentProcessor paymentProcessor = mock(SplitPaymentProcessor.class);

        when(paymentSettingRepository.getPaymentConfiguration()).thenReturn(paymentConfiguration);
        when(paymentConfiguration.getPaymentProcessor()).thenReturn(paymentProcessor);
        when(paymentProcessor.shouldSkipUserConfirmation()).thenReturn(true);

        presenter.onPaymentMethodSelected();
        verify(checkoutView).showPaymentProcessorWithAnimation();
        verify(checkoutView, never()).showReviewAndConfirm(anyBoolean());
        verifyNoMoreInteractions(checkoutView);
    }

    @Test
    public void whenDefaultCardIdValidSelectedThenShowSecurityCode() {
        final InitResponse search = mockPaymentMethodSearchForDriver(true);
        presenter.startFlow(search);
        verify(checkoutView).showSavedCardFlow(any(Card.class));
    }

    @Test
    public void whenDefaultCardIdInvalidSelectedThenShowPaymentVault() {
        final InitResponse search = mockPaymentMethodSearchForDriver(false);
        presenter.startFlow(search);
        verify(checkoutView).showPaymentMethodSelection();
    }

    @Test
    public void whenDefaultCardIdIsNullAndDefaultPaymentTypeIsValidThenShowNewCardFlow() {
        final InitResponse search = mockPaymentMethodSearchForNewCardDriver();
        presenter.startFlow(search);
        verify(checkoutView).showNewCardFlow();
    }

    @Test
    public void whenCardFlowCanceledAndThereIsValidCardThenCancelCheckout() {
        final InitResponse paymentMethodSearch = mockPaymentMethodSearchForDriver(true);
        when(initRepository.init()).thenReturn(new StubSuccessMpCall<>(paymentMethodSearch));

        presenter.onCardFlowCancel();

        verify(checkoutView).cancelCheckout();
        verifyNoMoreInteractions(checkoutView);
    }

    @Test
    public void whenCardFlowCancelAndDefaultCardIdIsNullAndDefaultPaymentTypeIsValidThenCancelCheckout() {
        final InitResponse paymentMethodSearch = mockPaymentMethodSearchForNewCardDriver();
        when(initRepository.init()).thenReturn(new StubSuccessMpCall<>(paymentMethodSearch));

        presenter.onCardFlowCancel();

        verify(checkoutView).cancelCheckout();
        verifyNoMoreInteractions(checkoutView);
    }

    @Test
    public void whenCardFlowCanceledAndThereIsInValidCardThenShowPaymentMethodSelection() {
        final InitResponse paymentMethodSearch = mockPaymentMethodSearchForDriver(false);
        when(initRepository.init()).thenReturn(new StubSuccessMpCall<>(paymentMethodSearch));

        presenter.onCardFlowCancel();

        verify(checkoutView).showPaymentMethodSelection();
        verifyNoMoreInteractions(checkoutView);
    }

    @Test
    public void whenCardFlowCanceledAndPaymentMethodSearchFailsThenShowPaymentMethodSelection() {
        final ApiException apiException = mock(ApiException.class);
        when(initRepository.init()).thenReturn(new StubFailMpCall<>(apiException));

        presenter.onCardFlowCancel();

        verify(checkoutView).showPaymentMethodSelection();
        verifyNoMoreInteractions(checkoutView);
    }

    @Test
    public void whenPaymentMethodCanceledThenCancelCheckout() {
        presenter.onPaymentMethodSelectionCancel();
        verify(checkoutView).cancelCheckout();
    }

    @Test
    public void whenPaymentResultWithCreatedPaymentThenFinishCheckoutWithPaymentResult() {
        final Payment payment = mock(Payment.class);
        when(paymentRepository.getPayment()).thenReturn(payment);

        presenter.onPaymentResultResponse(null, null, null);

        verify(checkoutView).finishWithPaymentResult(null, payment);
    }

    @Test
    public void whenPaymentResultWithoutCreatedPaymentThenFinishCheckoutWithoutPaymentResult() {
        presenter.onPaymentResultResponse(null, null, null);
        verify(checkoutView).finishWithPaymentResult(null, null);
    }

    @Test
    public void whenPaymentIsCanceledBecauseUserWantsToSelectOtherPaymentMethodThenShowPaymentMethodSelection() {
        presenter.onChangePaymentMethod();

        verify(checkoutView).transitionOut();
        verify(checkoutView).showPaymentMethodSelection();
        verifyNoMoreInteractions(checkoutView);
    }

    @Test
    public void whenUserSelectChangePaymentMethodFromPaymentResultAndExitOnIsTrueThenNotShowPaymentMethodSelection() {
        when(paymentRepository.getPayment()).thenReturn(mock(Payment.class));
        when(internalConfiguration.shouldExitOnPaymentMethodChange()).thenReturn(true);

        presenter.onChangePaymentMethod();

        verify(checkoutView).transitionOut();
        verify(checkoutView)
            .finishWithPaymentResult(Constants.RESULT_CHANGE_PAYMENT_METHOD, (Payment) paymentRepository.getPayment());
    }

    @Test
    public void whenUserSelectChangePaymentMethodWithoutPaymentAndExitOnIsTrueThenFinishWithPaymentResultChangePaymentMethodCode() {
        when(internalConfiguration.shouldExitOnPaymentMethodChange()).thenReturn(true);

        presenter.onChangePaymentMethod();

        verify(checkoutView).transitionOut();
        verify(checkoutView).finishWithPaymentResult(Constants.RESULT_CHANGE_PAYMENT_METHOD, null);
    }

    @Test
    public void whenCardFlowResponseHasNotRecoverableTokenProcessAndThereIsNoAvailableHooksThenShowReviewAndConfirmIfPaymentProcessorShouldNotSkipUserConfirmation() {
        final PaymentConfiguration paymentConfiguration = mock(PaymentConfiguration.class);
        final SplitPaymentProcessor paymentProcessor = mock(SplitPaymentProcessor.class);

        when(paymentSettingRepository.getPaymentConfiguration()).thenReturn(paymentConfiguration);
        when(paymentConfiguration.getPaymentProcessor()).thenReturn(paymentProcessor);
        when(paymentProcessor.shouldSkipUserConfirmation()).thenReturn(false);

        presenter.onCardFlowResponse();

        verify(paymentRepository).hasRecoverablePayment();
        verify(checkoutView).showReviewAndConfirm(false);
        verifyNoMoreInteractions(checkoutView);
        verifyNoMoreInteractions(paymentRepository);
    }

    @Test
    public void whenCardFlowResponseHasNotRecoverableTokenProcessAndThereIsNoAvailableHooksThenShowVisualPaymentProcessorIfItShouldSkipUserConfirmation() {
        final PaymentConfiguration paymentConfiguration = mock(PaymentConfiguration.class);
        final SplitPaymentProcessor paymentProcessor = mock(SplitPaymentProcessor.class);

        when(paymentSettingRepository.getPaymentConfiguration()).thenReturn(paymentConfiguration);
        when(paymentConfiguration.getPaymentProcessor()).thenReturn(paymentProcessor);
        when(paymentProcessor.shouldSkipUserConfirmation()).thenReturn(true);

        presenter.onCardFlowResponse();

        verify(paymentRepository).hasRecoverablePayment();
        verify(checkoutView).showPaymentProcessorWithAnimation();
        verify(checkoutView, never()).showReviewAndConfirm(anyBoolean());
        verifyNoMoreInteractions(checkoutView);
        verifyNoMoreInteractions(paymentRepository);
    }

    //Backs
    @Test
    public void whenCheckoutisInitializedAndUserPressesBackThenCancelCheckout() {
        when(paymentSettingRepository.getCheckoutPreference()).thenReturn(stubPreferenceOneItem());
        when(initRepository.init()).thenReturn(new StubSuccessMpCall<>(InitResponseStub.FULL.get()));

        presenter.initialize();

        verify(checkoutView).showPaymentMethodSelection();

        presenter.onPaymentMethodSelectionCancel();

        verify(checkoutView).cancelCheckout();
    }

    @Test
    public void whenPaymentMethodEditionIsRequestedAndUserPressesBackThenCancelCheckout() {
        final PaymentConfiguration paymentConfiguration = mock(PaymentConfiguration.class);
        final SplitPaymentProcessor paymentProcessor = mock(SplitPaymentProcessor.class);

        when(paymentSettingRepository.getCheckoutPreference()).thenReturn(stubPreferenceOneItem());
        when(initRepository.init()).thenReturn(new StubSuccessMpCall<>(InitResponseStub.FULL.get()));
        when(paymentSettingRepository.getPaymentConfiguration()).thenReturn(paymentConfiguration);
        when(paymentConfiguration.getPaymentProcessor()).thenReturn(paymentProcessor);
        when(paymentProcessor.shouldSkipUserConfirmation()).thenReturn(false);

        presenter.initialize();

        verify(checkoutView).showPaymentMethodSelection();

        presenter.onPaymentMethodSelected();
        verify(checkoutView).showReviewAndConfirm(anyBoolean());

        presenter.onChangePaymentMethod();
        verify(checkoutView, atLeastOnce()).showPaymentMethodSelection();

        presenter.onPaymentMethodSelectionCancel();
        verify(checkoutView).cancelCheckout();
    }

    @Test
    public void whenReviewAndConfirmCanceledAndOnlyOnePaymentMethodThenCancelCheckout() {
        when(paymentSettingRepository.getCheckoutPreference()).thenReturn(stubPreferenceOneItem());
        when(initRepository.init()).thenReturn(new StubSuccessMpCall<>(InitResponseStub.ONLY_ACCOUNT_MONEY_MLA.get()));

        presenter.initialize();
        presenter.onReviewAndConfirmCancel();

        verify(checkoutView).cancelCheckout();
    }

    @Test
    public void whenReviewAndConfirmCanceledAndThereIsMoreThanOnePaymentMethodThenShowPaymentMethodSelection() {
        when(paymentSettingRepository.getCheckoutPreference()).thenReturn(stubPreferenceOneItem());
        when(initRepository.init()).thenReturn(new StubSuccessMpCall<>(InitResponseStub.FULL.get()));

        presenter.initialize();
        presenter.onReviewAndConfirmCancel();

        verifyInitializeWithPreference();
        verify(checkoutView, atLeastOnce()).showPaymentMethodSelection();
        verify(checkoutView).transitionOut();
        verifyNoMoreInteractions(checkoutView);
    }

    @Test
    public void whenPaymentSelectionErrorAndPaymentMethodChangeRequestedFromReviewAndConfirmOnBackExitCheckout() {
        final MercadoPagoError mercadoPagoError = mock(MercadoPagoError.class);

        presenter.onChangePaymentMethod();
        presenter.onPaymentMethodSelectionError(mercadoPagoError);

        verify(checkoutView).transitionOut();
        verify(checkoutView).showPaymentMethodSelection();
        verify(checkoutView).cancelCheckout(mercadoPagoError);

        verifyNoMoreInteractions(checkoutView);
    }

    @Test
    public void whenPaymentSelectionErrorAndPaymentMethodChangeNotRequestedFromReviewAndConfirmThenCancelCheckout() {
        final MercadoPagoError mercadoPagoError = mock(MercadoPagoError.class);

        presenter.onPaymentMethodSelectionError(mercadoPagoError);

        verify(checkoutView).cancelCheckout(mercadoPagoError);

        verifyNoMoreInteractions(checkoutView);
    }

    @Test
    public void whenErrorShownAndInvalidIdentificationThenGoBackToPaymentMethodSelection() {
        final ApiException apiException = ApiExceptionStubs.INVALID_IDENTIFICATION_PAYMENT.get();
        final MercadoPagoError mpException = new MercadoPagoError(apiException, "");

        presenter.onErrorCancel(mpException);

        verify(checkoutView).showPaymentMethodSelection();
        verifyNoMoreInteractions(checkoutView);
    }

    @Test
    public void whenErrorShownAndValidIdentificationThenCancelCheckout() {
        final ApiException apiException = mock(ApiException.class);
        final MercadoPagoError mpException = new MercadoPagoError(apiException, "");

        presenter.onErrorCancel(mpException);

        verify(checkoutView).cancelCheckout();
        verifyNoMoreInteractions(checkoutView);
    }

    @Test
    public void whenNoDefaultPaymentMethodsAndIsExpressCheckoutThenShowExpressCheckout() {
        final PaymentMethodSearch search = mock(PaymentMethodSearch.class);
        when(search.hasExpressCheckoutMetadata()).thenReturn(true);

        presenter.noDefaultPaymentMethods(search);

        verifyShowOneTap();
        verifyNoMoreInteractions(checkoutView);
    }

    @Test
    public void whenCancelExpressCheckoutThenHideProgress() {
        final PaymentMethodSearch search = mock(PaymentMethodSearch.class);
        final CheckoutPreference checkoutPreference = mock(CheckoutPreference.class);

        when(paymentSettingRepository.getCheckoutPreference()).thenReturn(checkoutPreference);
        when(checkoutPreference.getPaymentPreference()).thenReturn(null);
        when(search.hasExpressCheckoutMetadata()).thenReturn(true);

        presenter.startFlow(search);
        presenter.cancelCheckout();

        verifyShowOneTap();
        verify(checkoutView, atLeastOnce()).hideProgress();
        verifyNoMoreInteractions(checkoutView);
    }

    @Test
    public void whenCancelRegularCheckoutThenCancelCheckout() {
        presenter.cancelCheckout();

        verify(checkoutView).cancelCheckout();
        verifyNoMoreInteractions(checkoutView);
    }

    @Test
    public void whenTerminalErrorThenCancelCheckout() {
        final MercadoPagoError mercadoPagoError = mock(MercadoPagoError.class);
        presenter.onTerminalError(mercadoPagoError);
        verify(checkoutView).cancelCheckout(mercadoPagoError);
        verifyNoMoreInteractions(checkoutView);
    }

    @Test
    public void whenReviewAndConfirmErrorThenCancelCheckout() {
        final MercadoPagoError mercadoPagoError = mock(MercadoPagoError.class);
        presenter.onReviewAndConfirmError(mercadoPagoError);
        verify(checkoutView).cancelCheckout(mercadoPagoError);
        verifyNoMoreInteractions(checkoutView);
    }

    @Test
    public void whenCustomReviewAndConfirmResponseThenCancelCheckout() {
        presenter.onCustomReviewAndConfirmResponse(CUSTOM_RESULT_CODE);
        verify(checkoutView).cancelCheckout(eq(CUSTOM_RESULT_CODE), anyBoolean());
        verifyNoMoreInteractions(checkoutView);
    }

    @Test
    public void whenCustomPaymentResultResponseHasPaymentThenFinishWithPaymentResult() {
        final Payment payment = mock(Payment.class);
        when(paymentRepository.getPayment()).thenReturn(payment);

        presenter.onPaymentResultResponse(CUSTOM_RESULT_CODE, null, null);

        verify(checkoutView).finishWithPaymentResult(CUSTOM_RESULT_CODE, payment);
        verifyNoMoreInteractions(checkoutView);
    }

    @Test
    public void whenCustomPaymentResultResponseHasNotPaymentThenFinishWithPaymentResult() {
        presenter.onPaymentResultResponse(CUSTOM_RESULT_CODE, null, null);

        verify(checkoutView).finishWithPaymentResult(CUSTOM_RESULT_CODE, null);
        verifyNoMoreInteractions(checkoutView);
    }

    @Test
    public void whenCustomPaymentResultResponseHasBusinessPaymentThenFinishWithPaymentResult() {
        final BusinessPayment payment = mock(BusinessPayment.class);
        when(paymentRepository.getPayment()).thenReturn(payment);

        presenter.onPaymentResultResponse(CUSTOM_RESULT_CODE, null, null);

        verify(checkoutView).finishWithPaymentResult(CUSTOM_RESULT_CODE, null);
        verifyNoMoreInteractions(checkoutView);
    }

// --------- Helper methods ----------- //

    @NonNull
    private CheckoutPresenter getBasePresenter(
        final Checkout.View view, final CheckoutStateModel checkoutStateModel) {

        presenter = new CheckoutPresenter(checkoutStateModel, paymentSettingRepository, userSelectionRepository,
            initRepository, pluginRepository, paymentRepository, congratsRepository, internalConfiguration,
            experimentsRepository, postPaymentUrlsMapper);

        presenter.attachView(view);
        return presenter;
    }

    @NonNull
    private CheckoutPresenter getPresenter(final CheckoutStateModel checkoutStateModel) {
        return getBasePresenter(checkoutView, checkoutStateModel);
    }

    private void verifyInitializeWithPreference() {
        verify(checkoutView).showProgress();
        verify(checkoutView, atLeastOnce()).showPaymentMethodSelection();
    }

    @NonNull
    private InitResponse mockPaymentMethodSearchForDriver(boolean isValidCard) {
        final InitResponse search = mock(InitResponse.class);
        final PaymentMethod paymentMethod = mock(PaymentMethod.class);
        when(paymentMethod.getPaymentTypeId()).thenReturn("debit_card");
        final ArrayList settingsList = mock(ArrayList.class);
        final Setting setting = mock(Setting.class);
        when(setting.getSecurityCode()).thenReturn(null);
        when(settingsList.get(any(int.class))).thenReturn(setting);
        when(paymentMethod.getSettings()).thenReturn(settingsList);
        when(search.getPaymentMethodById(any(String.class))).thenReturn(paymentMethod);
        if (isValidCard) {
            when(search.getCardById(any(String.class))).thenReturn(new Card());
        } else {
            when(search.getCardById(any(String.class))).thenReturn(null);
        }
        final CheckoutPreference checkoutPreference = mock(CheckoutPreference.class);
        when(paymentSettingRepository.getCheckoutPreference()).thenReturn(checkoutPreference);
        final PaymentPreference paymentPreference = mock(PaymentPreference.class);
        when(checkoutPreference.getPaymentPreference()).thenReturn(paymentPreference);
        when(paymentSettingRepository.getCheckoutPreference().getPaymentPreference().getDefaultCardId()).thenReturn(
            DEFAULT_CARD_ID);
        when(paymentSettingRepository.getCheckoutPreference().getPaymentPreference().getDefaultPaymentMethodId())
            .thenReturn(DEBIT_CARD_DEBCABAL);
        return search;
    }

    @NonNull
    private InitResponse mockPaymentMethodSearchForNewCardDriver() {
        final InitResponse search = mock(InitResponse.class);
        final CheckoutPreference checkoutPreference = mock(CheckoutPreference.class);
        when(checkoutPreference.getPaymentPreference()).thenReturn(mock(PaymentPreference.class));
        when(checkoutPreference.getPaymentPreference().getDefaultCardId()).thenReturn(null);
        when(checkoutPreference.getPaymentPreference().getDefaultPaymentTypeId()).thenReturn("debit_card");
        when(paymentSettingRepository.getCheckoutPreference()).thenReturn(checkoutPreference);
        return search;
    }

    private void verifyShowOneTap() {
        verify(checkoutView, atLeastOnce()).hideProgress();
        verify(checkoutView).showOneTap(any());
    }
}
