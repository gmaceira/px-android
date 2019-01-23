package com.mercadopago.android.px.guessing;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.configuration.AdvancedConfiguration;
import com.mercadopago.android.px.internal.controllers.PaymentMethodGuessingController;
import com.mercadopago.android.px.internal.features.guessing_card.GuessingCardActivityView;
import com.mercadopago.android.px.internal.features.guessing_card.GuessingCardPaymentPresenter;
import com.mercadopago.android.px.internal.features.uicontrollers.card.CardView;
import com.mercadopago.android.px.internal.repository.BankDealsRepository;
import com.mercadopago.android.px.internal.repository.CardTokenRepository;
import com.mercadopago.android.px.internal.repository.GroupsRepository;
import com.mercadopago.android.px.internal.repository.IdentificationRepository;
import com.mercadopago.android.px.internal.repository.IssuersRepository;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.repository.UserSelectionRepository;
import com.mercadopago.android.px.mocks.BankDeals;
import com.mercadopago.android.px.mocks.Cards;
import com.mercadopago.android.px.mocks.DummyCard;
import com.mercadopago.android.px.mocks.IdentificationTypes;
import com.mercadopago.android.px.mocks.Issuers;
import com.mercadopago.android.px.mocks.PaymentMethods;
import com.mercadopago.android.px.mocks.Tokens;
import com.mercadopago.android.px.model.BankDeal;
import com.mercadopago.android.px.model.Card;
import com.mercadopago.android.px.model.CardInfo;
import com.mercadopago.android.px.model.Cardholder;
import com.mercadopago.android.px.model.Identification;
import com.mercadopago.android.px.model.IdentificationType;
import com.mercadopago.android.px.model.Issuer;
import com.mercadopago.android.px.model.Payment;
import com.mercadopago.android.px.model.PaymentMethod;
import com.mercadopago.android.px.model.PaymentMethodSearch;
import com.mercadopago.android.px.model.PaymentRecovery;
import com.mercadopago.android.px.model.PaymentType;
import com.mercadopago.android.px.model.PaymentTypes;
import com.mercadopago.android.px.model.Site;
import com.mercadopago.android.px.model.Token;
import com.mercadopago.android.px.model.exceptions.ApiException;
import com.mercadopago.android.px.model.exceptions.CardTokenException;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;
import com.mercadopago.android.px.preferences.CheckoutPreference;
import com.mercadopago.android.px.preferences.PaymentPreference;
import com.mercadopago.android.px.utils.CardTestUtils;
import com.mercadopago.android.px.utils.StubSuccessMpCall;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.mercadopago.android.px.internal.util.ApiUtil.RequestOrigin.GET_IDENTIFICATION_TYPES;
import static com.mercadopago.android.px.internal.util.ApiUtil.RequestOrigin.GET_PAYMENT_METHODS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("PMD.ExcessiveClassLength")
@RunWith(MockitoJUnitRunner.class)
public class GuessingCardPaymentPresenterTest {

    private final MockedView mockedView = new MockedView();
    private GuessingCardPaymentPresenter presenter;

    @Mock private UserSelectionRepository userSelectionRepository;
    @Mock private GroupsRepository groupsRepository;
    @Mock private IssuersRepository issuersRepository;
    @Mock private PaymentSettingRepository paymentSettingRepository;
    @Mock private CardTokenRepository cardTokenRepository;
    @Mock private BankDealsRepository bankDealsRepository;
    @Mock private IdentificationRepository identificationRepository;
    
    @Mock private CheckoutPreference checkoutPreference;
    @Mock private PaymentPreference paymentPreference;
    
    @Mock private PaymentMethodSearch paymentMethodSearch;
    @Mock private AdvancedConfiguration advancedConfiguration;
    @Mock private Site site;

    @Mock private GuessingCardActivityView view;

    @Before
    public void setUp() {
        // No charge initialization.
//        when(paymentSettingRepository.getCheckoutPreference()).thenReturn(checkoutPreference);
//        when(checkoutPreference.getSite()).thenReturn(site);
//        when(checkoutPreference.getPaymentPreference()).thenReturn(paymentPreference);
        final List<PaymentMethod> pm = PaymentMethods.getPaymentMethodListMLA();
//        when(groupsRepository.getGroups()).thenReturn(new StubSuccessMpCall<>(paymentMethodSearch));
//        when(paymentMethodSearch.getPaymentMethods()).thenReturn(pm);
//        when(advancedConfiguration.isBankDealsEnabled()).thenReturn(true);
        presenter =
            new GuessingCardPaymentPresenter(userSelectionRepository, paymentSettingRepository,
                groupsRepository, issuersRepository, cardTokenRepository, bankDealsRepository,
                identificationRepository, advancedConfiguration,
                new PaymentRecovery(Payment.StatusDetail.STATUS_DETAIL_CC_REJECTED_CALL_FOR_AUTHORIZE)
            );
        presenter.attachView(mockedView);
    }

    @Ignore
    @Test
    public void ifPublicKeySetThenCheckValidStart() {
        presenter.initialize();
        assertTrue(mockedView.validStart);
    }

