package com.mercadopago.android.px.internal.repository;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.callbacks.MPCall;
import com.mercadopago.android.px.model.IdentificationType;
import java.util.List;

public interface IdentificationRepository {

    /**
     * Get user's identification types according to site.
     * @return List of IdentificationTypes.
     */
    MPCall<List<IdentificationType>> getIdentificationTypes();

    MPCall<List<IdentificationType>> getIdentificationTypesAsync(@NonNull final String accessToken);
}
