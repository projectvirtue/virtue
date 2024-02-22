package io.github.taikonaut3.virtue.common.util;

import java.util.Objects;

public interface AssertUtil {

    static void condition(boolean condition) {
        condition(condition, null);
    }

    static void condition(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    static void notNull(Object object) {
        notNull(object, null);
    }

    static void notNull(Object... objects) {
        for (Object object : objects) {
            notNull(object);
        }
    }

    static void notNull(Object object, String message) {
        if (object == null) {
            if (StringUtil.isBlank(message))
                message = "The object can't be null";
            throw new NullPointerException(message);
        }
    }

    static void assertNotBlank(String str, String message) {
        condition(!StringUtil.isBlank(str), message);
    }

    static void assertEquals(Object expected, Object actual) {
        assertEquals(expected, actual, null);
    }

    static void assertEquals(Object expected, Object actual, String message) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError(message);
        }
    }

}
