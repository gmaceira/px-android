package com.mercadopago.android.px.configuration;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.mercadopago.android.px.R;

public class CustomStringConfiguration {

    @StringRes private final int mainVerbStringResourceId;
    @Nullable private final String customPaymentVaultTitle;

    /* default */ CustomStringConfiguration(@NonNull final Builder builder) {
        mainVerbStringResourceId = builder.mainVerbStringResourceId;
        customPaymentVaultTitle = builder.customPaymentVaultTitle;
    }

    /**
     * Let us know what the main verb is
     *
     * @return custom main verb or the default one
     */
    @StringRes
    public int getMainVerbStringResourceId() {
        return mainVerbStringResourceId;
    }

    /**
     * Check if has
     * @return true/false if customPaymentVaultTitle is (or not) empty
     */
    public boolean hasCustomPaymentVaultTitle() {
        return !TextUtils.isEmpty(customPaymentVaultTitle);
    }

    /**
     *
     * @return Custom Payment Vault Title
     */
    public String getCustomPaymentVaultTitle() {
        return customPaymentVaultTitle;
    }

    public static class Builder {
        /* default */ int mainVerbStringResourceId;
        /* default */ String customPaymentVaultTitle;

        public Builder() {
            mainVerbStringResourceId = R.string.px_main_verb;
        }

        /**
         * Used to replace the main verb in Payment Methods screen
         *
         * @param mainVerbStringResId the string resource that will be used
         * @return builder to keep operating
         */
        @SuppressWarnings("unused")
        public Builder setMainVerbStringResourceId(@StringRes final int mainVerbStringResId) {
            mainVerbStringResourceId = mainVerbStringResId;
            return this;
        }

        /**
         * Add the possibility to add a custom Title in payment vault screen.
         *
         * @param title Custom title to be setted
         * @return builder to keep operating
         */
        public Builder setCustomPaymentVaultTitle(String title){
            this.customPaymentVaultTitle = title;
            return this;
        }

        public CustomStringConfiguration build() {
            return new CustomStringConfiguration(this);
        }
    }
}
