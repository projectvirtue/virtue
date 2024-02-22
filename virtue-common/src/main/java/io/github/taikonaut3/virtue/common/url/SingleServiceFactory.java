package io.github.taikonaut3.virtue.common.url;

/**
 * Get a singleton service.
 *
 * @param <T>
 */
public abstract class SingleServiceFactory<T> implements ServiceFactory<T> {
    private volatile T service;

    @Override
    public T get(URL url) {
        if (service == null) {
            synchronized (this) {
                if (service == null) {
                    service = create(url);
                }
            }
        }
        return service;
    }

    protected abstract T create(URL url);
}