    @Ignore
    @Test
    public void ifPaymentRecoverySetThenSaveCardholderNameAndIdentification() {

        final Cardholder cardHolder = mock(Cardholder.class);
        final Token token = mock(Token.class);
        when(paymentSettingRepository.getToken()).thenReturn(token);
        when(token.getCardHolder()).thenReturn(cardHolder);
        when(cardHolder.getIdentification()).thenReturn(mock(Identification.class));

        presenter
            .setPaymentRecovery(new PaymentRecovery(Payment.StatusDetail.STATUS_DETAIL_CC_REJECTED_CALL_FOR_AUTHORIZE));
        presenter.attachView(view);
        presenter.initialize();

        verify(view).setCardholderName(cardHolder.getName());
        verify(view).setIdentificationNumber(cardHolder.getIdentification().getNumber());
    }

    @Ignore
    @Test
    public void ifPaymentMethodListSetWithOnePaymentMethodThenSelectIt() {

        final List<IdentificationType> identificationTypesList = IdentificationTypes.getIdentificationTypes();
        //provider.setIdentificationTypesResponse(identificationTypesList);

        presenter.initialize();

        final List<PaymentMethod> mockedGuessedPaymentMethods = new ArrayList<>();
        mockedGuessedPaymentMethods.add(PaymentMethods.getPaymentMethodOnVisa());

        presenter.resolvePaymentMethodListSet(mockedGuessedPaymentMethods, Cards.MOCKED_BIN_VISA);

        assertTrue(mockedView.paymentMethodSet);
    }

    @Ignore
    @Test
    public void ifPaymentMethodListSetIsEmptyThenShowError() {

        final List<IdentificationType> identificationTypesList = IdentificationTypes.getIdentificationTypes();
        //provider.setIdentificationTypesResponse(identificationTypesList);

        presenter.initialize();

        final List<PaymentMethod> mockedGuessedPaymentMethods = new ArrayList<>();

        presenter.resolvePaymentMethodListSet(mockedGuessedPaymentMethods, Cards.MOCKED_BIN_VISA);

        assertFalse(mockedView.paymentMethodSet);
        assertTrue(mockedView.invalidPaymentMethod);
        assertTrue(mockedView.multipleErrorViewShown);
    }

    @Ignore
    @Test
    public void ifPaymentMethodListSetWithTwoOptionsThenAskForPaymentType() {

        final List<IdentificationType> identificationTypesList = IdentificationTypes.getIdentificationTypes();
       //provider.setIdentificationTypesResponse(identificationTypesList);

        presenter.initialize();

        final List<PaymentMethod> mockedGuessedPaymentMethods = new ArrayList<>();
        mockedGuessedPaymentMethods.add(PaymentMethods.getPaymentMethodOnVisa());
        mockedGuessedPaymentMethods.add(PaymentMethods.getPaymentMethodOnDebit());

        assertTrue(presenter.shouldAskPaymentType(mockedGuessedPaymentMethods));
    }

    @Ignore
    @Test
    public void ifPaymentMethodListSetWithTwoOptionsThenChooseFirstOne() {

        final List<IdentificationType> identificationTypesList = IdentificationTypes.getIdentificationTypes();
        //provider.setIdentificationTypesResponse(identificationTypesList);

        presenter.initialize();

        final List<PaymentMethod> mockedGuessedPaymentMethods = new ArrayList<>();
        final PaymentMethod paymentMethodOnVisa = PaymentMethods.getPaymentMethodOnVisa();
        mockedGuessedPaymentMethods.add(paymentMethodOnVisa);
        mockedGuessedPaymentMethods.add(PaymentMethods.getPaymentMethodOnDebit());

        presenter.resolvePaymentMethodListSet(mockedGuessedPaymentMethods, Cards.MOCKED_BIN_VISA);

        when(userSelectionRepository.getPaymentMethod()).thenReturn(paymentMethodOnVisa);

        assertTrue(mockedView.paymentMethodSet);
        assertNotNull(presenter.getPaymentMethod());
        assertEquals(presenter.getPaymentMethod().getId(), mockedGuessedPaymentMethods.get(0).getId());
    }

    @Ignore
    @Test
    public void ifPaymentMethodSetAndDeletedThenClearConfiguration() {

        final List<IdentificationType> identificationTypesList = IdentificationTypes.getIdentificationTypes();
        //provider.setIdentificationTypesResponse(identificationTypesList);

        presenter.initialize();

        final List<PaymentMethod> mockedGuessedPaymentMethods = new ArrayList<>();
        mockedGuessedPaymentMethods.add(PaymentMethods.getPaymentMethodOnVisa());

        presenter.resolvePaymentMethodListSet(mockedGuessedPaymentMethods, Cards.MOCKED_BIN_VISA);

        assertTrue(mockedView.paymentMethodSet);

        presenter.setPaymentMethod(null);

        assertEquals(Card.CARD_DEFAULT_SECURITY_CODE_LENGTH, presenter.getSecurityCodeLength());
        assertEquals(CardView.CARD_SIDE_BACK, presenter.getSecurityCodeLocation());
        assertTrue(presenter.isSecurityCodeRequired());
        assertEquals(0, presenter.getSavedBin().length());
    }

