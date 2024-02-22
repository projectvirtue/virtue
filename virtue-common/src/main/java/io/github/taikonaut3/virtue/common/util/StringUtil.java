package io.github.taikonaut3.virtue.common.util;

import java.util.Objects;

public interface StringUtil {

    static boolean isBlank(String str) {
        return (str == null || str.isEmpty() || isWhitespace(str));
    }

    static String isBlankOrDefault(String target, String defaultValue) {
        return isBlank(target) ? defaultValue : target;
    }

    static String normalizePath(String path) {
        return path.replaceAll("^/+", "").replaceAll("/+$", "").replaceAll("/+", "/");
    }

    static String simpleClassName(Object o) {
        if (o == null) {
            return "null_object";
        } else {
            return simpleClassName(o.getClass());
        }
    }

    static String simpleClassName(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        String className = clazz.getName();
        final int lastDotIdx = className.lastIndexOf(".");
        if (lastDotIdx > -1) {
            return className.substring(lastDotIdx + 1);
        }
        return className;
    }

    private static boolean isWhitespace(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}
