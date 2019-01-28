package com.mercadopago.android.px.internal.features.guessing_card;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.gson.reflect.TypeToken;
import com.mercadopago.android.px.configuration.AdvancedConfiguration;
import com.mercadopago.android.px.internal.callbacks.FailureRecovery;
import com.mercadopago.android.px.internal.callbacks.TaggedCallback;
import com.mercadopago.android.px.internal.controllers.PaymentMethodGuessingController;
import com.mercadopago.android.px.internal.repository.GroupsRepository;
import com.mercadopago.android.px.internal.repository.IssuersRepository;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.repository.UserSelectionRepository;
import com.mercadopago.android.px.internal.util.ApiUtil;
import com.mercadopago.android.px.internal.util.JsonUtil;
import com.mercadopago.android.px.internal.util.TextUtil;
import com.mercadopago.android.px.model.BankDeal;
import com.mercadopago.android.px.model.IdentificationType;
import com.mercadopago.android.px.model.Issuer;
import com.mercadopago.android.px.model.PaymentMethod;
import com.mercadopago.android.px.model.PaymentMethodSearch;
import com.mercadopago.android.px.model.PaymentRecovery;
import com.mercadopago.android.px.model.PaymentType;
import com.mercadopago.android.px.model.Token;
import com.mercadopago.android.px.model.exceptions.ApiException;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;
import com.mercadopago.android.px.preferences.PaymentPreference;
import com.mercadopago.android.px.services.Callback;
import com.mercadopago.android.px.tracking.internal.MPTracker;
import java.lang.reflect.Type;
import java.util.List;

public class GuessingCardPaymentPresenter extends GuessingCardPresenter {

    @NonNull /* default */ final PaymentSettingRepository paymentSettingRepository;
    @NonNull private final UserSelectionRepository userSelectionRepository;
    @NonNull private final GroupsRepository groupsRepository;
    @NonNull private final IssuersRepository issuersRepository;
    @NonNull private final AdvancedConfiguration advancedConfiguration;
    @Nullable private List<BankDeal> bankDealList;

    protected PaymentRecovery paymentRecovery;
    private Issuer issuer;

    public GuessingCardPaymentPresenter(@NonNull final UserSelectionRepository userSelectionRepository,
        @NonNull final PaymentSettingRepository paymentSettingRepository,
        @NonNull final GroupsRepository groupsRepository,
        @NonNull final IssuersRepository issuersRepository,
        @NonNull final AdvancedConfiguration advancedConfiguration,
        @NonNull final PaymentRecovery paymentRecovery) {
        super();
        this.userSelectionRepository = userSelectionRepository;
        this.paymentSettingRepository = paymentSettingRepository;
        this.groupsRepository = groupsRepository;
        this.issuersRepository = issuersRepository;
        this.advancedConfiguration = advancedConfiguration;
        this.paymentRecovery = paymentRecovery;
    }

    @Override
    public void initialize() {
        getView().onValidStart();
        initializeCardToken();
        resolveBankDeals();
        getPaymentMethods();
        if (recoverWithCardHolder()) {
            fillRecoveryFields();
        }
    }

    private void fillRecoveryFields() {
        getView().setCardholderName(paymentSettingRepository.getToken().getCardHolder().getName());
        getView().setIdentificationNumber(
            paymentSettingRepository.getToken().getCardHolder().getIdentification().getNumber());
    }

    @Nullable
    @Override
    public PaymentMethod getPaymentMethod() {
        return userSelectionRepository.getPaymentMethod();
    }

    @Override
    public void setPaymentMethod(@Nullable final PaymentMethod paymentMethod) {
        userSelectionRepository.select(paymentMethod);
        if (paymentMethod == null) {
            clearCardSettings();
        }
    }