    @Ignore
    @Test
    public void ifPaymentMethodSetAndDeletedThenClearViews() {

        final List<IdentificationType> identificationTypesList = IdentificationTypes.getIdentificationTypes();
       // provider.setIdentificationTypesResponse(identificationTypesList);

        presenter.initialize();

        final List<PaymentMethod> mockedGuessedPaymentMethods = new ArrayList<>();
        mockedGuessedPaymentMethods.add(PaymentMethods.getPaymentMethodOnVisa());

        presenter.resolvePaymentMethodListSet(mockedGuessedPaymentMethods, Cards.MOCKED_BIN_VISA);
        assertTrue(mockedView.paymentMethodSet);

        presenter.resolvePaymentMethodCleared();

        assertFalse(mockedView.errorState);
        assertTrue(mockedView.cardNumberLengthDefault);
        assertTrue(mockedView.securityCodeInputErased);
        assertTrue(mockedView.clearCardView);
    }

    @Ignore
    @Test
    public void ifPaymentMethodSetHasIdentificationTypeRequiredThenShowIdentificationView() {

        final List<IdentificationType> identificationTypesList = IdentificationTypes.getIdentificationTypes();
        //provider.setIdentificationTypesResponse(identificationTypesList);

        presenter.initialize();

        final List<PaymentMethod> mockedGuessedPaymentMethods = new ArrayList<>();
        mockedGuessedPaymentMethods.add(PaymentMethods.getPaymentMethodOnVisa());

        presenter.resolvePaymentMethodListSet(mockedGuessedPaymentMethods, Cards.MOCKED_BIN_VISA);

        assertTrue(mockedView.paymentMethodSet);
        assertTrue(presenter.isIdentificationNumberRequired());
        assertTrue(mockedView.identificationTypesInitialized);
    }

    @Ignore
    @Test
    public void ifPaymentMethodSetDoNotHaveIdentificationTypeRequiredThenHideIdentificationView() {

        final List<IdentificationType> identificationTypesList = IdentificationTypes.getIdentificationTypes();
        //provider.setIdentificationTypesResponse(identificationTypesList);

        presenter.initialize();

        final List<PaymentMethod> mockedGuessedPaymentMethods = new ArrayList<>();
        mockedGuessedPaymentMethods.add(PaymentMethods.getPaymentMethodWithIdNotRequired());

        presenter.resolvePaymentMethodListSet(mockedGuessedPaymentMethods, Cards.MOCKED_BIN_CORDIAL);

        assertTrue(mockedView.paymentMethodSet);
        assertFalse(presenter.isIdentificationNumberRequired());
        assertFalse(mockedView.identificationTypesInitialized);
        assertTrue(mockedView.hideIdentificationInput);
    }

    @Ignore
    @Test
    public void initializeGuessingFormWithPaymentMethodListFromCardVault() {

        final List<IdentificationType> identificationTypesList = IdentificationTypes.getIdentificationTypes();
        //provider.setIdentificationTypesResponse(identificationTypesList);

        presenter.initialize();

        assertTrue(mockedView.showInputContainer);
        assertTrue(mockedView.initializeGuessingForm);
        assertTrue(mockedView.initializeGuessingListeners);
    }

    @Ignore
    @Test
    public void ifBankDealsNotEnabledThenHideBankDeals() {

        final List<IdentificationType> identificationTypesList = IdentificationTypes.getIdentificationTypes();
        //provider.setIdentificationTypesResponse(identificationTypesList);

        presenter.attachView(mockedView);

        presenter.initialize();

        assertTrue(mockedView.hideBankDeals);
    }

    @Ignore
    @Test
    public void ifGetPaymentMethodFailsThenShowErrorMessage() {

        final ApiException apiException = PaymentMethods.getDoNotFindPaymentMethodsException();
        final MercadoPagoError mpException = new MercadoPagoError(apiException, GET_PAYMENT_METHODS);
      //  provider.setPaymentMethodsResponse(mpException);

        presenter.attachView(mockedView);
        

        presenter.initialize();

       // assertEquals(MockedProvider.PAYMENT_METHODS_NOT_FOUND, provider.failedResponse.getApiException().getError());
    }

    @Ignore
    @Test
    public void ifPaymentTypeSetAndTwoPaymentMethodsThenChooseByPaymentType() {

        final List<PaymentMethod> paymentMethodList = PaymentMethods.getPaymentMethodListMLM();
        final List<IdentificationType> identificationTypesList = IdentificationTypes.getIdentificationTypes();
        //provider.setIdentificationTypesResponse(identificationTypesList);
        when(userSelectionRepository.getPaymentType()).thenReturn(PaymentTypes.DEBIT_CARD);

        presenter.initialize();

        final PaymentMethodGuessingController controller = new PaymentMethodGuessingController(
            paymentMethodList, PaymentTypes.DEBIT_CARD, null);

        final List<PaymentMethod> paymentMethodsWithExclusionsList =
            controller.guessPaymentMethodsByBin(Cards.MOCKED_BIN_MASTER);

        presenter.resolvePaymentMethodListSet(paymentMethodsWithExclusionsList, Cards.MOCKED_BIN_MASTER);

        when(userSelectionRepository.getPaymentMethod()).thenReturn(controller.getGuessedPaymentMethods().get(0));
        assertEquals(1, paymentMethodsWithExclusionsList.size());
        assertNotNull(presenter.getPaymentMethod());
        assertEquals("debmaster", presenter.getPaymentMethod().getId());
        assertFalse(presenter.shouldAskPaymentType(paymentMethodsWithExclusionsList));
    }

