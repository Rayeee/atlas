package com.eparty.ccp.core.util;

public class DefaultValue {

    public static Object getDefaultValue(Class<?> clazz) {
        if (clazz.equals(boolean.class)) {
            return false;
        } else if (clazz.equals(char.class)) {
            return '\u0000';
        } else if (clazz.equals(byte.class)) {
            return 0;
        } else if (clazz.equals(short.class)) {
            return 0;
        } else if (clazz.equals(int.class)) {
            return 0;
        } else if (clazz.equals(long.class)) {
            return 0l;
        } else if (clazz.equals(float.class)) {
            return 0.0f;
        } else if (clazz.equals(double.class)) {
            return 0.0d;
        } else {
            return null;
        }
    }
}
