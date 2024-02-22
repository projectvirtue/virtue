package io.github.taikonaut3.virtue.config.manager;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractManager<T> {

    protected final Virtue virtue;

    protected Map<String, T> map;

    protected AbstractManager(Virtue virtue) {
        this.virtue = virtue;
        this.map = new HashMap<>();
    }

    public void register(String name, T value) {
        map.put(name, value);
    }

    public void remove(String name) {
        map.remove(name);
    }

    public T get(String name) {
        return map.get(name);
    }

    public Map<String, T> getManagerMap() {
        return map;
    }

}