    @Ignore
    @Test
    public void ifSecurityCodeSettingsAreWrongThenHideSecurityCodeView() {

        final List<IdentificationType> identificationTypesList = IdentificationTypes.getIdentificationTypes();
       // provider.setIdentificationTypesResponse(identificationTypesList);

        presenter.attachView(mockedView);

        presenter.initialize();

        final List<PaymentMethod> mockedGuessedPaymentMethods = new ArrayList<>();
        mockedGuessedPaymentMethods.add(PaymentMethods.getPaymentMethodWithWrongSecurityCodeSettings());
        when(userSelectionRepository.getPaymentMethod()).thenReturn(null);
        presenter.resolvePaymentMethodListSet(mockedGuessedPaymentMethods, Cards.MOCKED_BIN_VISA);
        assertTrue(mockedView.paymentMethodSet);
        assertTrue(mockedView.hideSecurityCodeInput);
    }

    @Ignore
    @Test
    public void ifPaymentMethodSettingsAreEmptyThenShowErrorMessage() {

        final List<IdentificationType> identificationTypesList = IdentificationTypes.getIdentificationTypes();
       // provider.setIdentificationTypesResponse(identificationTypesList);

        presenter.attachView(mockedView);
       // presenter.attachResourcesProvider(provider);

        presenter.initialize();

        final List<PaymentMethod> mockedGuessedPaymentMethods = new ArrayList<>();
        final PaymentMethod mockedPaymentMethod = PaymentMethods.getPaymentMethodOnVisa();
        mockedPaymentMethod.setSettings(null);
        mockedGuessedPaymentMethods.add(mockedPaymentMethod);

        presenter.resolvePaymentMethodListSet(mockedGuessedPaymentMethods, Cards.MOCKED_BIN_VISA);

       // assertEquals(MockedProvider.SETTING_NOT_FOUND_FOR_BIN, mockedView.errorShown.getMessage());
    }

    @Ignore
    @Test
    public void ifGetIdentificationTypesFailsThenShowErrorMessage() {

        final ApiException apiException = IdentificationTypes.getDoNotFindIdentificationTypesException();
        final MercadoPagoError mpException =
            new MercadoPagoError(apiException, GET_IDENTIFICATION_TYPES);

        //provider.setIdentificationTypesResponse(mpException);

        presenter.initialize();
        //assertEquals(MockedProvider.IDENTIFICATION_TYPES_NOT_FOUND,
          //  provider.failedResponse.getApiException().getError());
    }

    @Ignore
    @Test
    public void ifGetIdentificationTypesIsEmptyThenShowErrorMessage() {

        final List<IdentificationType> identificationTypes = new ArrayList<>();
        //provider.setIdentificationTypesResponse(identificationTypes);

        when(userSelectionRepository.getPaymentMethod()).thenReturn(null);

        final List<PaymentMethod> paymentMethodList = PaymentMethods.getPaymentMethodListMLA();

        presenter.attachView(mockedView);
        //presenter.attachResourcesProvider(provider);
        presenter.initialize();
        presenter.resolvePaymentMethodListSet(paymentMethodList, Cards.MOCKED_BIN_VISA);

        //assertEquals(MockedProvider.MISSING_IDENTIFICATION_TYPES, mockedView.errorShown.getMessage());
    }

    @Ignore
    @Test
    public void ifBankDealsNotEmptyThenShowThem() {

        final List<IdentificationType> identificationTypes = IdentificationTypes.getIdentificationTypes();
        //provider.setIdentificationTypesResponse(identificationTypes);

        final List<BankDeal> bankDeals = BankDeals.getBankDealsListMLA();
        //provider.setBankDealsResponse(bankDeals);

        presenter.attachView(mockedView);

        presenter.initialize();

        final List<PaymentMethod> pm = PaymentMethods.getPaymentMethodListMLA();
        presenter.resolvePaymentMethodListSet(pm, Cards.MOCKED_BIN_VISA);

        assertTrue(mockedView.bankDealsShown);
    }

    @Ignore
    @Test
    public void ifCardNumberSetThenValidateItAndSaveItInCardToken() {

        final List<IdentificationType> identificationTypesList = IdentificationTypes.getIdentificationTypes();
        //provider.setIdentificationTypesResponse(identificationTypesList);

        final PaymentMethod mockedPaymentMethod = PaymentMethods.getPaymentMethodOnMaster();
        when(userSelectionRepository.getPaymentMethod()).thenReturn(mockedPaymentMethod);

        presenter.attachView(mockedView);

        presenter.initialize();

        final DummyCard card = CardTestUtils.getDummyCard("master");
        assertNotNull(card);
        presenter.saveCardNumber(card.getCardNumber());
        presenter.setPaymentMethod(mockedPaymentMethod);

        final boolean valid = presenter.validateCardNumber();

        assertTrue(valid);
        assertEquals(presenter.getCardToken().getCardNumber(), card.getCardNumber());
    }

