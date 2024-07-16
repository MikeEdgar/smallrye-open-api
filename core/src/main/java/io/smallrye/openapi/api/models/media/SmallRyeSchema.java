package io.smallrye.openapi.api.models.media;

import static io.smallrye.openapi.runtime.io.schema.SchemaConstant.PROP_TYPE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.microprofile.openapi.models.ExternalDocumentation;
import org.eclipse.microprofile.openapi.models.media.Discriminator;
import org.eclipse.microprofile.openapi.models.media.Schema;
import org.eclipse.microprofile.openapi.models.media.XML;

import io.smallrye.openapi.api.models.ExternalDocumentationImpl;
import io.smallrye.openapi.api.util.MergeUtil;
import io.smallrye.openapi.api.util.VersionUtil;
import io.smallrye.openapi.runtime.util.ModelUtil;

public interface SmallRyeSchema extends Schema {

    public static SmallRyeSchema newInstance() {
        return newInstance(null);
    }

    public static SmallRyeSchema newInstance(String name) {
        if (VersionUtil.VER4) {
            return new SchemaImpl(name);
        }
        return Schema30Impl.newInstance(name);
    }

    public static String getName(Schema schema) {
        return schema instanceof SmallRyeSchema ? ((SmallRyeSchema) schema).getName() : null;
    }

    public static boolean isNamed(Schema schema) {
        return getName(schema) != null;
    }

    /**
     * Implements the old logic of setType(SchemaType).
     * <p>
     * {@link #setType(Schema, SchemaType)}, {@link #setNullable(Schema, Boolean)} and {@link #getNullable(Schema)} can
     * be used together allow the type and nullability of a schema to be set separately by different parts of the
     * scanning process, even though that information is now contained in one field.
     */
    public static void setType(Schema schema, SchemaType type) {
        if (!(schema instanceof SchemaImpl)) {
            return;
        }

        SchemaImpl s = (SchemaImpl) schema;
        List<SchemaType> currentValue = s.getListProperty(PROP_TYPE);
        if (currentValue != null && currentValue.contains(SchemaType.NULL)) {
            if (type == null) {
                s.setListProperty(PROP_TYPE, Arrays.asList(SchemaType.NULL));
            } else {
                s.setListProperty(PROP_TYPE, Arrays.asList(type, SchemaType.NULL));
            }
        } else {
            if (type == null) {
                s.setListProperty(PROP_TYPE, null);
            } else {
                s.setListProperty(PROP_TYPE, Collections.singletonList(type));
            }
        }

        if (s.typeObservers != null) {
            s.typeObservers.forEach(o -> setType(o, type));
        }
    }

    /**
     * Implements the old logic of getNullable().
     * <p>
     * {@link #setType(Schema, SchemaType)}, {@link #setNullable(Schema, Boolean)} and {@link #getNullable(Schema)} can
     * be used together allow the type and nullability of a schema to be set separately by different parts of the
     * scanning process, even though that information is now contained in one field.
     */
    public static Boolean getNullable(Schema schema) {
        List<SchemaType> types = schema.getType();

        if (!(schema instanceof SchemaImpl)) {
            return types != null && types.contains(SchemaType.NULL);
        }

        SchemaImpl s = (SchemaImpl) schema;

        if (types != null) {
            boolean nullPermitted = types.contains(SchemaType.NULL);
            // Retain old tri-state behaviour of getNullable
            // If setNullable has not been called and null is not permitted, return null rather than false
            if (!nullPermitted && s.nullable == null) {
                return null;
            } else {
                return nullPermitted;
            }
        } else {
            // If types is unset, return any value passed to setNullable
            return s.nullable;
        }
    }

    /**
     * Implements the old logic of setNullable(Boolean).
     * <p>
     * {@link #setType(Schema, SchemaType)}, {@link #setNullable(Schema, Boolean)} and {@link #getNullable(Schema)} can
     * be used together allow the type and nullability of a schema to be set separately by different parts of the
     * scanning process, even though that information is now contained in one field.
     */
    public static void setNullable(Schema schema, Boolean nullable) {
        if (!(schema instanceof SchemaImpl)) {
            return;
        }

        SchemaImpl s = (SchemaImpl) schema;

        s.incrementModCount();
        s.nullable = nullable;
        if (nullable == Boolean.TRUE) {
            List<SchemaType> types = s.getType();
            if (types == null || !types.contains(SchemaType.NULL)) {
                s.addType(SchemaType.NULL);
            }
        } else {
            s.removeType(SchemaType.NULL);
        }
    }

    public static int getModCount(Schema schema) {
        return schema instanceof SchemaImpl ? ((SchemaImpl) schema).modCount : -1;
    }

    public static void addTypeObserver(Schema observable, Schema observer) {
        if (observable instanceof SchemaImpl) {
            SchemaImpl obs = (SchemaImpl) observable;
            obs.typeObservers = ModelUtil.add(observer, obs.typeObservers, ArrayList<Schema>::new);
        }

        setTypeRetainingNull(observer, observable.getType());
    }

    public static SchemaImpl copyOf(Schema other) {
        if (other == null) {
            return new SchemaImpl();
        }
        if (other instanceof SchemaImpl) {
            SchemaImpl otherImpl = (SchemaImpl) other;
            SchemaImpl result = new SchemaImpl();
            result.getDataMap().putAll(copyOf(otherImpl.getDataMap()));
            return result;
        }
        throw new UnsupportedOperationException("Can't copy a different impl");
    }

    private static <K, V> Map<K, V> copyOf(Map<K, V> map) {
        Map<K, V> clone = new HashMap<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            K key = entry.getKey();
            V val = entry.getValue();
            clone.put(key, copyOf(val));
        }
        return clone;
    }

    private static <T> List<T> copyOf(List<T> list) {
        List<T> clone = new ArrayList<>();
        for (T value : list) {
            clone.add(copyOf(value));
        }
        return clone;
    }

    @SuppressWarnings("unchecked")
    private static <T> T copyOf(T value) {
        if (value instanceof Map) {
            return (T) copyOf((Map<?, ?>) value);
        } else if (value instanceof List) {
            return (T) copyOf((List<?>) value);
        } else if (value instanceof Schema) {
            return (T) copyOf((Schema) value);
        } else if (value instanceof XML) {
            return (T) MergeUtil.mergeObjects(new XMLImpl(), (XML) value);
        } else if (value instanceof ExternalDocumentation) {
            return (T) MergeUtil.mergeObjects(new ExternalDocumentationImpl(), (ExternalDocumentation) value);
        } else if (value instanceof Discriminator) {
            return (T) MergeUtil.mergeObjects(new DiscriminatorImpl(), (Discriminator) value);
        } else {
            return value;
        }
    }

    public static void setTypeRetainingNull(Schema target, List<SchemaType> types) {
        // Set types on the observer, but retain null if it was set on the observer
        List<SchemaType> oldTypes = target.getType();
        if (oldTypes != null && types != null
                && oldTypes.contains(SchemaType.NULL)
                && !types.contains(SchemaType.NULL)) {
            types = new ArrayList<>(types);
            types.add(SchemaType.NULL);
        }
        target.setType(types);
    }

    public static void clear(Schema schema) {
        SchemaImpl impl = (SchemaImpl) schema;
        impl.getDataMap().clear();
        impl.booleanValue = null;
    }

    Map<String, Object> getDataMap();

    SmallRyeSchema type(SchemaType type);

    List<SchemaType> getType();

    String getName();

}
