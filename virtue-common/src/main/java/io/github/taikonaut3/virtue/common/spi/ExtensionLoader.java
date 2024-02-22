package io.github.taikonaut3.virtue.common.spi;

import io.github.taikonaut3.virtue.common.util.ReflectUtil;
import io.github.taikonaut3.virtue.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.taikonaut3.virtue.common.constant.Constant.SPI_FIX_PATH;

/**
 * Extension spi loader.
 *
 * @param <S>
 */
public class ExtensionLoader<S> {

    private static final Logger logger = LoggerFactory.getLogger(ExtensionLoader.class);

    private static final String PREFIX = SPI_FIX_PATH;

    private static final ClassLoader classLoader = ClassLoader.getSystemClassLoader();

    private static final Map<String, ExtensionLoader<?>> LOADED_MAP = new ConcurrentHashMap<>();

    private static final Map<Class<?>, Set<LoadedListener<?>>> LISTENERMAP = new ConcurrentHashMap<>();

    private final Map<String, S> services;

    private final Map<String, Class<? extends S>> serviceClasses;

    private final Class<S> type;

    private final String defaultService;

    public ExtensionLoader(Class<S> clazz) {
        this.type = clazz;
        this.defaultService = clazz.getAnnotation(ServiceInterface.class).value();
        this.services = new ConcurrentHashMap<>();
        this.serviceClasses = new ConcurrentHashMap<>();
        loadServiceClasses(clazz);
    }

    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <S> ExtensionLoader<S> load(Class<S> clazz, LoadedListener<S>... loadedListeners) {
        if (clazz == null) {
            throw new IllegalArgumentException("Service type is null");
        }
        if (!clazz.isInterface()) {
            throw new IllegalArgumentException("Service type(" + clazz + ") is not interface");
        }
        if (!clazz.isAnnotationPresent(ServiceInterface.class)) {
            throw new IllegalArgumentException("Service type(" + clazz + ") can not be load because WITHOUT @ServiceInterface");
        }
        ExtensionLoader<?> extensionLoader = LOADED_MAP.get(clazz.getTypeName());
        if (extensionLoader == null) {
            extensionLoader = new ExtensionLoader<>(clazz);
            LOADED_MAP.putIfAbsent(clazz.getTypeName(), extensionLoader);
        }
        addListener(clazz, loadedListeners);
        return (ExtensionLoader<S>) extensionLoader;
    }

    @SafeVarargs
    public static <S> S loadService(Class<S> clazz, String serviceName, LoadedListener<S>... loadedListeners) {
        return load(clazz, loadedListeners).getService(serviceName);
    }

    @SafeVarargs
    public static <S> S loadService(Class<S> clazz, LoadedListener<S>... loadedListeners) {
        return load(clazz, loadedListeners).getDefault();
    }

    @SafeVarargs
    public static <S> List<S> loadServices(Class<S> clazz, LoadedListener<S>... loadedListeners) {
        return load(clazz, loadedListeners).getServices();
    }

    @SafeVarargs
    public static <S> void addListener(Class<S> interfaceType, LoadedListener<S>... loadedListeners) {
        if (loadedListeners != null && loadedListeners.length > 0) {
            Set<LoadedListener<?>> loadedListenerSet = LISTENERMAP.computeIfAbsent(interfaceType, k -> new HashSet<>());
            Collections.addAll(loadedListenerSet, loadedListeners);
        }
    }

    @SuppressWarnings("unchecked")
    private void loadServiceClasses(Class<S> clazz) {
        /**========================load services from {@link PREFIX}========================*/
        try {
            Enumeration<URL> resources = classLoader.getResources(PREFIX + clazz.getTypeName());
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                /**=======================load legal services to {@link services}========================*/
                try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
                    String serviceClassName;
                    while ((serviceClassName = br.readLine()) != null) {
                        try {
                            Class<?> serviceClass = classLoader.loadClass(serviceClassName);
                            if (serviceClass.isAnnotationPresent(ServiceProvider.class)) {
                                ServiceProvider provider = serviceClass.getAnnotation(ServiceProvider.class);
                                String key = StringUtil.isBlankOrDefault(provider.value(), serviceClass.getName());
                                serviceClasses.put(key, (Class<? extends S>) serviceClass);
                            }
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            logger.error("Load " + clazz + " error", e);
            throw new RuntimeException(e);
        }

    }

    public S getService(String serviceName) {
        S service = services.get(serviceName);
        if (service == null) {
            Class<? extends S> clazz = serviceClasses.get(serviceName);
            if (clazz == null) {
                logger.warn("Load service type({}:{}) failed,Unknown the [{}] Please check is Exist", this.type, serviceName, serviceName);
                return getDefault();
            }
            if (this.type.isAssignableFrom(clazz)) {
                service = createInstance(clazz);
                services.putIfAbsent(serviceName, service);
            } else {
                throw new IllegalArgumentException("Load service type(" + this.type + ":" + serviceName + ") failed," + clazz + " is not " + this.type + "’s subclass");
            }
        }
        return service;
    }

    public S getDefault() {
        if (!StringUtil.isBlank(defaultService)) {
            if (services.size() > 1) {
                throw new IllegalArgumentException(this.type + " have Multi service Unknown Who is default load");
            }
        }
        return getService(defaultService);
    }

    public List<S> getServices() {
        ArrayList<S> list = new ArrayList<>();
        for (String key : serviceClasses.keySet()) {
            list.add(getService(key));
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    private S createInstance(Class<? extends S> clazz) {
        S service = ReflectUtil.createInstance(clazz);
        Set<LoadedListener<?>> listeners = LISTENERMAP.get(type);
        if (listeners != null) {
            for (LoadedListener<?> listener : listeners) {
                ((LoadedListener<S>) listener).listen(service);
            }
        }
        return service;
    }

}