    @Ignore
    @Test
    public void ifCardholderNameSetThenValidateItAndSaveItInCardToken() {

        final List<IdentificationType> identificationTypesList = IdentificationTypes.getIdentificationTypes();
        //provider.setIdentificationTypesResponse(identificationTypesList);

        final PaymentMethod mockedPaymentMethod = PaymentMethods.getPaymentMethodOnMaster();

        presenter.attachView(mockedView);

        presenter.initialize();

        final DummyCard card = CardTestUtils.getDummyCard("master");
        assertNotNull(card);
        presenter.saveCardNumber(card.getCardNumber());
        presenter.setPaymentMethod(mockedPaymentMethod);
        presenter.saveCardholderName(CardTestUtils.DUMMY_CARDHOLDER_NAME);

        assertTrue(presenter.validateCardName());
        assertEquals(CardTestUtils.DUMMY_CARDHOLDER_NAME, presenter.getCardToken().getCardholder().getName());
    }

    @Ignore
    @Test
    public void ifCardExpiryDateSetThenValidateItAndSaveItInCardToken() {

        final List<IdentificationType> identificationTypesList = IdentificationTypes.getIdentificationTypes();
        //provider.setIdentificationTypesResponse(identificationTypesList);

        final PaymentMethod mockedPaymentMethod = PaymentMethods.getPaymentMethodOnMaster();

        presenter.attachView(mockedView);

        presenter.initialize();

        final DummyCard card = CardTestUtils.getDummyCard("master");
        assertNotNull(card);
        presenter.saveCardNumber(card.getCardNumber());
        presenter.setPaymentMethod(mockedPaymentMethod);
        presenter.saveCardholderName(CardTestUtils.DUMMY_CARDHOLDER_NAME);
        presenter.saveExpiryMonth(CardTestUtils.DUMMY_EXPIRY_MONTH);
        presenter.saveExpiryYear(CardTestUtils.DUMMY_EXPIRY_YEAR_SHORT);

        assertTrue(presenter.validateExpiryDate());
        assertEquals(presenter.getCardToken().getExpirationMonth(), Integer.valueOf(CardTestUtils.DUMMY_EXPIRY_MONTH));
        assertEquals(presenter.getCardToken().getExpirationYear(),
            Integer.valueOf(CardTestUtils.DUMMY_EXPIRY_YEAR_LONG));
    }

    @Ignore
    @Test
    public void ifCardSecurityCodeSetThenValidateItAndSaveItInCardToken() {

        final List<IdentificationType> identificationTypesList = IdentificationTypes.getIdentificationTypes();
        //provider.setIdentificationTypesResponse(identificationTypesList);

        final PaymentMethod mockedPaymentMethod = PaymentMethods.getPaymentMethodOnMaster();

        when(userSelectionRepository.getPaymentMethod()).thenReturn(mockedPaymentMethod);

        presenter.attachView(mockedView);

        presenter.initialize();

        final DummyCard card = CardTestUtils.getDummyCard("master");
        assertNotNull(card);
        presenter.saveCardNumber(card.getCardNumber());
        presenter.setPaymentMethod(mockedPaymentMethod);
        presenter.saveCardholderName(CardTestUtils.DUMMY_CARDHOLDER_NAME);
        presenter.saveExpiryMonth(CardTestUtils.DUMMY_EXPIRY_MONTH);
        presenter.saveExpiryYear(CardTestUtils.DUMMY_EXPIRY_YEAR_SHORT);
        presenter.saveSecurityCode(card.getSecurityCode());
        final boolean validCardNumber = presenter.validateCardNumber();
        final boolean validSecurityCode = presenter.validateSecurityCode();

        assertTrue(validCardNumber && validSecurityCode);
        assertEquals(presenter.getCardToken().getSecurityCode(), card.getSecurityCode());
    }

    @Ignore
    @Test
    public void ifIdentificationNumberSetThenValidateItAndSaveItInCardToken() {

        final List<IdentificationType> identificationTypesList = IdentificationTypes.getIdentificationTypes();
        //provider.setIdentificationTypesResponse(identificationTypesList);

        final PaymentMethod mockedPaymentMethod = PaymentMethods.getPaymentMethodOnMaster();

        final Identification identification = new Identification();
        presenter.setIdentification(identification);

        presenter.attachView(mockedView);

        presenter.initialize();

        final DummyCard card = CardTestUtils.getDummyCard("master");
        assertNotNull(card);
        presenter.saveCardNumber(card.getCardNumber());
        presenter.setPaymentMethod(mockedPaymentMethod);
        presenter.saveCardholderName(CardTestUtils.DUMMY_CARDHOLDER_NAME);
        presenter.saveExpiryMonth(CardTestUtils.DUMMY_EXPIRY_MONTH);
        presenter.saveExpiryYear(CardTestUtils.DUMMY_EXPIRY_YEAR_SHORT);
        presenter.saveSecurityCode(card.getSecurityCode());
        presenter.saveIdentificationNumber(CardTestUtils.DUMMY_IDENTIFICATION_NUMBER_DNI);
        presenter.saveIdentificationType(IdentificationTypes.getIdentificationType());

        assertTrue(presenter.validateIdentificationNumber());
        assertEquals(CardTestUtils.DUMMY_IDENTIFICATION_NUMBER_DNI,
            presenter.getCardToken().getCardholder().getIdentification().getNumber());
    }

