package com.mercadopago.android.px.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import java.math.BigDecimal;

public class DiscountConfigurationModel implements Parcelable {

    public static final DiscountConfigurationModel NONE = new DiscountConfigurationModel(null, null, true);

    private final Discount discount;
    private final Campaign campaign;
    private final boolean isAvailable;

    public DiscountConfigurationModel(@Nullable final Discount discount, @Nullable final Campaign campaign,
        final boolean isAvailable) {
        this.discount = discount;
        this.campaign = campaign;
        this.isAvailable = isAvailable;
    }

    protected DiscountConfigurationModel(final Parcel in) {
        discount = in.readParcelable(Discount.class.getClassLoader());
        campaign = in.readParcelable(Campaign.class.getClassLoader());
        isAvailable = in.readByte() != 0;
    }

    public static final Creator<DiscountConfigurationModel> CREATOR = new Creator<DiscountConfigurationModel>() {
        @Override
        public DiscountConfigurationModel createFromParcel(final Parcel in) {
            return new DiscountConfigurationModel(in);
        }

        @Override
        public DiscountConfigurationModel[] newArray(final int size) {
            return new DiscountConfigurationModel[size];
        }
    };

    public Discount getDiscount() {
        return discount;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public boolean hasValidDiscount() {
        return discount != null && campaign != null;
    }

    public BigDecimal getAmountWithDiscount(final BigDecimal amount) {
        if (hasValidDiscount()) {
            return discount.getAmountWithDiscount(amount);
        } else {
            return amount;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeParcelable(discount, flags);
        dest.writeParcelable(campaign, flags);
        dest.writeByte((byte) (isAvailable ? 1 : 0));
    }
}