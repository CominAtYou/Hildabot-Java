package com.cominatyou.util;

import java.text.DecimalFormat;

/**
 * Class for formatting thousands with commas.
 */
public class ThousandsFormat {
    private static final DecimalFormat formatter = new DecimalFormat("#,###");
    private static final DecimalFormat formatterWithDecimals = new DecimalFormat("#,###.##");

    /**
     * <p>Formats a long with comma-based thousands separators.</p>
     * Any number over 999 will be formatted with thousands separators. For example, {@code 10927} will be formatted as {@code 10,927},
     * while any number under 1,000 will be returned as is.
     * @param number The number to format.
     * @return The number, formatted with thousands separators if needed.
     */
    public static String format(long number) {
        return formatter.format(number);
    }

    /**
     * <p>Formats an int with comma-based thousands separators.</p>
     * Any number over 999 will be formatted with thousands separators. For example, {@code 10927} will be formatted as {@code 10,927},
     * while any number under 1,000 will be returned as is.
     * @param number The number to format.
     * @return The number, formatted with thousands separators if needed.
     */
    public static String format(int number) {
        return formatter.format(number);
    }

    /**
     * <p>Formats a double with comma-based thousands separators.</p>
     * Any number over 999 will be formatted with thousands separators. For example, {@code 10927} will be formatted as {@code 10,927},
     * while any number under 1,000 will be returned as is.
     * @param number The number to format.
     * @return The number, formatted with thousands separators if needed.
     */
    public static String format(double number) {
        return formatterWithDecimals.format(number);
    }

    /**
     * <p>Formats a float with comma-based thousands separators.</p>
     * Any number over 999 will be formatted with thousands separators. For example, {@code 10927} will be formatted as {@code 10,927},
     * while any number under 1,000 will be returned as is.
     * @param number The number to format.
     * @return The number, formatted with thousands separators if needed.
     */
    public static String format(float number) {
        return formatterWithDecimals.format(number);
    }
}