    @Ignore
    @Test
    public void ifCardDataSetAndValidThenCreateToken() {

        final List<IdentificationType> identificationTypesList = IdentificationTypes.getIdentificationTypes();
        //provider.setIdentificationTypesResponse(identificationTypesList);

        final List<Issuer> issuerList = Issuers.getIssuersListMLA();
        //provider.setIssuersResponse(issuerList);

        final PaymentMethod mockedPaymentMethod = PaymentMethods.getPaymentMethodOnMaster();

        final Token mockedToken = Tokens.getToken();
        //provider.setTokenResponse(mockedToken);

        final Identification identification = new Identification();
        presenter.setIdentification(identification);

        presenter.attachView(mockedView);
        when(userSelectionRepository.getPaymentMethod()).thenReturn(mockedPaymentMethod);

        presenter.initialize();

        final DummyCard card = CardTestUtils.getDummyCard("master");
        assertNotNull(card);
        presenter.saveCardNumber(card.getCardNumber());
        presenter.setPaymentMethod(mockedPaymentMethod);
        presenter.saveCardholderName(CardTestUtils.DUMMY_CARDHOLDER_NAME);
        presenter.saveExpiryMonth(CardTestUtils.DUMMY_EXPIRY_MONTH);
        presenter.saveExpiryYear(CardTestUtils.DUMMY_EXPIRY_YEAR_SHORT);
        presenter.saveSecurityCode(card.getSecurityCode());
        presenter.saveIdentificationNumber(CardTestUtils.DUMMY_IDENTIFICATION_NUMBER_DNI);
        presenter.saveIdentificationType(IdentificationTypes.getIdentificationType());

        final boolean valid =
            presenter.validateCardNumber() && presenter.validateCardName() && presenter.validateExpiryDate()
                && presenter.validateSecurityCode() && presenter.validateIdentificationNumber();

        when(issuersRepository.getIssuers(mockedPaymentMethod.getId(), presenter.getSavedBin()))
            .thenReturn(new StubSuccessMpCall<>(Issuers.getIssuersListMLA()));

        assertTrue(valid);
        presenter.checkFinishWithCardToken();
        presenter.resolveTokenRequest(mockedToken);
        assertEquals(presenter.getToken(), mockedToken);
    }

    //TODO check this test, should not happen and does nothing.
    @Ignore
    @Test
    public void ifPaymentMethodExclusionSetAndUserSelectsItThenShowErrorMessage() {

        final List<IdentificationType> identificationTypesList = IdentificationTypes.getIdentificationTypes();
        //provider.setIdentificationTypesResponse(identificationTypesList);

        presenter.initialize();

        //The user enters a master bin
        final PaymentMethodGuessingController controller = presenter.getGuessingController();
        final List<PaymentMethod> guessedPaymentMethods = controller.guessPaymentMethodsByBin(Cards.MOCKED_BIN_MASTER);

        presenter.resolvePaymentMethodListSet(guessedPaymentMethods, Cards.MOCKED_BIN_MASTER);

        //We show a red container showing the multiple available payment methods
        assertFalse(mockedView.paymentMethodSet);
        assertTrue(mockedView.invalidPaymentMethod);
        assertTrue(mockedView.multipleErrorViewShown);

        //The users deletes the bin master
        presenter.setPaymentMethod(null);
        presenter.resolvePaymentMethodCleared();

        //The red container disappears
        assertFalse(mockedView.multipleErrorViewShown);
        assertFalse(mockedView.invalidPaymentMethod);
    }

    @Ignore
    @Test
    public void ifPaymentMethodExclusionSetAndUserSelectsItWithOnlyOnePMAvailableThenShowInfoMessage() {

        //We only have visa and master
        final List<PaymentMethod> paymentMethodList = PaymentMethods.getPaymentMethodListWithTwoOptions();
        final PaymentMethodSearch paymentMethodSearch = mock(PaymentMethodSearch.class);
        when(groupsRepository.getGroups()).thenReturn(new StubSuccessMpCall<>(paymentMethodSearch));
        when(paymentMethodSearch.getPaymentMethods()).thenReturn(paymentMethodList);

        final List<IdentificationType> identificationTypesList = IdentificationTypes.getIdentificationTypes();
        //provider.setIdentificationTypesResponse(identificationTypesList);

        //We exclude master
        final Collection<String> excludedPaymentMethodIds = new ArrayList<>();
        excludedPaymentMethodIds.add("master");

        when(userSelectionRepository.getPaymentType()).thenReturn(PaymentTypes.CREDIT_CARD);

        when(paymentPreference.getSupportedPaymentMethods(paymentMethodSearch.getPaymentMethods()))
            .thenReturn(Collections.singletonList(paymentMethodList.get(0)));

        presenter = new GuessingCardPaymentPresenter(userSelectionRepository, paymentSettingRepository,
            groupsRepository, issuersRepository, cardTokenRepository, bankDealsRepository,
            identificationRepository, advancedConfiguration,
            new PaymentRecovery(Payment.StatusDetail.STATUS_DETAIL_CC_REJECTED_CALL_FOR_AUTHORIZE));

        presenter.attachView(mockedView);

        presenter.initialize();

        //Black info container shows the only available payment method
        assertTrue(mockedView.onlyOnePMErrorViewShown);

        assertEquals("visa", mockedView.supportedPaymentMethodId);

        final PaymentMethodGuessingController controller = presenter.getGuessingController();
        final List<PaymentMethod> guessedPaymentMethods = controller.guessPaymentMethodsByBin(Cards.MOCKED_BIN_MASTER);
        presenter.resolvePaymentMethodListSet(guessedPaymentMethods, Cards.MOCKED_BIN_MASTER);

        //When the user enters a master bin the container turns red
        assertFalse(mockedView.paymentMethodSet);
        assertTrue(mockedView.infoContainerTurnedRed);
        assertTrue(mockedView.invalidPaymentMethod);

        presenter.setPaymentMethod(null);
        presenter.resolvePaymentMethodCleared();

        //When the user deletes the input the container turns black again
        assertFalse(mockedView.infoContainerTurnedRed);
        assertTrue(mockedView.onlyOnePMErrorViewShown);
    }

