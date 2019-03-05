package com.mercadopago.android.px.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import java.io.Serializable;

public final class Site implements Serializable, Parcelable {

    public static final Creator<Site> CREATOR = new Creator<Site>() {
        @Override
        public Site createFromParcel(final Parcel in) {
            return new Site(in);
        }

        @Override
        public Site[] newArray(final int size) {
            return new Site[size];
        }
    };

    /* default */ static Site createWith(@NonNull final String id,
        @NonNull final String currencyId,
        @NonNull final String termsAndConditionsUrl, final boolean shouldWarnAboutBankInterests){
        return new Site(id, currencyId, termsAndConditionsUrl, shouldWarnAboutBankInterests);
    }

    private final String id;
    private final String currencyId;
    private final String termsAndConditionsUrl;
    private final boolean shouldWarnAboutBankInterests;

    private Site(@NonNull final String id,
        @NonNull final String currencyId,
        @NonNull final String termsAndConditionsUrl, final boolean shouldWarnAboutBankInterests) {
        this.id = id;
        this.currencyId = currencyId;
        this.termsAndConditionsUrl = termsAndConditionsUrl;
        this.shouldWarnAboutBankInterests = shouldWarnAboutBankInterests;
    }

    private Site(final Parcel in) {
        id = in.readString();
        currencyId = in.readString();
        termsAndConditionsUrl = in.readString();
        shouldWarnAboutBankInterests = in.readByte() == 1;
    }

    public String getId() {
        return id;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public boolean shouldWarnAboutBankInterests() {
        return shouldWarnAboutBankInterests;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(id);
        dest.writeString(currencyId);
        dest.writeString(termsAndConditionsUrl);
        dest.writeByte((byte) (shouldWarnAboutBankInterests ? 1 : 0));
    }

    public String getTermsAndConditionsUrl() {
        return termsAndConditionsUrl;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Site)) {
            return false;
        }

        final Site site = (Site) o;

        if (id != null ? !id.equals(site.id) : site.id != null) {
            return false;
        }
        if (currencyId != null ? !currencyId.equals(site.currencyId) : site.currencyId != null) {
            return false;
        }
        return termsAndConditionsUrl != null ? termsAndConditionsUrl.equals(site.termsAndConditionsUrl)
            : site.termsAndConditionsUrl == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (currencyId != null ? currencyId.hashCode() : 0);
        result = 31 * result + (termsAndConditionsUrl != null ? termsAndConditionsUrl.hashCode() : 0);
        return result;
    }
}
