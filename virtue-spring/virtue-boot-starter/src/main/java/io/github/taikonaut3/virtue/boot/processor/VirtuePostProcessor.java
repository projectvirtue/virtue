package io.github.taikonaut3.virtue.boot.processor;

import io.github.taikonaut3.virtue.boot.EnvironmentKey;
import io.github.taikonaut3.virtue.common.exception.SourceException;
import io.github.taikonaut3.virtue.config.manager.Virtue;
import org.springframework.beans.BeansException;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

public class VirtuePostProcessor extends VirtueAdapterPostProcessor {

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof Virtue virtue) {
            Environment environment = beanFactory.getBean(Environment.class);
            String app_name = environment.getProperty(EnvironmentKey.APPLICATION_NAME);
            if (!StringUtils.hasText(app_name)) {
                app_name = environment.getProperty(EnvironmentKey.SPRING_APPLICATION_NAME);
                if (!StringUtils.hasText(app_name)) {
                    throw new SourceException("缺少application-name");
                }
            }
            virtue.applicationName(app_name);
        }
        return bean;
    }

}