    @Override
    public void getIdentificationTypesAsync() {
        getResourcesProvider().getIdentificationTypesAsync(
            new TaggedCallback<List<IdentificationType>>(ApiUtil.RequestOrigin.GET_IDENTIFICATION_TYPES) {
                @Override
                public void onSuccess(final List<IdentificationType> identificationTypes) {
                    resolveIdentificationTypes(identificationTypes);
                }

                @Override
                public void onFailure(final MercadoPagoError error) {
                    if (isViewAttached()) {
                        getView().showError(error, ApiUtil.RequestOrigin.GET_IDENTIFICATION_TYPES);
                        setFailureRecovery(new FailureRecovery() {
                            @Override
                            public void recover() {
                                getIdentificationTypesAsync();
                            }
                        });
                    }
                }
            });
    }

    @Override
    public void getPaymentMethods() {
        getView().showProgress();
        groupsRepository.getGroups().enqueue(new Callback<PaymentMethodSearch>() {
            @Override
            public void success(final PaymentMethodSearch paymentMethodSearch) {
                if (isViewAttached()) {
                    getView().hideProgress();
                    final PaymentPreference paymentPreference =
                        paymentSettingRepository.getCheckoutPreference().getPaymentPreference();
                    paymentMethodGuessingController = new PaymentMethodGuessingController(
                        paymentPreference.getSupportedPaymentMethods(paymentMethodSearch.getPaymentMethods()),
                        getPaymentTypeId(),
                        paymentPreference.getExcludedPaymentTypes());
                    startGuessingForm();
                }
            }

            @Override
            public void failure(final ApiException apiException) {
                if (isViewAttached()) {
                    getView().hideProgress();
                    setFailureRecovery(new FailureRecovery() {
                        @Override
                        public void recover() {
                            getPaymentMethods();
                        }
                    });
                }
            }
        });
    }

    @Nullable
    @Override
    public String getPaymentTypeId() {
        return userSelectionRepository.getPaymentType();
    }

    private void resolveBankDeals() {
        if (advancedConfiguration.isBankDealsEnabled()) {
            getBankDealsAsync();
        } else {
            getView().hideBankDeals();
        }
    }

    @Nullable
    @Override
    public List<BankDeal> getBankDealsList() {
        return bankDealList;
    }

    private void setBankDealsList(@Nullable final List<BankDeal> bankDealsList) {
        bankDealList = bankDealsList;
    }

    @Override
    public void onIssuerSelected(final Long issuerId) {
        // Empty body, this behavior only exists on CardStoragePresenter
    }

    @Override
    public void onSaveInstanceState(final Bundle outState, final String cardSideState, final boolean lowResActive) {
        if (getPaymentMethod() != null) {
            super.onSaveInstanceState(outState, cardSideState, lowResActive);
            outState.putString(BANK_DEALS_LIST_BUNDLE, JsonUtil.getInstance().toJson(getBankDealsList()));
            outState.putString(PAYMENT_TYPES_LIST_BUNDLE, JsonUtil.getInstance().toJson(getPaymentTypes()));
        }
    }

