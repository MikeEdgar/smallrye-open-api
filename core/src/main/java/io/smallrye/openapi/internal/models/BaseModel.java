package io.smallrye.openapi.internal.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.eclipse.microprofile.openapi.OASFactory;
import org.eclipse.microprofile.openapi.OASFilter;
import org.eclipse.microprofile.openapi.models.Constructible;
import org.eclipse.microprofile.openapi.models.Reference;

import io.smallrye.openapi.api.models.ModelImpl;
import io.smallrye.openapi.api.util.MergeUtil;
import io.smallrye.openapi.runtime.io.ReferenceType;
import io.smallrye.openapi.runtime.util.ModelUtil;

public abstract class BaseModel<C extends Constructible> implements ModelImpl {

    protected enum MergeDirective {
        PRESERVE_VALUE,
        OVERRIDE_VALUE,
        MERGE_VALUES
    }

    protected final Map<String, Object> properties = new LinkedHashMap<>(2);
    private int modCount;
    private int hash = 0;

    @SuppressWarnings("unchecked")
    protected static <T> Class<T> withTypeArguments(Class<?> clazz) {
        return (Class<T>) clazz;
    }

    @SuppressWarnings("unchecked")
    public static <O extends Constructible> O deepCopy(O other, Class<O> type) {
        BaseModel<O> result = (BaseModel<O>) OASFactory.createObject(type);

        if (other instanceof BaseModel) {
            BaseModel<O> otherImpl = (BaseModel<O>) other;
            result.properties.putAll(deepCopy(otherImpl.properties));
        } else if (other != null) {
            //TODO generated "load" methods on BaseModel implementations
            throw new UnsupportedOperationException("Only BaseModel types may be copied: " + other.getClass());
        }

        return (O) result;
    }

    private static <K, V> Map<K, V> deepCopy(Map<K, V> map) {
        Map<K, V> clone = new LinkedHashMap<>(map.size());

        for (Map.Entry<K, V> entry : map.entrySet()) {
            clone.put(entry.getKey(), deepCopy(entry.getValue()));
        }

        return clone;
    }

    private static <T> List<T> deepCopy(List<T> list) {
        List<T> clone = new ArrayList<>(list.size());

        for (T value : list) {
            clone.add(deepCopy(value));
        }

        return clone;
    }

    @SuppressWarnings("unchecked")
    private static <T, N extends Constructible> T deepCopy(T value) {
        if (value instanceof Map) {
            return (T) deepCopy((Map<?, ?>) value);
        } else if (value instanceof List) {
            return (T) deepCopy((List<?>) value);
        } else if (value instanceof BaseModel) {
            N nested = (N) value;
            Class<N> nestedType = (Class<N>) nested.getClass();
            return (T) deepCopy(nested, findConstructible(nestedType));
        } else if (value instanceof Constructible) {
            Class<N> nestedType = (Class<N>) value.getClass();
            return (T) MergeUtil.mergeObjects(OASFactory.createObject(nestedType), value);
        } else {
            return value;
        }
    }

    @SuppressWarnings("unchecked")
    private static <C extends Constructible> Class<C> findConstructible(Class<?> type) {
        for (Class<?> i : type.getInterfaces()) {
            if (Constructible.class.equals(i)) {
                return (Class<C>) type;
            }

            Class<C> result = findConstructible(i);

            if (result != null) {
                return result;
            }
        }

        Class<?> parent = type.getSuperclass();

        if (parent == null && type.isInterface()) {
            return null;
        }

        if (parent == null || Object.class.equals(parent)) {
            throw new IllegalStateException("Failed to find direct Constructible interface: " + type);
        }

        return findConstructible(parent);
    }

    protected BaseModel() {
    }

    protected void incrementModCount() {
        modCount++;
    }

    public int getModCount() {
        return modCount;
    }

    @Override
    public String toString() {
        return String.valueOf(properties);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof BaseModel) {
            return Objects.equals(properties, ((BaseModel<?>) obj).properties);
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (hash != 0) {
            return hash;
        }

        final Map<Object, Object> stack = new IdentityHashMap<>();
        final int result = hash(stack, this);

        hash = result;
        return result;
    }

