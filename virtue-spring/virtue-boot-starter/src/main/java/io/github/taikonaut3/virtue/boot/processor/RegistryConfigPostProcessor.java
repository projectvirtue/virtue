package io.github.taikonaut3.virtue.boot.processor;

import io.github.taikonaut3.virtue.config.config.RegistryConfig;
import org.springframework.beans.BeansException;
import org.springframework.lang.NonNull;

public class RegistryConfigPostProcessor extends VirtueAdapterPostProcessor {


    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof RegistryConfig config) {
            config.name(beanName);
            virtue().register(config);
        }
        return bean;
    }

}
