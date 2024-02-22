package io.github.taikonaut3.virtue.rpc.config;

import io.github.taikonaut3.virtue.common.constant.Components;
import io.github.taikonaut3.virtue.common.constant.Key;
import io.github.taikonaut3.virtue.common.exception.RpcException;
import io.github.taikonaut3.virtue.common.exception.SourceException;
import io.github.taikonaut3.virtue.common.extension.RpcContext;
import io.github.taikonaut3.virtue.common.spi.ExtensionLoader;
import io.github.taikonaut3.virtue.common.url.Parameter;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.common.util.CollectionUtil;
import io.github.taikonaut3.virtue.common.util.NetUtil;
import io.github.taikonaut3.virtue.common.util.ReflectUtil;
import io.github.taikonaut3.virtue.common.util.StringUtil;
import io.github.taikonaut3.virtue.config.CallArgs;
import io.github.taikonaut3.virtue.config.ClientCaller;
import io.github.taikonaut3.virtue.config.Invocation;
import io.github.taikonaut3.virtue.config.RemoteCaller;
import io.github.taikonaut3.virtue.config.annotation.Options;
import io.github.taikonaut3.virtue.config.config.ClientConfig;
import io.github.taikonaut3.virtue.config.config.RegistryConfig;
import io.github.taikonaut3.virtue.config.filter.Filter;
import io.github.taikonaut3.virtue.config.filter.FilterScope;
import io.github.taikonaut3.virtue.config.manager.ClientConfigManager;
import io.github.taikonaut3.virtue.config.manager.ConfigManager;
import io.github.taikonaut3.virtue.config.manager.Virtue;
import io.github.taikonaut3.virtue.governance.directory.Directory;
import io.github.taikonaut3.virtue.governance.faulttolerance.FaultTolerance;
import io.github.taikonaut3.virtue.governance.loadbalance.LoadBalance;
import io.github.taikonaut3.virtue.governance.router.Router;
import io.github.taikonaut3.virtue.registry.Registry;
import io.github.taikonaut3.virtue.registry.RegistryFactory;
import io.github.taikonaut3.virtue.rpc.RpcFuture;
import io.github.taikonaut3.virtue.transport.Request;
import io.github.taikonaut3.virtue.transport.client.Client;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static io.github.taikonaut3.virtue.common.constant.Components.DEFAULT;

