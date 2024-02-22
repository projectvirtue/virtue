package io.github.taikonaut3.virtue.boot.processor;

import io.github.taikonaut3.virtue.boot.VirtueRegistrationLifecycle;
import org.springframework.beans.BeansException;
import org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration;
import org.springframework.lang.NonNull;

public class ServiceRegistrationPostProcessor extends VirtueAdapterPostProcessor {

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof AbstractAutoServiceRegistration<?> autoServiceRegistration) {
            autoServiceRegistration.addRegistrationLifecycle(new VirtueRegistrationLifecycle<>(virtue()));
        }
        return bean;
    }

}
