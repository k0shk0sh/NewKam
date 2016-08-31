package com.fastaccess.helper;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

/**
 * Created by kosh20111 on 3/11/2015. CopyRights @ Innov8tif
 * <p>
 * Input Helper to validate stuff related to input fields.
 */
public class InputHelper {


    private static boolean isWhiteSpaces(String s) {
        return s != null && s.matches("\\s+");
    }

    public static boolean isEmail(String text) {
        return Patterns.EMAIL_ADDRESS.matcher(text).matches();
    }

    public static boolean isWeb(String text) {
        return Patterns.WEB_URL.matcher(text).matches();
    }

    public static boolean isPhone(String text) {
        return Patterns.PHONE.matcher(text).matches();
    }

    public static boolean isEmpty(String text) {
        return text == null || TextUtils.isEmpty(text) || isWhiteSpaces(text);
    }

    public static boolean isEmpty(Object text) {
        return text == null || TextUtils.isEmpty(text.toString()) || isWhiteSpaces(text.toString());
    }

    public static boolean isEmpty(EditText text) {
        return text == null || isEmpty(text.getText().toString());
    }

    public static boolean isEmpty(TextView text) {
        return text == null || isEmpty(text.getText().toString());
    }

    public static boolean isEmpty(TextInputLayout txt) {
        return txt == null || isEmpty(txt.getEditText());
    }

    public static boolean isDigit(String text) {
        return TextUtils.isDigitsOnly(text);
    }

    public static boolean isAlphaNumeric(String s) {
        String pattern = "^[a-zA-Z0-9]*$";
        return s.matches(pattern);
    }

    public static String toNA(String value) {
        return isEmpty(value) ? "N/A" : value;
    }

    public static String toString(EditText editText) {
        return editText.getText().toString();
    }

    public static String toString(TextView editText) {
        return editText.getText().toString();
    }

    public static String toString(TextInputLayout textInputLayout) {
        return toString(textInputLayout.getEditText());
    }

    public static String formatSize(Context context, long size) {
        return Formatter.formatShortFileSize(context, size);
    }

    public static String formatPrice(double doubleValue) {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) nf).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("");
        ((DecimalFormat) nf).setDecimalFormatSymbols(decimalFormatSymbols);
        return nf.format(doubleValue).trim();
    }

    public static String ordinal(int i) {
        return i % 100 == 11 || i % 100 == 12 || i % 100 == 13 ? i + "th" : i +
                new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"}[i % 10];
    }
}
