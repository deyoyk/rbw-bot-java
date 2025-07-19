/*
 * Recoded by deyo 
 */
package com.deyo.rbw.childclasses.configuration;

public final class NumberConversions {
    private NumberConversions() {
    }

    public static int floor(double num) {
        int floor = (int)num;
        return (double)floor == num ? floor : floor - (int)(Double.doubleToRawLongBits(num) >>> 63);
    }

    public static int ceil(double num) {
        int floor = (int)num;
        return (double)floor == num ? floor : floor + (int)((Double.doubleToRawLongBits(num) ^ 0xFFFFFFFFFFFFFFFFL) >>> 63);
    }

    public static int round(double num) {
        return NumberConversions.floor(num + 0.5);
    }

    public static double square(double num) {
        return num * num;
    }

    public static int toInt(Object object) {
        if (object instanceof Number) {
            return ((Number)object).intValue();
        }
        try {
            return Integer.valueOf(object.toString());
        }
        catch (NullPointerException | NumberFormatException runtimeException) {
            return 0;
        }
    }

    public static float toFloat(Object object) {
        if (object instanceof Number) {
            return ((Number)object).floatValue();
        }
        try {
            return Float.valueOf(object.toString()).floatValue();
        }
        catch (NullPointerException | NumberFormatException runtimeException) {
            return 0.0f;
        }
    }

    public static double toDouble(Object object) {
        if (object instanceof Number) {
            return ((Number)object).doubleValue();
        }
        try {
            return Double.valueOf(object.toString());
        }
        catch (NullPointerException | NumberFormatException runtimeException) {
            return 0.0;
        }
    }

    public static long toLong(Object object) {
        if (object instanceof Number) {
            return ((Number)object).longValue();
        }
        try {
            return Long.valueOf(object.toString());
        }
        catch (NullPointerException | NumberFormatException runtimeException) {
            return 0L;
        }
    }

    public static short toShort(Object object) {
        if (object instanceof Number) {
            return ((Number)object).shortValue();
        }
        try {
            return Short.valueOf(object.toString());
        }
        catch (NullPointerException | NumberFormatException runtimeException) {
            return 0;
        }
    }

    public static byte toByte(Object object) {
        if (object instanceof Number) {
            return ((Number)object).byteValue();
        }
        try {
            return Byte.valueOf(object.toString());
        }
        catch (NullPointerException | NumberFormatException runtimeException) {
            return 0;
        }
    }

    public static boolean isFinite(double d) {
        return Math.abs(d) <= Double.MAX_VALUE;
    }

    public static boolean isFinite(float f) {
        return Math.abs(f) <= Float.MAX_VALUE;
    }

    public static void checkFinite(double d, String message) {
        if (!NumberConversions.isFinite(d)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void checkFinite(float d, String message) {
        if (!NumberConversions.isFinite(d)) {
            throw new IllegalArgumentException(message);
        }
    }
}

