package com.mercadopago.android.px.mocks;

import com.mercadopago.android.px.internal.util.TextUtil;
import com.mercadopago.android.px.model.Identification;

public final class IdentificationUtils {

    private static final String IDENTIFICATION_TYPE_CPF = "CPF";
    private static final String VALID_CPF_NUMBER = "12345678909";
    private static final String INVALID_CPF_NUMBER = "12345678900";
    private static final String INVALID_LENGTH_CPF_NUMBER = "1234567890";
    private static final String INVALID_EQUAL_NUMBERS_CPF = "00000000000";

    private IdentificationUtils() {
    }

    public static Identification getIdentificationCPF() {
        Identification identification = new Identification();
        identification.setNumber(VALID_CPF_NUMBER);
        identification.setType(IDENTIFICATION_TYPE_CPF);

        return identification;
    }

    public static Identification getIdentificationWithWrongLengthNumber() {
        Identification identification = new Identification();
        identification.setNumber(TextUtil.EMPTY);
        identification.setType(IDENTIFICATION_TYPE_CPF);

        return identification;
    }

    public static Identification getIdentificationWithInvalidCpfNumber() {
        Identification identification = new Identification();
        identification.setNumber(INVALID_CPF_NUMBER);
        identification.setType(IDENTIFICATION_TYPE_CPF);

        return identification;
    }

    public static Identification getIdentificationWithInvalidLengthCpfNumber() {
        Identification identification = new Identification();
        identification.setNumber(INVALID_LENGTH_CPF_NUMBER);
        identification.setType(IDENTIFICATION_TYPE_CPF);

        return identification;
    }

    public static Identification getIdentificationWithInvalidEqualNumbersCpf() {
        Identification identification = new Identification();
        identification.setNumber(INVALID_EQUAL_NUMBERS_CPF);
        identification.setType(IDENTIFICATION_TYPE_CPF);

        return identification;
    }
}
