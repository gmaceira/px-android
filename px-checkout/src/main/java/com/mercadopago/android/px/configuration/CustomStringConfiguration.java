package com.mercadopago.android.px.configuration;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import com.mercadopago.android.px.R;

public class CustomStringConfiguration {

    @StringRes private final int mainVerbStringResourceId;
    private final String customTitle;

    /* default */ CustomStringConfiguration(@NonNull final Builder builder) {
        mainVerbStringResourceId = builder.mainVerbStringResourceId;
        customTitle = builder.customTitle;
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

    public String getCustomTitle(){
        return customTitle;
    }

    public static class Builder {
        /* default */ int mainVerbStringResourceId;
        String customTitle;

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

        public Builder setCustomTitle(final String customTitle){
            this.customTitle = customTitle;
            return this;
        }

        public CustomStringConfiguration build() {
            return new CustomStringConfiguration(this);
        }
    }
}
