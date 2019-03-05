package com.mercadopago.android.px.configuration;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.mercadopago.android.px.R;
import java.io.Serializable;

public final class CustomStringConfiguration implements Serializable {

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
     * Check if a custom payment vault title is setted
     * 
     * @return true/false if customPaymentVaultTitle is (or not) empty
     */
    public boolean hasCustomPaymentVaultTitle() {
        return !TextUtils.isEmpty(customPaymentVaultTitle);
    }

    /**
     * Obtain customized payment vault title
     *
     * @return Custom Payment Vault Title
     */
    @Nullable
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
        public Builder setCustomPaymentVaultTitle(@NonNull final String title){
            this.customPaymentVaultTitle = title;
            return this;
        }

        public CustomStringConfiguration build() {
            return new CustomStringConfiguration(this);
        }
    }
}