    @Test
    public void whenAllGuessedPaymentMethodsShareTypeThenDoNotAskForPaymentType() {
        final PaymentMethod creditCard1 = new PaymentMethod();
        creditCard1.setPaymentTypeId(PaymentTypes.CREDIT_CARD);

        final PaymentMethod creditCard2 = new PaymentMethod();
        creditCard2.setPaymentTypeId(PaymentTypes.CREDIT_CARD);

        final List<PaymentMethod> paymentMethodList = new ArrayList<>();
        paymentMethodList.add(creditCard1);
        paymentMethodList.add(creditCard2);

        final boolean shouldAskPaymentType = presenter.shouldAskPaymentType(paymentMethodList);
        assertFalse(shouldAskPaymentType);
    }

    @Test
    public void whenNotAllGuessedPaymentMethodsShareTypeThenDoAskForPaymentType() {
        final PaymentMethod creditCard = new PaymentMethod();
        creditCard.setPaymentTypeId(PaymentTypes.CREDIT_CARD);

        final PaymentMethod debitCard = new PaymentMethod();
        debitCard.setPaymentTypeId(PaymentTypes.DEBIT_CARD);

        final List<PaymentMethod> paymentMethodList = new ArrayList<>();
        paymentMethodList.add(creditCard);
        paymentMethodList.add(debitCard);

        final boolean shouldAskPaymentType = presenter.shouldAskPaymentType(paymentMethodList);
        assertTrue(shouldAskPaymentType);
    }

    @Test
    public void whenUniquePaymentMethodGuessedThenPaymentMethodShouldDefined() {

        final PaymentMethod creditCard = new PaymentMethod();
        creditCard.setPaymentTypeId(PaymentTypes.CREDIT_CARD);

        final List<PaymentMethod> paymentMethodList = new ArrayList<>();
        paymentMethodList.add(creditCard);
        assertFalse(presenter.shouldAskPaymentType(paymentMethodList));
    }

    @SuppressWarnings("WeakerAccess")
    private static class MockedView implements GuessingCardActivityView {

        protected boolean validStart;
        protected boolean paymentMethodSet;
        protected boolean invalidPaymentMethod;
        protected boolean multipleErrorViewShown;
        protected String savedCardholderName;
        protected String savedIdentificationNumber;
        protected boolean errorState;
        protected boolean cardNumberLengthDefault;
        protected boolean securityCodeInputErased;
        protected boolean clearCardView;
        protected boolean identificationTypesInitialized;
        protected boolean hideIdentificationInput;
        protected boolean showInputContainer;
        protected boolean initializeGuessingForm;
        protected boolean initializeGuessingListeners;
        protected MercadoPagoError errorShown;
        protected boolean hideBankDeals;
        protected boolean hideSecurityCodeInput;
        protected boolean bankDealsShown;
        protected boolean onlyOnePMErrorViewShown;
        protected boolean infoContainerTurnedRed;
        protected String supportedPaymentMethodId;
        protected CardTokenException cardTokenError;
        protected boolean formDataErrorState;

        @Override
        public void setPaymentMethod(final PaymentMethod paymentMethod) {
            //Empty body
        }

        @Override
        public void askForIssuer(final CardInfo cardInfo, final List<Issuer> issuers,
            final PaymentMethod paymentMethod) {
            //Empty body
        }

        @Override
        public void recoverCardViews(final boolean lowResActive, final String cardNumber, final String cardHolderName,
            final String expiryMonth, final String expiryYear, final String identificationNumber,
            final IdentificationType identificationType) {
            // Empty body
        }

        @Override
        public void clearSecurityCodeEditText() {
            securityCodeInputErased = true;
        }

        @Override
        public void askForPaymentType(final List<PaymentMethod> paymentMethods, final List<PaymentType> paymentTypes,
            final CardInfo cardInfo) {
            // Empty body
        }

        @Override
        public void restoreBlackInfoContainerView() {
            onlyOnePMErrorViewShown = true;
            infoContainerTurnedRed = false;
        }

        @Override
        public void hideRedErrorContainerView(final boolean withAnimation) {
            multipleErrorViewShown = false;
            invalidPaymentMethod = false;
        }

        @Override
        public void resolvePaymentMethodSet(final PaymentMethod paymentMethod) {
            paymentMethodSet = true;
        }

        @Override
        public void clearErrorIdentificationNumber() {
            // Empty method
        }

        @Override
        public void setSoftInputMode() {
            // Empty method
        }

        @Override
        public void finishCardFlow(@NonNull final List<Issuer> issuers) {
            // Empty method
        }

        @Override
        public void finishCardFlow() {
            // Empty method
        }

        @Override
        public void setErrorContainerListener() {
            // Empty method
        }