    private static int hash(Map<Object, Object> stack, BaseModel<?> model) {
        int result = 0;

        if (!stack.containsKey(model)) {
            stack.put(model, model);

            for (Map.Entry<String, Object> e : model.properties.entrySet()) {
                result = 31 * result + (e == null ? 0 : hash(stack, e));
            }

            stack.remove(model);
        }

        return result;
    }

    private static int hash(Map<Object, Object> stack, Map.Entry<String, Object> entry) {
        int result = entry.getKey().hashCode();
        result = 31 * result + hash(stack, entry.getValue());
        return result;
    }

    private static int hash(Map<Object, Object> stack, Object value) {
        int result = 0;

        if (value instanceof BaseModel) {
            result = hash(stack, (BaseModel<?>) value);
        } else if (value instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) value;
            for (Map.Entry<String, Object> e : map.entrySet()) {
                result = 31 * result + (e == null ? 0 : hash(stack, e));
            }
        } else if (value instanceof List) {
            List<?> list = (List<?>) value;
            for (Object e : list) {
                result = 31 * result + (e == null ? 0 : hash(stack, e));
            }
        } else {
            result = Objects.hash(value);
        }

        return result;
    }

    public C filter(OASFilter filter, Map<Object, Object> stack) {
        Iterator<Map.Entry<String, Object>> cursor = properties.entrySet().iterator();

        while (cursor.hasNext()) {
            Map.Entry<String, Object> entry = cursor.next();
            Object value = entry.getValue();

            if (value instanceof BaseModel && !stack.containsKey(value)) { // NOSONAR
                stack.put(value, value);

                Object replacement = ((BaseModel<?>) value).filter(filter, stack);

                if (replacement != null) {
                    entry.setValue(replacement);
                } else {
                    cursor.remove();
                    incrementModCount();
                }

                stack.remove(value);
            }
        }

        // TODO: check if instance of BaseModel<C> and convert if not.
        return filter(filter);
    }

    /**
     * Apply the provided filter to this instance. This should be overridden by subclasses
     * that have a relevant filter method defined in {@link OASFilter}.
     *
     * Note, this method may return a different instance or null, depending on the return
     * from the filter.
     *
     * @param filter the {@link OASFilter} filter to apply to the instance
     * @return
     */
    @SuppressWarnings("unchecked")
    protected C filter(OASFilter filter) {
        return (C) this;
    }

    protected boolean isExtension(String name) {
        return false;
    }

    /**
     * Merge all properties from another {@link BaseModel} object into this one.
     *
     * @param other the other {@link BaseModel} object
     */
    public void merge(BaseModel<C> other) {
        for (Entry<String, Object> entry : other.properties.entrySet()) {
            String name = entry.getKey();
            Object newValue = entry.getValue();
            Object oldValue = properties.get(entry.getKey());
            MergeDirective mergeDirective = mergeDirective(name);

            if (oldValue != null && mergeDirective == MergeDirective.PRESERVE_VALUE) {
                // Leave the old value as-is if it is present
            } else if (oldValue == null || oldValue.getClass() != newValue.getClass()
                    || mergeDirective == MergeDirective.OVERRIDE_VALUE) {
                properties.put(name, newValue);
            } else if (oldValue instanceof ModelImpl) {
                properties.put(name, MergeUtil.mergeObjects(oldValue, newValue));
            } else if (oldValue instanceof Map) {
                properties.put(name, MergeUtil.mergeObjects(oldValue, newValue));
            } else if (oldValue instanceof List) {
                properties.put(name, MergeUtil.mergeObjects(oldValue, newValue));
            } else {
                properties.put(name, newValue);
            }
        }
    }

    /**
     * Determine how the property indicated by name should be merged.
     * @param name property name
     */
    protected MergeDirective mergeDirective(String name) {
        return MergeDirective.MERGE_VALUES;
    }

    /**
     * Returns a read-only view of all properties.
     */
    public Map<String, ?> getAllProperties() {
        return Collections.unmodifiableMap(properties);
    }

    public void setAllProperties(Map<String, ?> allProperties) {
        properties.clear();

        if (allProperties != null) {
            allProperties.forEach(this::setProperty);
        }
    }

    /**
     * Returns a read-only copy of all properties, excluding extensions.
     */
    protected Map<String, ?> getProperties() {
        Map<String, Object> result = new LinkedHashMap<>(properties);
        result.keySet().removeIf(this::isExtension);
        return Collections.unmodifiableMap(result);
    }

    /**
     * Returns a read-only copy of all properties having type T, excluding extensions.
     */
    @SuppressWarnings("unchecked")
    protected <T> Map<String, T> getProperties(Class<T> type) {
        Map<String, T> result = new LinkedHashMap<>(properties.size());

        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            if (type.isInstance(entry.getValue()) && !isExtension(entry.getKey())) {
                result.put(entry.getKey(), (T) entry.getValue());
            }
        }

        return Collections.unmodifiableMap(result);
    }

    @SuppressWarnings("unchecked")
    protected <T> T getProperty(String name) {
        return (T) properties.get(name);
    }

    protected <T> T getProperty(String name, Class<T> type) {
        Object value = getProperty(name);

        if (type.isInstance(value)) {
            return type.cast(value);
        } else {
            return null;
        }
    }

    public <T> void setProperty(String name, T value) {
        if (value == null) {
            properties.remove(name);
        } else {
            properties.put(name, value);
        }
        incrementModCount();
    }

    @SuppressWarnings("unchecked")
    protected <T> List<T> getListProperty(String name) {
        Object value = getProperty(name);

        if (value instanceof List) {
            return Collections.unmodifiableList((List<T>) value);
        } else {
            return null; // NOSONAR
        }
    }

    protected <T> void setListProperty(String name, List<T> value) {
        value = ModelUtil.replace(value, ArrayList::new);
        setProperty(name, value);
    }

    @SuppressWarnings("unchecked")
    protected <T> void addListPropertyEntry(String name, T value) {
        if (value != null) {
            List<T> list = getProperty(name, List.class);
            list = ModelUtil.add(value, list, () -> new ArrayList<T>(2));
            setProperty(name, list);
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> void removeListPropertyEntry(String name, T value) {
        List<T> list = getProperty(name, List.class);
        ModelUtil.remove(list, value);
        incrementModCount();
    }

    @SuppressWarnings("unchecked")
    protected <T> Map<String, T> getMapProperty(String name) {
        Object value = getProperty(name);

        if (value instanceof Map) {
            return Collections.unmodifiableMap((Map<String, T>) value);
        } else {
            return null; // NOSONAR
        }
    }

    protected <T> void setMapProperty(String name, Map<String, T> value) {
        value = ModelUtil.replace(value, LinkedHashMap::new);
        setProperty(name, value);
    }

    protected <V> void putMapPropertyEntry(String name, String key, V value) {
        if (value != null) {
            @SuppressWarnings("unchecked")
            Map<String, V> map = getProperty(name, Map.class);
            map = ModelUtil.add(key, value, map, () -> new LinkedHashMap<String, V>(2));
            setProperty(name, map);
        }
    }

    protected <V> void removeMapPropertyEntry(String name, String key) {
        @SuppressWarnings("unchecked")
        Map<String, V> map = getProperty(name, Map.class);
        ModelUtil.remove(map, key);
        incrementModCount();
    }

    /// Properties used by many models, declare them centrally here.

    public String getRef() {
        return getProperty("$ref", String.class);
    }

    public void setRef(String ref) {
        if (ref != null && !ref.contains("/") && this instanceof Reference) {
            ref = ReferenceType.fromModel((Reference<?>) this).referenceOf(ref);
        }
        setProperty("$ref", ref);
    }

    public String getName() {
        return getProperty("name");
    }

    public void setName(String newValue) {
        setProperty("name", newValue);
    }

    public String getSummary() {
        return getProperty("summary", String.class);
    }

    public void setSummary(String summary) {
        setProperty("summary", summary);
    }

    public String getDescription() {
        return getProperty("description", String.class);
    }

    public void setDescription(String description) {
        setProperty("description", description);
    }

    public org.eclipse.microprofile.openapi.models.ExternalDocumentation getExternalDocs() {
        return getProperty("externalDocs");
    }

    public void setExternalDocs(org.eclipse.microprofile.openapi.models.ExternalDocumentation newValue) {
        setProperty("externalDocs", newValue);
    }

}
