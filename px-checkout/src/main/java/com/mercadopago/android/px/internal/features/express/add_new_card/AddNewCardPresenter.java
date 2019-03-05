package com.mercadopago.android.px.internal.features.express.add_new_card;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mercadopago.android.px.internal.base.BasePresenter;
import com.mercadopago.android.px.internal.repository.GroupsRepository;
import com.mercadopago.android.px.model.PaymentMethodSearch;
import com.mercadopago.android.px.model.PaymentMethodSearchItem;
import com.mercadopago.android.px.model.exceptions.ApiException;
import com.mercadopago.android.px.services.Callback;
import java.util.List;

public class AddNewCardPresenter extends BasePresenter<AddNewCard.View> implements AddNewCard.Actions {

    private static final String TYPE_TO_DRIVE = "cards";

    private final GroupsRepository groupsRepository;

    public AddNewCardPresenter(@NonNull final GroupsRepository groupsRepository) {
        this.groupsRepository = groupsRepository;
    }

    @Override
    public void onAddNewCardSelected() {
        groupsRepository.getGroups().execute(new Callback<PaymentMethodSearch>() {
            @Override
            public void success(final PaymentMethodSearch paymentMethodSearch) {
                final PaymentMethodSearchItem paymentMethodSearchItem = getCardsGroup(paymentMethodSearch.getGroups());
                if (paymentMethodSearchItem != null) {
                    getView().showPaymentMethodsWithSelection(paymentMethodSearchItem);
                } else {
                    getView().showPaymentMethods();
                }
            }

            @Override
            public void failure(final ApiException apiException) {
                throw new IllegalStateException("AddNewCardPresenter could not retrieve PaymentMethodSearch");
            }
        });
    }

    @Nullable
    private PaymentMethodSearchItem getCardsGroup(@NonNull final List<PaymentMethodSearchItem> groups) {
        for (final PaymentMethodSearchItem paymentMethodSearchItem : groups) {
            if (TYPE_TO_DRIVE.equalsIgnoreCase(paymentMethodSearchItem.getId())) {
                return paymentMethodSearchItem;
            }
        }
        return null;
    }
}
