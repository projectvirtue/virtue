package io.github.taikonaut3.virtue.rpc.http.spring;

import io.github.taikonaut3.virtue.common.spi.ExtensionLoader;
import io.github.taikonaut3.virtue.common.spi.ServiceProvider;
import io.github.taikonaut3.virtue.config.VirtueConfiguration;
import io.github.taikonaut3.virtue.config.manager.Virtue;
import io.github.taikonaut3.virtue.rpc.http1_1.HttpParser;
import io.github.taikonaut3.virtue.rpc.http1_1.HttpProtocol;
import io.github.taikonaut3.virtue.rpc.protocol.Protocol;

@ServiceProvider("springWebMethodParserRegister")
public class SpringWebMethodParserRegister implements VirtueConfiguration {

    @Override
    public void initBefore(Virtue virtue) {
        ExtensionLoader.addListener(Protocol.class, protocol -> {
            if (protocol instanceof HttpProtocol) {
                HttpParser httpParser = (HttpParser) protocol.parser();
                httpParser.methodParser(new SpringWebMethodParser(httpParser));
            }
        });
    }
}
