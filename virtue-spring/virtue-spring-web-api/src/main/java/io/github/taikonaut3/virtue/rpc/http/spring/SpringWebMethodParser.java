package io.github.taikonaut3.virtue.rpc.http.spring;

import io.github.taikonaut3.virtue.common.util.StringUtil;
import io.github.taikonaut3.virtue.config.CallArgs;
import io.github.taikonaut3.virtue.rpc.http1_1.HttpParser;
import io.github.taikonaut3.virtue.rpc.http1_1.config.ContentType;
import io.github.taikonaut3.virtue.rpc.http1_1.config.HttpRequestWrapper;
import io.github.taikonaut3.virtue.rpc.http1_1.config.MethodParser;
import io.netty.handler.codec.http.HttpHeaderNames;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Support Spring Web API
 */
public class SpringWebMethodParser implements MethodParser<RequestMapping> {

    private static final List<Class<? extends Annotation>> SPRING_WEB_ANNOTATION = List.of(
            RequestMapping.class, GetMapping.class, PostMapping.class, PutMapping.class, DeleteMapping.class);

    private final HttpParser httpParser;

    public SpringWebMethodParser(HttpParser httpParser) {
        this.httpParser = httpParser;
    }

    @Override
    public Class<RequestMapping> support() {
        return RequestMapping.class;
    }

    @Override
    public HttpRequestWrapper parse(Method method) {
        Annotation webAnnotation = null;
        for (Class<? extends Annotation> type : SPRING_WEB_ANNOTATION) {
            webAnnotation = method.getAnnotation(type);
            if (webAnnotation != null) {
                break;
            }
        }
        if (webAnnotation == null) return null;
        HttpRequestWrapper wrapper = new HttpRequestWrapper();
        if (webAnnotation.annotationType() == RequestMapping.class) {
            RequestMapping mapping = (RequestMapping) webAnnotation;
            wrapper.httpMethod(mapping.method()[0].name());
            wrapper.headers(httpParser.parseHeaders(mapping.headers()));
            wrapper.params(httpParser.parseParams(mapping.params()));
            wrapper.path(mapping.value()[0]);
        } else if (webAnnotation.annotationType() == GetMapping.class) {
            GetMapping mapping = (GetMapping) webAnnotation;
            wrapper.path(mapping.value()[0]);
            wrapper.httpMethod(HttpMethod.GET.name());
            wrapper.headers(httpParser.parseHeaders(mapping.headers()));
            wrapper.params(httpParser.parseParams(mapping.params()));
        } else if (webAnnotation.annotationType() == PostMapping.class) {
            PostMapping mapping = (PostMapping) webAnnotation;
            wrapper.httpMethod(HttpMethod.POST.name());
            wrapper.path(mapping.value()[0]);
            wrapper.headers(httpParser.parseHeaders(mapping.headers()));
            wrapper.params(httpParser.parseParams(mapping.params()));
        } else if (webAnnotation.annotationType() == PutMapping.class) {
            PutMapping mapping = (PutMapping) webAnnotation;
            wrapper.httpMethod(HttpMethod.PUT.name());
            wrapper.path(mapping.value()[0]);
            wrapper.headers(httpParser.parseHeaders(mapping.headers()));
            wrapper.params(httpParser.parseParams(mapping.params()));
        } else if (webAnnotation.annotationType() == DeleteMapping.class) {
            DeleteMapping mapping = (DeleteMapping) webAnnotation;
            wrapper.httpMethod(HttpMethod.DELETE.name());
            wrapper.path(mapping.value()[0]);
            wrapper.headers(httpParser.parseHeaders(mapping.headers()));
            wrapper.params(httpParser.parseParams(mapping.params()));
        }
        return wrapper;
    }

    @Override
    public Map<String, String> parseParams(CallArgs args, HttpRequestWrapper wrapper) {
        Parameter[] parameters = args.caller().method().getParameters();
        HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            RequestParam param = parameter.getAnnotation(RequestParam.class);
            if (param != null) {
                String key = param.value();
                if (StringUtil.isBlank(key)) {
                    key = parameter.getName();
                }
                map.put(key, String.valueOf(args.args()[i]));
            }
        }
        wrapper.params().putAll(map);
        return wrapper.params();
    }

    @Override
    public String parsePathVariables(CallArgs args, HttpRequestWrapper wrapper) {
        String path = wrapper.path();
        Parameter[] parameters = args.caller().method().getParameters();
        if (StringUtil.isBlank(path)) {
            return path;
        }
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
            if (pathVariable != null) {
                String key = pathVariable.value();
                if (StringUtil.isBlank(key)) {
                    key = parameter.getName();
                }
                String replaceKey = "{" + key + "}";
                path = path.replace(replaceKey, String.valueOf(args.args()[i]));
            }
        }
        return path;
    }

    @Override
    public Object parseRequestBody(CallArgs args, HttpRequestWrapper wrapper) {
        List<Class<? extends Annotation>> executed = List.of(RequestParam.class, PathVariable.class);
        Parameter[] parameters = args.caller().method().getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Annotation[] annotations = parameter.getAnnotations();
            boolean isMatch = false;
            if (annotations.length == 0) {
                wrapper.headers().put(HttpHeaderNames.CONTENT_TYPE.toString(), ContentType.APPLICATION_X_WWW_FORM_URLENCODED);
                return args.args()[i];
            }
            List<? extends Class<? extends Annotation>> annotationTypes = Arrays.stream(annotations).map(Annotation::annotationType).toList();
            if (annotationTypes.contains(RequestBody.class)) {
                wrapper.headers().put(HttpHeaderNames.CONTENT_TYPE.toString(), ContentType.APPLICATION_JSON);
                return args.args()[i];
            }
            for (Annotation annotation : annotations) {
                if (!executed.contains(annotation.annotationType())) {
                    isMatch = true;
                    break;
                }
            }
            if (isMatch) {
                return args.args()[i];
            }
        }
        return null;
    }
}