@Setter
@Getter
@Accessors(fluent = true, chain = true)
public abstract class AbstractClientCaller<T extends Annotation> extends AbstractCaller<T> implements ClientCaller<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractClientCaller.class);

    protected String directUrl;

    protected Type returnType;

    @Parameter(Key.RETRIES)
    protected int retires;

    @Parameter(Key.ASYNC)
    protected boolean async;

    @Parameter(Key.LOAD_BALANCE)
    protected String loadBalance;

    @Parameter(Key.DIRECTORY)
    protected String directory;

    @Parameter(Key.ROUTER)
    protected String router;

    @Parameter(Key.FAULT_TOLERANCE)
    protected String faultTolerance;

    @Parameter(Key.TIMEOUT)
    protected int timeout;

    @Parameter(Key.ONEWAY)
    protected boolean oneWay;

    @Parameter(Key.MULTIPLEX)
    protected boolean multiplex;

    @Parameter(Key.CLIENT)
    protected String clientConfig;

    protected volatile List<RegistryConfig> registryConfigs;

    protected List<URL> registryConfigUrls;

    protected AbstractClientCaller(Method method, RemoteCaller<?> remoteCaller, String protocol, Class<T> annoType) {
        super(method, remoteCaller, protocol, annoType);
    }

    @Override
    public void init() {
        Options ops = getOptions();
        // check
        checkAsyncReturnType(method);
        checkDirectUrl(ops.url());
        // set
        router(ops.router());
        directUrl(ops.url());
        timeout(ops.timeout());
        retires(ops.retires());
        directory(ops.directory());
        loadBalance(ops.loadBalance());
        faultTolerance(ops.faultTolerance());
        oneWay(returnType().getTypeName().equals("void"));
        multiplex(ops.multiplex());
        clientConfig(ops.client());
        // subclass init
        doInit();
        // parse config
        ConfigManager configManager = virtue.configManager();
        for (RegistryConfig registryConfig : configManager.registryConfigManager().globalConfigs()) {
            addRegistryConfig(registryConfig);
        }
        String[] registryNames = getOptions().registries();
        Optional.ofNullable(registryNames).ifPresent(names -> Arrays.stream(names).map(configManager.registryConfigManager()::get).filter(Objects::nonNull).forEach(this::addRegistryConfig));
        ClientConfig clientConfig = checkAndGetClientConfig();
        url = createUrl(clientConfig.toUrl());
    }

    @Override
    public void start() {
        if (!remoteCaller().lazyDiscover()) {
            if(registryConfigs==null || registryConfigs.isEmpty()){
                logger.warn("Can't find registryConfig(s)");
            }else {
                for (URL registryUrl : registryConfigUrls) {
                    RegistryFactory registryFactory = ExtensionLoader.loadService(RegistryFactory.class, registryUrl.protocol());
                    Registry registry = registryFactory.get(registryUrl);
                    registry.discover(url);
                }
            }
        }
    }

    private Options getOptions() {
        if (method.isAnnotationPresent(Options.class)) {
            return method.getAnnotation(Options.class);
        }
        if (options() != null) {
            return options();
        }
        return ReflectUtil.getDefaultInstance(Options.class);
    }

    @Override
    public void addRegistryConfig(RegistryConfig... configs) {
        if (registryConfigs == null) {
            synchronized (this) {
                if (registryConfigs == null) {
                    registryConfigs = new LinkedList<>();
                    registryConfigUrls = new LinkedList<>();
                }
            }
        }
        CollectionUtil.addToList(this.registryConfigs,
                (registryConfig, config) ->
                        Objects.equals(config.type(), registryConfig.type())
                                && config.host().equals(registryConfig.host())
                                && config.port() == registryConfig.port(),
                config -> {
                    URL configUrl = config.toUrl();
                    configUrl.attribute(Virtue.ATTRIBUTE_KEY).set(virtue);
                    configUrl.addParameter(Key.VIRTUE, virtue.name());
                    registryConfigUrls.add(configUrl);
                }, configs);

    }

    @Override
    public Object call(URL url, CallArgs args) throws RpcException {
        Object result = null;
        try {
            List<Filter> preFilters = FilterScope.PRE.filterScope(filters);
            Invocation invocation = Invocation.create(url, args, this::governanceCall);
            result = filterChain.filter(invocation, preFilters);
        } catch (RpcException e) {
            logger.error("Remote Call Exception " + this, e);
        } finally {
            RpcContext.getContext().clear();
        }
        return result;
    }

    @Override
    public Object call(Invocation invocation) throws RpcException {
        try {
            List<Filter> postFilters = FilterScope.POST.filterScope(filters);
            invocation.revise(this::directRemoteCall);
            RpcFuture future = (RpcFuture) filterChain.filter(invocation, postFilters);
            return async() ? future : future.get();
        } catch (Exception e) {
            throw new RpcException(e);
        }
    }

    @Override
    public Type returnType() {
            return returnType;
    }

    @Override
    public Class<?> returnClass() {
        Class<?> returnType = method.getReturnType();
        if (async()) {
            return (Class<?>) ((ParameterizedType) returnType()).getRawType();
        } else {
            return returnType;
        }
    }

    @Override
    public RemoteCaller<?> remoteCaller() {
        return (RemoteCaller<?>) container();
    }

    @Override
    protected URL createUrl(URL clientUrl) {
        String address = remoteApplication;
        if (!StringUtil.isBlank(directUrl)) {
            address = directUrl;
        }
        URL url = new URL(protocol, address);
        url.replacePaths(pathList());
        url.addParameters(clientUrl.parameters());
        url.addParameters(parameterization());
        return url;
    }

    protected Object governanceCall(Invocation invocation) {
        try {
            URL url = invocation.url();
            ClientCaller<?> caller = (ClientCaller<?>) invocation.callArgs().caller();
            if (StringUtil.isBlank(caller.directUrl())) {
                // Get available service urls
                String directoryKey = url.getParameter(Key.DIRECTORY, DEFAULT);
                Directory directory = ExtensionLoader.loadService(Directory.class, directoryKey);
                URL[] registryConfigUrls = Optional.ofNullable(caller.registryConfigUrls()).stream()
                        .flatMap(Collection::stream)
                        .toArray(URL[]::new);
                List<URL> availableServiceUrls = directory.list(invocation, registryConfigUrls);
                if (availableServiceUrls.isEmpty()) {
                    throw new SourceException("Not found available service!,Path:" + url.path());
                }
                // Router
                Router router = virtue.attribute(Router.ATTRIBUTE_KEY).get();
                if (router == null) {
                    String routerKey = virtue.configManager().applicationConfig().router();
                    router = ExtensionLoader.loadService(Router.class, routerKey);
                }
                List<URL> finalServiceUrls = router.route(invocation, availableServiceUrls);
                // LoadBalance
                String loadBalanceKey = url.getParameter(Key.LOAD_BALANCE, Components.LoadBalance.RANDOM);
                LoadBalance loadBalance = ExtensionLoader.loadService(LoadBalance.class, loadBalanceKey);
                URL selectedServiceUrl = loadBalance.select(invocation, finalServiceUrls);
                url.address(selectedServiceUrl.address()).addParameters(selectedServiceUrl.parameters());
            }
            // Invocation revise
            invocation.revise(caller::call);
            // faultTolerance call
            String faultToleranceKey = url.getParameter(Key.FAULT_TOLERANCE, Components.FaultTolerance.FAIL_FAST);
            FaultTolerance faultTolerance = ExtensionLoader.loadService(FaultTolerance.class, faultToleranceKey);
            return faultTolerance.operation(invocation);
        } catch (RpcException e) {
            throw new RpcException(e);
        }
    }

    protected RpcFuture directRemoteCall(Invocation invocation) {
        URL url = invocation.url();
        CallArgs callArgs = invocation.callArgs();
        Client client = getClient(url.address());
        Object message = protocolInstance.createRequest(url, callArgs);
        Request request = new Request(url, message);
        RpcFuture future = new RpcFuture(url, callArgs);
        future.client(client);
        // request
        client.send(request);
        return future;
    }

    private Client getClient(String address) {
        ClientConfigManager clientConfigManager = virtue.configManager().clientConfigManager();
        ClientConfig clientConfig = clientConfigManager.get(this.clientConfig);
        URL clientUrl = new URL(protocol, address);
        if (clientConfig == null) {
            clientConfig = clientConfigManager.get(protocol);
        }
        clientUrl.addParameters(clientConfig.parameterization());
        clientUrl.addParameter(Key.MULTIPLEX, String.valueOf(multiplex));
        return protocolInstance.openClient(clientUrl);
    }

    private void checkAsyncReturnType(Method method) {
        returnType = method.getGenericReturnType();
            if (returnType instanceof ParameterizedType parameterizedType) {
                if (parameterizedType.getRawType() == CompletableFuture.class) {
                    returnType = parameterizedType.getActualTypeArguments()[0];
                    async(true);
            }
        }
    }

    private void checkDirectUrl(String url) {
        if (!StringUtil.isBlank(url)) {
            try {
                NetUtil.toInetSocketAddress(url);
            } catch (IllegalArgumentException e) {
                logger.error("Illegal direct connection address", e);
                throw new IllegalArgumentException(e);
            }
        }
    }

    private ClientConfig checkAndGetClientConfig() {
        ClientConfigManager clientConfigManager = virtue.configManager().clientConfigManager();
        ClientConfig clientConfig = clientConfigManager.get(this.clientConfig);
        if (clientConfig == null) {
            clientConfig = clientConfigManager.get(protocol);
        }
        if (clientConfig == null) {
            clientConfig = new ClientConfig(protocol);
            clientConfigManager.register(clientConfig);
        }
        return clientConfig;
    }

    protected Options options() {
        return null;
    }
}
