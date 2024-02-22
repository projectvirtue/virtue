package io.github.taikonaut3.virtue.common.spi;

import io.github.taikonaut3.virtue.common.exception.RpcException;
import io.github.taikonaut3.virtue.common.util.FileUtil;
import lombok.Getter;
import lombok.experimental.Accessors;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static io.github.taikonaut3.virtue.common.constant.Constant.SPI_FIX_PATH;

/**
 * Extend JDK SPI.
 *
 * @see ServiceInterface
 * @see ServiceProvider
 */
@SupportedSourceVersion(SourceVersion.RELEASE_21)
@SupportedAnnotationTypes({
        "io.github.taikonaut3.virtue.common.spi.ServiceInterface",
        "io.github.taikonaut3.virtue.common.spi.ServiceProvider"
})
public class ExtensionLoaderAnnotationProcessor extends AbstractProcessor {

    private Map<String, File> fileMap;

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> serviceInterfaceElements = roundEnvironment.getElementsAnnotatedWith(ServiceProvider.class);
        for (Element element : serviceInterfaceElements) {
            if (element instanceof TypeElement typeElement) {
                List<? extends AnnotationMirror> annotationMirrors = typeElement.getAnnotationMirrors().stream().filter(annotationMirror -> annotationMirror.getAnnotationType().toString().equals("io.github.taikonaut3.virtue.common.spi.ServiceProvider")).toList();
                ServiceProviderWrapper wrapper = new ServiceProviderWrapper(annotationMirrors.get(0));
                List<String> allInterfaces = getAllInterfaces(typeElement).stream().map(typeMirror -> getTypeElement(typeMirror).getQualifiedName().toString()).toList();
                if (wrapper.interfaces().isEmpty()) {
                    for (String interfaceType : allInterfaces) {
                        String path = SPI_FIX_PATH + interfaceType;
                        String content = typeElement.getQualifiedName().toString();
                        writeServiceFile(path, content);
                    }
                } else {
                    List<String> interfaces = wrapper.interfaces();
                    for (String interfaceType : interfaces) {
                        if (allInterfaces.contains(interfaceType)) {
                            String path = SPI_FIX_PATH + interfaceType;
                            String content = typeElement.getQualifiedName().toString();
                            writeServiceFile(path, content);
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        fileMap = new HashMap<>();
        super.init(processingEnv);
    }

    private File creatrFile(String path) {
        FileObject fileObject;
        try {
            fileObject = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", path);
        } catch (IOException e) {
            throw new RpcException("Create File fail path: " + path, e);
        }
        return new File(fileObject.toUri());
    }

    private void writeServiceFile(String path, String content) {
        File file = fileMap.get(path);
        if (file == null) {
            file = creatrFile(path);
            fileMap.put(path, file);
        }
        FileUtil.writeLineFile(content, file);
    }

    private List<TypeMirror> getAllInterfaces(TypeElement typeElement) {
        List<? extends TypeMirror> typeMirrors = typeElement.getInterfaces().stream().filter(typeMirror -> {
            Element interfaceElement = processingEnv.getTypeUtils().asElement(typeMirror);
            ServiceInterface annotation = interfaceElement.getAnnotation(ServiceInterface.class);
            return annotation != null;
        }).toList();
        List<TypeMirror> interfaces = new ArrayList<>(typeMirrors);
        if (typeElement.getSuperclass() instanceof DeclaredType declaredType) {
            if (declaredType.asElement() instanceof TypeElement superClassElement) {
                interfaces.addAll(getAllInterfaces(superClassElement));
            }
        }
        return interfaces;
    }

    private  TypeElement getTypeElement(TypeMirror typeMirror) {
        return (TypeElement) processingEnv.getTypeUtils().asElement(typeMirror);
    }

    @Getter
    @Accessors(fluent = true)
    private static class ServiceProviderWrapper {

        private String value;

        private final List<String> interfaces;

        public ServiceProviderWrapper(AnnotationMirror annotationMirror) {
            interfaces = new ArrayList<>();
            Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = annotationMirror.getElementValues();
            for (ExecutableElement key : elementValues.keySet()) {
                AnnotationValue annotationValue = elementValues.get(key);
                if (key.getSimpleName().toString().equals("interfaces")) {
                    interfaces.addAll(getPropertyTypes(annotationValue));
                } else if (key.getSimpleName().toString().equals("value")) {
                    value = (String) annotationValue.getValue();
                }
            }

        }
        private List<String> getPropertyTypes(AnnotationValue value) {
            List<String> propertyTypes = new ArrayList<>();
            @SuppressWarnings("unchecked")
            List<AnnotationValue> values = (List<AnnotationValue>) value.getValue();
            for (AnnotationValue annotationValue : values) {
                TypeMirror typeMirror = (TypeMirror) annotationValue.getValue();
                String propertyType = typeMirror.toString();
                propertyTypes.add(propertyType);
            }
            return propertyTypes;
        }

    }

}

