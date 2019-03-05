package com.mercadopago.android.px.internal.util;

import android.support.annotation.Nullable;

import java.util.regex.Pattern;

public final class TextUtil {

    public static final String EMPTY = "";
    public static final CharSequence SPACE = " ";

    private static final Pattern DIGIT_PATTERN = Pattern.compile("\\d+");

    private TextUtil() {
        throw new AssertionError("Util classes shouldn't be instantiated.");
    }

    public static boolean isEmpty(@Nullable final CharSequence text) {
        return text == null || text.length() <= 0;
    }

    public static boolean isNotEmpty(@Nullable final CharSequence text) {
        return !isEmpty(text);
    }

    public static boolean isDigitsOnly(@Nullable final CharSequence text) {
        return text != null && DIGIT_PATTERN.matcher(text).matches();
    }
}