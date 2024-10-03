package io.smallrye.openapi.model.apt;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Generated;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import org.eclipse.microprofile.openapi.OASFilter;
import org.eclipse.microprofile.openapi.models.ExternalDocumentation;

@SupportedAnnotationTypes({
        "io.smallrye.openapi.model.apt.OASModelType",
        "io.smallrye.openapi.model.apt.OASModelType.List" })
@SupportedSourceVersion(javax.lang.model.SourceVersion.RELEASE_11)
public class SmallRyeModelProcessor extends AbstractProcessor {

    private static final String INDENT = "    ";
    private static final String PUBLIC = "public ";
    private static final String NEW_VALUE_ARG = "newValue";
    private static final String RETURN_THIS = INDENT.repeat(2) + "return this;\n";

    private static final Map<String, Class<?>> CENTRALIZED_PROPERIES = Map.ofEntries(
            Map.entry("name", String.class),
            Map.entry("ref", String.class),
            Map.entry("description", String.class),
            Map.entry("summary", String.class),
            Map.entry("externalDocs", ExternalDocumentation.class));

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {

            for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                // Process each element
                processPackage(annotation, element);
            }
        }

        return true; // No further processing of this annotation type
    }

    private void processPackage(TypeElement annotation, Element element) {
        String annotationName = annotation.getQualifiedName().toString();
        PackageElement pkg = (PackageElement) element;
        String packageName = pkg.getQualifiedName().toString();
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Processing: " + packageName);
        Filer filer = processingEnv.getFiler();

        for (var mirror : pkg.getAnnotationMirrors()) {
            if (annotationName.equals(OASModelType.class.getName())) {
                writeType(filer, packageName, toMap(mirror));
            } else if (annotationName.equals(OASModelType.List.class.getName().replace('$', '.'))) {
                @SuppressWarnings("unchecked")
                List<AnnotationValue> types = (List<AnnotationValue>) toMap(mirror).get("value");

                for (var type : types) {
                    writeType(filer, packageName, toMap((AnnotationMirror) type.getValue()));
                }
            }
        }
    }

    private Map<String, Object> toMap(AnnotationMirror annotation) {
        return processingEnv.getElementUtils()
                .getElementValuesWithDefaults(annotation)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().getSimpleName().toString(),
                        e -> e.getValue().getValue(),
                        (u1, u2) -> u2,
                        LinkedHashMap::new));
    }

    private void writeType(Filer filer, String packageName, Map<String, Object> annotation) {
        String className = (String) annotation.get("name");
        String fqcn = packageName + "." + className;
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Writing: " + fqcn);

        try {
            JavaFileObject fileObject = filer.createSourceFile(fqcn);

            try (Writer writer = fileObject.openWriter()) {
                writer.write("package ");
                writer.write(packageName);
                writer.write(";\n");
                writer.write('\n');

                @SuppressWarnings("unchecked")
                List<AnnotationValue> properties = (List<AnnotationValue>) annotation.get("properties");

                writeClassHeader(writer, annotation);

                Map<String, Map<String, Object>> propertyMap = properties.stream()
                        .map(AnnotationValue::getValue)
                        .map(AnnotationMirror.class::cast)
                        .map(this::toMap)
                        .filter(Predicate.not(this::isCentralizedProperty))
                        .collect(Collectors.toMap(
                                e -> e.get("name").toString(),
                                Function.identity(),
                                (u1, u2) -> u2,
                                LinkedHashMap::new));

                //                DeclaredType constructible = (DeclaredType) annotation.get("constructible");
                //
                //                Map<String, ExecutableElement> methods = ((TypeElement) constructible.asElement())
                //                        .getEnclosedElements()
                //                        .stream()
                //                        .filter(e -> e.getKind() == ElementKind.METHOD)
                //                        .map(ExecutableElement.class::cast)
                //                        .collect(Collectors.toMap(e -> e.getSimpleName().toString(), Function.identity()));

                propertyMap.values().forEach(property -> {
                    try {
                        writeProperty(writer, className, property);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });

                writer.write("}\n");
            }
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    "Failed to generate class: " + e.getMessage());
        }
    }

    private void writeClassHeader(Writer writer, Map<String, Object> modelAnnotation) throws IOException {
        writer.write('@');
        writer.write(Generated.class.getName());
        writer.write("(value = \"");
        writer.write(getClass().getName());
        writer.write("\", date = \"");
        writer.write(OffsetDateTime.now().toString());
        writer.write("\")\n");

        if (get(modelAnnotation, "incomplete", Boolean.class).booleanValue()) {
            writer.write("abstract ");
        } else {
            writer.write(PUBLIC);
        }

        writer.write("class ");
        writer.write(get(modelAnnotation, "name", String.class));

        String parent = get(modelAnnotation, "parent", TypeMirror.class).toString();
        DeclaredType constructible = (DeclaredType) modelAnnotation.get("constructible");

        if (!Void.class.getName().equals(parent)) {
            writer.write('\n');
            writer.write(INDENT.repeat(2));
            writer.write("extends ");
            writer.write(parent);
            writer.write('<');
            writer.write(constructible.toString());
            writer.write('>');
        }

        writer.write('\n');
        writer.write(INDENT.repeat(2));
        writer.write("implements ");
        writer.write(constructible.toString());

        for (Object entry : get(modelAnnotation, "interfaces", List.class)) {
            AnnotationValue iface = (AnnotationValue) entry;
            writeInterface(writer, iface.getValue().toString());
        }

        writer.write(" {\n");

        try {
            Class<?> constType = Class.forName(constructible.toString());
            String simpleName = constructible.asElement().getSimpleName().toString();
            Method filterMethod = OASFilter.class.getMethod("filter" + simpleName, constType);

            if (filterMethod != null) {
                writeFilter(writer, filterMethod);
            }
        } catch (NoSuchMethodException | SecurityException | ClassNotFoundException e) {
            // Ignore errors
        }
    }

    private void writeInterface(Writer writer, String interfaceName) {
        try {
            writer.write(",\n");
            writer.write(INDENT.repeat(2));
            writer.write(interfaceName);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private boolean isCentralizedProperty(Map<String, Object> values) {
        String name = values.get("name").toString();
        String type = values.get("type").toString();
        return CENTRALIZED_PROPERIES.getOrDefault(name, Void.class).getName().equals(type);
    }

    private void writeProperty(Writer writer, String className, Map<String, Object> values)
            throws IOException {

        final String name = (String) values.get("name");
        final String rawType = ((TypeMirror) values.get("type")).toString();
        String type;
        String valueType;
        String getPropertyMethod;
        String setPropertyMethod;
        String singularName;
        boolean unwrapped = get(values, "unwrapped", Boolean.class).booleanValue();

        if (Map.class.getName().equals(rawType)) {
            valueType = getValueType(values, rawType);
            type = String.format("%s<String, %s>", rawType, valueType);
            getPropertyMethod = "getMapProperty";
            setPropertyMethod = "setMapProperty";
            singularName = get(values, "singularName", String.class);
            if (singularName.isBlank()) {
                if (name.endsWith("s")) {
                    singularName = name.substring(0, name.length() - 1);
                } else {
                    throw new IllegalArgumentException(
                            "singularName is required for Map type when name does not end with 's': " + name);
                }
            }
        } else if (List.class.getName().equals(rawType)) {
            if (unwrapped) {
                throw new IllegalArgumentException("unwrapped model types must have a property of type Map: " + name);
            }

            valueType = getValueType(values, rawType);
            type = String.format("%s<%s>", rawType, valueType);
            getPropertyMethod = "getListProperty";
            setPropertyMethod = "setListProperty";
            singularName = get(values, "singularName", String.class);
            if (singularName.isBlank()) {
                if (name.endsWith("s")) {
                    singularName = name.substring(0, name.length() - 1);
                } else {
                    throw new IllegalArgumentException(
                            "singularName is required for List type when name does not end with 's': " + name);
                }
            }
        } else {
            if (unwrapped) {
                throw new IllegalArgumentException("unwrapped model types must have a property of type Map: " + name);
            }

            valueType = null;
            type = rawType;
            getPropertyMethod = "getProperty";
            setPropertyMethod = "setProperty";
            singularName = null;
        }

        StringBuilder buffer = new StringBuilder();

        String methodName = getOptional(values, "methodNameOverride", String.class)
                .filter(Predicate.not(String::isBlank))
                .orElseGet(() -> {
                    buffer.append(name);
                    buffer.setCharAt(0,
                            Character.toUpperCase(name.charAt(0)));
                    return buffer.toString();
                });

        String singularMethodName = null;

        if (singularName != null) {
            buffer.setLength(0);
            buffer.append(singularName);
            buffer.setCharAt(0, Character.toUpperCase(singularName.charAt(0)));
            singularMethodName = buffer.toString();
        }

        //        Set<String> constructibleMethods = ((TypeElement) constructible.asElement()).getEnclosedElements()
        //                .stream()
        //                .filter(e -> e.getKind() == ElementKind.METHOD)
        //                .map(Element::getSimpleName)
        //                .map(Name::toString)
        //                .collect(Collectors.toSet());

        writeJavaDoc(writer);
        writeOverride(writer);

        writer.write(INDENT);
        writer.write(PUBLIC);
        writer.write(type);
        writer.write(" get");
        writer.write(methodName);
        writer.write("() {\n");

        if (unwrapped) {
            String valueTypeParam;

            if (valueType.indexOf('<') > -1) {
                valueTypeParam = "withTypeArguments(" + valueType.substring(0, valueType.indexOf('<')) + ".class)";
            } else {
                valueTypeParam = valueType + ".class";
            }

            writeCodeLn(writer, 2, "return getProperties(", valueTypeParam, ");");
        } else {
            writer.write(INDENT.repeat(2));
            writer.write("return ");
            writer.write(getPropertyMethod);
            writer.write("(\"");
            writer.write(name);
            writer.write("\");\n");
        }

        writer.write(INDENT);
        writer.write("}\n");

        writeJavaDoc(writer);
        writeOverride(writer);

        writeCode(writer, 1, "public void set", methodName);
        writeArgs(writer, type, NEW_VALUE_ARG);

        if (unwrapped) {
            writeCodeLn(writer, 2, "getProperties().keySet().forEach(this::remove", singularMethodName, ");");
            writeCodeLn(writer, 2, "if (", NEW_VALUE_ARG, " != null) {");
            writeCodeLn(writer, 3, NEW_VALUE_ARG, ".forEach(this::add", singularMethodName, ");");
            writeCodeLn(writer, 2, "}");
        } else {
            writeCodeLn(writer, 2, setPropertyMethod, "(\"", name, "\", newValue);");
        }

        writer.write(INDENT);
        writer.write("}\n");

        if (Map.class.getName().equals(rawType)) {
            writeJavaDoc(writer);
            writeOverride(writer);

            writer.write(INDENT);
            writer.write(PUBLIC);
            writer.write(className);
            writer.write(" add");
            writer.write(singularMethodName);
            writeArgs(writer, "String", "newKey", valueType, NEW_VALUE_ARG);
            writeCodeLn(writer, 2, "java.util.Objects.requireNonNull(newKey, \"Key must not be null\");");
            writeCodeLn(writer, 2, "java.util.Objects.requireNonNull(newValue, \"Value must not be null\");");

            if (unwrapped) {
                writeCodeLn(writer, 2, "setProperty(newKey, newValue);");
            } else {
                writeCodeLn(writer, 2, "putMapPropertyEntry(\"", name, "\", newKey, newValue);");
            }

            writer.write(RETURN_THIS);
            writer.write(INDENT);
            writer.write("}\n");

            writeJavaDoc(writer);
            writeOverride(writer);

            writer.write(INDENT);
            writer.write("public void remove");
            writer.write(singularMethodName);
            writeArgs(writer, "String", "key");

            if (unwrapped) {
                writer.write(INDENT.repeat(2));
                writer.write("setProperty(key, null);\n");
            } else {
                writer.write(INDENT.repeat(2));
                writer.write("removeMapPropertyEntry");
                writer.write("(\"");
                writer.write(name);
                writer.write("\", key);\n");
            }

            writer.write(INDENT);
            writer.write("}\n");
        } else if (List.class.getName().equals(rawType)) {
            writeJavaDoc(writer);
            writeOverride(writer);

            writer.write(INDENT);
            writer.write(PUBLIC);
            writer.write(className);
            writer.write(" add");
            writer.write(singularMethodName);
            writeArgs(writer, valueType, NEW_VALUE_ARG);
            writer.write(INDENT.repeat(2));
            writer.write("addListPropertyEntry");
            writer.write("(\"");
            writer.write(name);
            writer.write("\", newValue);\n");
            writer.write(RETURN_THIS);
            writer.write(INDENT);
            writer.write("}\n");

            writeJavaDoc(writer);
            writeOverride(writer);

            writer.write(INDENT);
            writer.write("public void remove");
            writer.write(singularMethodName);
            writeArgs(writer, valueType, "value");
            writer.write(INDENT.repeat(2));
            writer.write("removeListPropertyEntry");
            writer.write("(\"");
            writer.write(name);
            writer.write("\", value);\n");
            writer.write(INDENT);
            writer.write("}\n");
        }
    }

    private void writeArgs(Writer writer, String... args) throws IOException {
        if ((args.length & 1) != 0) { // implicit nullcheck of input
            throw new IllegalArgumentException("length is odd");
        }

        writer.write('(');

        for (int i = 0; i < args.length; i += 2) {
            if (i > 0) {
                writer.write(", ");
            }
            writer.write(args[i]);
            writer.write(' ');
            writer.write(args[i + 1]);
        }

        writer.write(") {\n");
    }

    private void writeCode(Writer writer, int indent, String... fragments) throws IOException {
        writer.write(INDENT.repeat(indent));
        for (String f : fragments) {
            writer.write(f);
        }
    }

    private void writeCodeLn(Writer writer, int indent, String... fragments) throws IOException {
        writeCode(writer, indent, fragments);
        writer.write('\n');
    }

    private void writeFilter(Writer writer, Method filterMethod) throws IOException {
        writeJavaDoc(writer);
        writeOverride(writer);
        writer.write(INDENT);
        writer.write("protected ");
        writer.write(filterMethod.getParameters()[0].getType().getName());
        writer.write(" filter(org.eclipse.microprofile.openapi.OASFilter filter) {\n");
        writer.write(INDENT.repeat(2));
        if (void.class.equals(filterMethod.getReturnType())) {
            writer.write("filter.filter");
        } else {
            writer.write("return filter.filter");
        }
        writer.write(filterMethod.getParameters()[0].getType().getSimpleName());
        writer.write("(this);\n");
        if (void.class.equals(filterMethod.getReturnType())) {
            writer.write(RETURN_THIS);
        }
        writer.write(INDENT);
        writer.write("}\n");
    }

    private String getValueType(Map<String, Object> values, String type) {
        return getOptional(values, "valueTypeLiteral", String.class).filter(Predicate.not(String::isBlank))
                .or(() -> getOptional(values, "valueType", TypeMirror.class)
                        .map(TypeMirror::toString)
                        .filter(Predicate.not(Void.class.getName()::equals)))
                .orElseThrow(() -> new NoSuchElementException("valueType required when type is " + type));
    }

    private <T> T get(Map<String, Object> values, String key, Class<T> type) {
        return getOptional(values, key, type).orElseThrow(() -> new NoSuchElementException(key));
    }

    private <T> Optional<T> getOptional(Map<String, Object> values, String key, Class<T> type) {
        return Optional.ofNullable(values.get(key)).map(type::cast);
    }

    private void writeJavaDoc(Writer writer) throws IOException {
        writer.write('\n');
        writer.write(INDENT);
        writer.write("/** {@inheritDoc} */\n");
    }

    private void writeOverride(Writer writer) throws IOException {
        writer.write(INDENT);
        writer.write("@Override\n");
    }
}
