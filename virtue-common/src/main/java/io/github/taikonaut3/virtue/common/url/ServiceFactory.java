package io.github.taikonaut3.virtue.common.url;

/**
 * Factory for the SPI instance created from the Url configuration
 */
public interface ServiceFactory<T> {

    /**
     * Get the instance by the url.
     *
     * @param url
     * @return instance
     */
    T get(URL url);
}