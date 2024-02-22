package io.github.taikonaut3.virtue.boot;

import io.github.taikonaut3.virtue.common.constant.Key;
import io.github.taikonaut3.virtue.config.config.ApplicationConfig;
import io.github.taikonaut3.virtue.config.manager.Virtue;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.RegistrationLifecycle;

import static io.github.taikonaut3.virtue.common.constant.Components.Protocol.HTTP;

public class VirtueRegistrationLifecycle<R extends Registration> implements RegistrationLifecycle<R> {

    private final Virtue virtue;

    public VirtueRegistrationLifecycle(Virtue virtue) {
        this.virtue = virtue;
    }

    @Override
    public void postProcessBeforeStartRegister(R registration) {
        ApplicationConfig config = virtue.configManager().applicationConfig();
        registration.getMetadata().put(Key.PROTOCOL,HTTP);
        registration.getMetadata().put(Key.WEIGHT, String.valueOf(config.weight()));
    }

    @Override
    public void postProcessAfterStartRegister(R registration) {

    }

    @Override
    public void postProcessBeforeStopRegister(R registration) {

    }

    @Override
    public void postProcessAfterStopRegister(R registration) {

    }

}
