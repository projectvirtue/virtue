package io.github.taikonaut3.virtue.common.util;

import java.util.Collection;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public interface CollectionUtil {

    static <T> void addToList(Collection<T> collection, BiPredicate<T, T> predicate, T... items) {
        addToList(collection, predicate, null, items);
    }

    static <T> void addToList(Collection<T> collection, BiPredicate<T, T> predicate, Consumer<T> successCallBack, T... items) {
        loop:
        for (T item : items) {
            for (T collect : collection) {
                if (predicate.test(collect, item)) {
                    continue loop;
                }
            }
            collection.add(item);
            if (successCallBack != null) {
                successCallBack.accept(item);
            }
        }
    }

}