    @Override
    public void onRestoreInstanceState(final Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.getString(PAYMENT_METHOD_BUNDLE) != null) {
            final String paymentMethodBundleJson = savedInstanceState.getString(PAYMENT_METHOD_BUNDLE);
            if (!TextUtil.isEmpty(paymentMethodBundleJson)) {
                List<PaymentType> paymentTypesList;
                try {
                    final Type listType = new TypeToken<List<PaymentType>>() {
                    }.getType();
                    paymentTypesList = JsonUtil.getInstance().getGson().fromJson(
                        savedInstanceState.getString(PAYMENT_TYPES_LIST_BUNDLE), listType);
                } catch (final Exception ex) {
                    paymentTypesList = null;
                }
                setPaymentTypesList(paymentTypesList);
                List<BankDeal> bankDealsList;
                try {
                    final Type listType = new TypeToken<List<BankDeal>>() {
                    }.getType();
                    bankDealsList = JsonUtil.getInstance().getGson().fromJson(
                        savedInstanceState.getString(BANK_DEALS_LIST_BUNDLE), listType);
                } catch (final Exception ex) {
                    bankDealsList = null;
                }
                setBankDealsList(bankDealsList);
                setPaymentRecovery(JsonUtil.getInstance()
                    .fromJson(savedInstanceState.getString(PAYMENT_RECOVERY_BUNDLE), PaymentRecovery.class));
                super.onRestoreInstanceState(savedInstanceState);
            }
        }
    }

    /* default */ void getBankDealsAsync() {
        getResourcesProvider()
            .getBankDealsAsync(new TaggedCallback<List<BankDeal>>(ApiUtil.RequestOrigin.GET_BANK_DEALS) {
                @Override
                public void onSuccess(final List<BankDeal> bankDeals) {
                    resolveBankDeals(bankDeals);
                }

                @Override
                public void onFailure(final MercadoPagoError error) {
                    if (isViewAttached()) {
                        setFailureRecovery(new FailureRecovery() {
                            @Override
                            public void recover() {
                                getBankDealsAsync();
                            }
                        });
                    }
                }
            });
    }

    /* default */ void resolveBankDeals(final List<BankDeal> bankDeals) {
        if (isViewAttached()) {
            if (bankDeals == null || bankDeals.isEmpty()) {
                getView().hideBankDeals();
            } else {
                bankDealList = bankDeals;
                getView().showBankDeals();
            }
        }
    }

    @Override
    public void resolveTokenRequest(final Token token) {
        this.token = token;
        paymentSettingRepository.configure(token);
        MPTracker.getInstance().trackTokenId(token.getId(), paymentSettingRepository.getPublicKey(),
            paymentSettingRepository.getCheckoutPreference().getSite());
        getIssuers();
    }

    /* default */ void getIssuers() {
        final PaymentMethod paymentMethod = getPaymentMethod();
        if (paymentMethod != null) {
            issuersRepository.getIssuers(paymentMethod.getId(), bin).enqueue(
                new TaggedCallback<List<Issuer>>(ApiUtil.RequestOrigin.GET_ISSUERS) {
                    @Override
                    public void onSuccess(final List<Issuer> issuers) {
                        resolveIssuersList(issuers);
                    }

                    @Override
                    public void onFailure(final MercadoPagoError error) {
                        setFailureRecovery(new FailureRecovery() {
                            @Override
                            public void recover() {
                                getIssuers();
                            }
                        });
                        getView().showError(error, ApiUtil.RequestOrigin.GET_ISSUERS);
                    }
                });
        }
    }

    /* default */ void resolveIssuersList(final List<Issuer> issuers) {
        if (issuers.size() == 1) {
            issuer = issuers.get(0);
            userSelectionRepository.select(issuer);
            // All set -  card info - user must select installments
            getView().finishCardFlow();
        } else {
            // User must select issuer and installments.
            getView().finishCardFlow(issuers);
        }
    }

    public PaymentRecovery getPaymentRecovery() {
        return paymentRecovery;
    }

    public void setPaymentRecovery(final PaymentRecovery paymentRecovery) {
        this.paymentRecovery = paymentRecovery;
        if (recoverWithCardHolder()) {
            saveCardholderName(paymentSettingRepository.getToken().getCardHolder().getName());
            saveIdentificationNumber(
                paymentSettingRepository.getToken().getCardHolder().getIdentification().getNumber());
        }
    }

    private boolean recoverWithCardHolder() {
        return paymentRecovery != null && paymentSettingRepository.getToken() != null &&
            paymentSettingRepository.getToken().getCardHolder() != null;
    }

    @Override
    public void createToken() {
        getResourcesProvider()
            .createTokenAsync(cardToken, new TaggedCallback<Token>(ApiUtil.RequestOrigin.CREATE_TOKEN) {
                @Override
                public void onSuccess(final Token token) {
                    resolveTokenRequest(token);
                }

                @Override
                public void onFailure(final MercadoPagoError error) {
                    resolveTokenCreationError(error, ApiUtil.RequestOrigin.CREATE_TOKEN);
                }
            });
    }
}