        @Override
        public void showMissingIdentificationTypesError(final boolean recoverable, final String requestOrigin) {

        }

        @Override
        public void showSettingNotFoundForBinError(final boolean recoverable, final String requestOrigin) {

        }

        @Override
        public void setInvalidCardOnePaymentMethodErrorView() {
            invalidPaymentMethod = true;
            onlyOnePMErrorViewShown = true;
            infoContainerTurnedRed = true;
        }

        @Override
        public void setInvalidIdentificationNumberErrorView() {

        }

        @Override
        public void setInvalidEmptyNameErrorView() {

        }

        @Override
        public void setInvalidExpiryDateErrorView() {

        }

        @Override
        public void setInvalidFieldErrorView() {

        }

        @Override
        public void setInvalidCardMultipleErrorView() {
            invalidPaymentMethod = true;
            multipleErrorViewShown = true;
        }

        @Override
        public void hideProgress() {
            // Empty method
        }

        @Override
        public void finishCardStorageFlowWithSuccess() {
            // Empty body
        }

        @Override
        public void finishCardStorageFlowWithError(final String accessToken) {
            // Empty body
        }

        @Override
        public void showProgress() {
            // Empty body
        }

        @Override
        public void showApiExceptionError(final ApiException exception, final String requestOrigin) {
            // Empty body
        }

        @Override
        public void setupPresenter() {
            // Empty body
        }

        @Override
        public void onValidStart() {
            validStart = true;
            showInputContainer = true;
        }

        @Override
        public void hideExclusionWithOneElementInfoView() {
            // Empty body
        }

        @Override
        public void showError(final MercadoPagoError error, final String requestOrigin) {
            errorShown = error;
        }

        @Override
        public void setContainerAnimationListeners() {
            // Empty body
        }

        @Override
        public void setExclusionWithOneElementInfoView(final PaymentMethod supportedPaymentMethod,
            final boolean withAnimation) {
            onlyOnePMErrorViewShown = true;
            supportedPaymentMethodId = supportedPaymentMethod.getId();
        }

        @Override
        public void clearCardNumberInputLength() {
            cardNumberLengthDefault = true;
        }

        @Override
        public void clearErrorView() {
            errorState = false;
        }

        @Override
        public void checkClearCardView() {
            clearCardView = true;
        }

        @Override
        public void setBackButtonListeners() {
            initializeGuessingListeners = true;
        }

        @Override
        public void setCardNumberListeners(final PaymentMethodGuessingController controller) {
            initializeGuessingListeners = true;
        }

        @Override
        public void setErrorSecurityCode() {
            // Empty body
        }

        @Override
        public void setErrorCardNumber() {
            // Empty body
        }

        @Override
        public void setErrorView(final CardTokenException exception) {
            formDataErrorState = true;
            cardTokenError = exception;
        }

        @Override
        public void setErrorView(final String mErrorState) {
            formDataErrorState = true;
        }

        @Override
        public void setSecurityCodeListeners() {
            initializeGuessingListeners = true;
        }

        @Override
        public void setIdentificationTypeListeners() {
            initializeGuessingListeners = true;
        }

        @Override
        public void setNextButtonListeners() {
            initializeGuessingListeners = true;
        }

        @Override
        public void setIdentificationNumberListeners() {
            initializeGuessingListeners = true;
        }

        @Override
        public void setSecurityCodeInputMaxLength(final int length) {
            // Empty body
        }

        @Override
        public void setSecurityCodeViewLocation(final String location) {
            // Empty body
        }

        @Override
        public void setIdentificationNumberRestrictions(final String type) {
            // Empty body
        }

        @Override
        public void setCardholderNameListeners() {
            initializeGuessingListeners = true;
        }

        @Override
        public void setExpiryDateListeners() {
            initializeGuessingListeners = true;
        }

        @Override
        public void setCardholderName(final String cardholderName) {
            savedCardholderName = cardholderName;
        }

        @Override
        public void setCardNumberInputMaxLength(final int length) {
            // Empty body
        }

        @Override
        public void setErrorCardholderName() {
            // Empty body
        }

        @Override
        public void setErrorExpiryDate() {
            // Empty body
        }

        @Override
        public void setErrorIdentificationNumber() {
            // Empty body
        }

        @Override
        public void setIdentificationNumber(final String identificationNumber) {
            savedIdentificationNumber = identificationNumber;
        }

        @Override
        public void showIdentificationInput() {
            // Empty method
        }

        @Override
        public void showInputContainer() {
            showInputContainer = true;
        }

        @Override
        public void showBankDeals() {
            bankDealsShown = true;
        }

        @Override
        public void hideBankDeals() {
            hideBankDeals = true;
        }

        @Override
        public void hideIdentificationInput() {
            hideIdentificationInput = true;
        }

        @Override
        public void hideSecurityCodeInput() {
            hideSecurityCodeInput = true;
        }

        @Override
        public void initializeIdentificationTypes(final List<IdentificationType> identificationTypes) {
            identificationTypesInitialized = true;
        }

        @Override
        public void initializeTitle() {
            initializeGuessingForm = true;
        }

        @Override
        public void showFinishCardFlow() {
            // Empty body
        }

        @Override
        public void eraseDefaultSpace() {
            // Empty body
        }
    }

}
