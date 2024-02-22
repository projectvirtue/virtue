package io.github.taikonaut3.virtue.rpc.virtue;

import io.github.taikonaut3.virtue.common.util.StringUtil;
import io.github.taikonaut3.virtue.config.RemoteService;
import io.github.taikonaut3.virtue.config.annotation.Config;
import io.github.taikonaut3.virtue.common.util.GenerateUtil;
import io.github.taikonaut3.virtue.rpc.config.AbstractServerCaller;
import io.github.taikonaut3.virtue.rpc.virtue.config.VirtueCallable;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;

import static io.github.taikonaut3.virtue.common.constant.Components.Protocol.VIRTUE;

@ToString
public class VirtueServerCaller extends AbstractServerCaller<VirtueCallable> {

    private static final Logger logger = LoggerFactory.getLogger(VirtueServerCaller.class);

    private String remoteService;

    private String callMethod;

    public VirtueServerCaller(Method method, RemoteService<?> remoteService) {
        super(method, remoteService, VIRTUE, VirtueCallable.class);
    }

    @Override
    public void doInit() {
        this.remoteService = remoteService().name();
        this.callMethod = StringUtil.isBlank(parsedAnnotation.name()) ? GenerateUtil.generateKey(method()) : parsedAnnotation.name();
    }

    @Override
    protected Config config() {
        return parsedAnnotation.config();
    }

    @Override
    public List<String> pathList() {
        return List.of(remoteService, callMethod);
    }
}
