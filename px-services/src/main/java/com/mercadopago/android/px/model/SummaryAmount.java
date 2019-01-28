package com.mercadopago.android.px.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import java.util.HashMap;
import java.util.Map;

public class SummaryAmount implements Parcelable {

    private final String defaultAmountConfiguration;
    private Map<String, DiscountConfigurationModel> discountsConfigurations;
    private Map<String, AmountConfiguration> amountConfigurations;

    /* default */ SummaryAmount(final Parcel in) {
        defaultAmountConfiguration = in.readString();
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(defaultAmountConfiguration);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SummaryAmount> CREATOR = new Creator<SummaryAmount>() {
        @Override
        public SummaryAmount createFromParcel(final Parcel in) {
            return new SummaryAmount(in);
        }

        @Override
        public SummaryAmount[] newArray(final int size) {
            return new SummaryAmount[size];
        }
    };

    public String getDefaultAmountConfiguration() {
        return defaultAmountConfiguration;
    }

    public AmountConfiguration getAmountConfiguration(final String key) {
        return amountConfigurations.get(key);
    }

    @NonNull
    public Map<String, DiscountConfigurationModel> getDiscountsConfigurations() {
        return discountsConfigurations == null
            ? new HashMap<String, DiscountConfigurationModel>() : discountsConfigurations;
    }
}
