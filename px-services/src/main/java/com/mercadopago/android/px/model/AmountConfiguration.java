package com.mercadopago.android.px.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AmountConfiguration implements Serializable, Parcelable {

    public static final int NO_SELECTED = -1;

    public final int selectedPayerCostIndex;
    public final List<PayerCost> payerCosts;
    public final String discountToken;

    public AmountConfiguration(final int selectedPayerCostIndex, @Nullable final List<PayerCost> payerCosts,
        @Nullable final String discountToken) {
        this.selectedPayerCostIndex = selectedPayerCostIndex;
        this.payerCosts = payerCosts;
        this.discountToken = discountToken;
    }

    protected AmountConfiguration(final Parcel in) {
        selectedPayerCostIndex = in.readInt();
        payerCosts = in.createTypedArrayList(PayerCost.CREATOR);
        discountToken = in.readString();
    }

    @NonNull
    public List<PayerCost> getPayerCosts() {
        return payerCosts == null ? new ArrayList<PayerCost>() : payerCosts;
    }

    public int getDefaultPayerCostIndex() {
        return selectedPayerCostIndex;
    }

    @Nullable
    public String getDiscountToken() {
        return discountToken;
    }

    public PayerCost getPayerCost(final int userSelectedPayerCost) {
        if (userSelectedPayerCost == NO_SELECTED) {
            return payerCosts.get(selectedPayerCostIndex);
        } else {
            return payerCosts.get(userSelectedPayerCost);
        }
    }

    public static final Creator<AmountConfiguration> CREATOR = new Creator<AmountConfiguration>() {
        @Override
        public AmountConfiguration createFromParcel(final Parcel in) {
            return new AmountConfiguration(in);
        }

        @Override
        public AmountConfiguration[] newArray(final int size) {
            return new AmountConfiguration[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(selectedPayerCostIndex);
        dest.writeTypedList(payerCosts);
        dest.writeString(discountToken);
    }
}
