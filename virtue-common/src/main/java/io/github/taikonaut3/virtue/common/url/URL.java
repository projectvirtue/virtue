package io.github.taikonaut3.virtue.common.url;

import io.github.taikonaut3.virtue.common.constant.Key;
import io.github.taikonaut3.virtue.common.exception.SourceException;
import io.github.taikonaut3.virtue.common.extension.AbstractAccessor;
import io.github.taikonaut3.virtue.common.extension.AttributeKey;
import io.github.taikonaut3.virtue.common.util.AssertUtil;
import io.github.taikonaut3.virtue.common.util.NetUtil;
import io.github.taikonaut3.virtue.common.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Basic for:
 * 1、Rpc Request: {@link RemoteUrl}.
 * 2、Connect to Third Party Middleware.
 */
@Getter
@Accessors(fluent = true)
public class URL extends AbstractAccessor {

    public static final AttributeKey<URL> ATTRIBUTE_KEY = AttributeKey.get(Key.URL);

    private static final Logger logger = LoggerFactory.getLogger(URL.class);

    private final List<String> paths;

    private Map<String, String> parameters;

    @Setter
    protected String host;

    @Setter
    protected int port;

    @Setter
    protected String protocol;

    protected String address;

    public URL() {
        this.paths = new LinkedList<>();
        this.parameters = new HashMap<>();
    }

    public URL(String protocol, InetSocketAddress address) {
        this();
        AssertUtil.notNull(protocol, address);
        this.protocol = protocol;
        this.host = address.getHostString();
        this.port = address.getPort();
        this.address = NetUtil.getAddress(host, port);
    }

    public URL(String protocol, InetSocketAddress address, Map<String, String> parameters) {
        this(protocol, address);
        addParameters(parameters);
    }

    public URL(String protocol, String ip, int port) {
        this(protocol, new InetSocketAddress(ip, port));
    }

    public URL(String protocol, String ip, int port, Map<String, String> parameters) {
        this(protocol, new InetSocketAddress(ip, port), parameters);
    }

    public URL(String protocol, String address) {
        this();
        AssertUtil.notNull(protocol, address);
        this.protocol = protocol;
        address(address);
    }

    public URL(String protocol, String address, Map<String, String> parameters) {
        this(protocol, address);
        addParameters(parameters);

    }


    public static URL valueOf(String url) {
        if (url == null || url.trim().isEmpty()) {
            throw new SourceException("url is null");
        }
        String[] strings = url.split("\\?");
        String fixed = strings[0];
        int protocolStartIndex = fixed.lastIndexOf("://");
        String protocol = fixed.substring(0, protocolStartIndex);
        fixed = fixed.substring(protocolStartIndex + 3);
        String[] addressPath = fixed.split("/");
        String address = addressPath[0];
        URL urlObj = new URL(protocol, address);
        if (addressPath.length > 1) {
            for (int i = 1; i < addressPath.length; i++) {
                urlObj.addPath(addressPath[i]);
            }
        }
        if (strings.length > 1) {
            String params = strings[1];
            urlObj.addParameters(urlStringToMap(params));
        }
        return urlObj;
    }

    public static String mapToUrlString(Map<String, String> parameters) {
        return parameters.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
    }

    public static Map<String, String> urlStringToMap(String params) {
        return Arrays.stream(params.split("&"))
                .map(param -> param.split("="))
                .filter(keyValue -> keyValue.length == 2)
                .collect(Collectors.toMap(
                        split -> split[0],
                        split -> split[1]
                ));
    }

    public static String toPath(List<String> paths) {
        if (paths == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (String path : paths) {
            builder.append("/");
            builder.append(path);
        }
        return builder.toString();
    }

    public static List<String> pathToList(String path) {
        List<String> list = new ArrayList<>();
        if (StringUtil.isBlank(path)) {
            return list;
        }
        String[] parts = path.split("/");
        for (String part : parts) {
            if (!part.isEmpty()) {
                list.add(part);
            }
        }
        return list;
    }

    public URL address(String address) {
        this.address = address;
        if (NetUtil.isValidIpPort(address)) {
            InetSocketAddress socketAddress = NetUtil.toInetSocketAddress(address);
            this.host = socketAddress.getHostString();
            this.port = socketAddress.getPort();
        }
        return this;
    }

    public void addPath(String path) {
        if (StringUtil.isBlank(path)) {
            logger.warn("Add empty path, will skip");
        } else {
            path = StringUtil.normalizePath(path);
            paths.add(path);
        }
    }

    public void addPath(int index, String path) {
        if (StringUtil.isBlank(path)) {
            logger.warn("Add empty path, will skip");
        } else {
            path = StringUtil.normalizePath(path);
            paths.add(index, path);
        }
    }

    public void addPaths(List<String> paths) {
        paths.forEach(this::addPath);
    }

    public void removePath(int index) {
        if (paths.size() > index) {
            paths.remove(index);
        }
    }

    public String getPath(int index) {
        if (paths.size() > index) {
            return paths.get(index);
        }
        return null;
    }

    public void addParameter(String key, String value) {
        parameters.put(key, value);
    }

    public void addParameters(Map<String, String> parameters) {
        if (parameters != null) this.parameters.putAll(parameters);
    }

    public void replaceParameters(Map<String, String> parameters) {
        if (parameters != null && !parameters.isEmpty()) {
            this.parameters = parameters;
        }
    }

    public String getParameter(String key, String defaultValue) {
        return parameters.getOrDefault(key, defaultValue);
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public boolean getBooleanParameter(String key) {
        return Boolean.parseBoolean(getParameter(key));
    }

    public boolean getBooleanParameter(String key, boolean defaultValue) {
        String value = getParameter(key);
        return StringUtil.isBlank(value) ? defaultValue : Boolean.parseBoolean(value);
    }

    public int getIntParameter(String key) {
        return Integer.parseInt(getParameter(key));
    }

    public int getIntParameter(String key, int defaultValue) {
        String value = getParameter(key);
        return StringUtil.isBlank(value) ? defaultValue : Integer.parseInt(value);
    }

    public long getLongParameter(String key) {
        return Long.parseLong(getParameter(key));
    }

    public void removeParameter(String key) {
        parameters.remove(key);
    }

    public String authority() {
        return protocol + "://" + address;
    }

    public String uri() {
        return authority() + path();
    }

    public void replacePaths(List<String> paths) {
        if (paths != null && !paths.isEmpty()) {
            this.paths.clear();
            addPaths(paths);
        }
    }

    public String path() {
        return toPath(paths);

    }

    public String pathAndParams() {
        String params = mapToUrlString(parameters);
        return path() + (StringUtil.isBlank(params) ? "" : "?" + params);
    }

    public URL deepCopy() {
        URL url = new URL(protocol, address(), parameters);
        url.replacePaths(paths);
        url.accessor.putAll(this.accessor);
        return url;
    }

    @Override
    public String toString() {
        return authority() + pathAndParams();
    }

}
