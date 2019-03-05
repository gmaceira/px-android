package com.mercadopago.android.px.model.exceptions;

public class InvalidFieldException extends Exception {
    public static final int INVALID_CPF = 0;
    public static final int INVALID_IDENTIFICATION_LENGHT = 1;

    private final int errorCode;

    public InvalidFieldException(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
