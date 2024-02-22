package io.github.taikonaut3.virtue.common.util;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * 生成工具类
 */
public interface GenerateUtil {

    static String generateKey(Method method) {
        StringBuilder builder = new StringBuilder();
        String name = method.getName();
        builder.append(name);
        Type[] types = method.getGenericParameterTypes();
        if (types.length > 0) {
            builder.append("(");
            for (int i = 0; i < types.length; i++) {
                builder.append(types[i].getTypeName());
                if (i != types.length - 1) {
                    builder.append(",");
                }
            }
            builder.append(")");
        }
        return builder.toString();
    }

    static String generateCallerIdentification(String protocol, String path) {
        return protocol + ":" + path;
    }
}
