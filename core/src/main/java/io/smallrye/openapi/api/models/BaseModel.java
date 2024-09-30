package io.smallrye.openapi.api.models;

import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.microprofile.openapi.models.media.Schema;

import io.smallrye.openapi.runtime.util.ModelUtil;

public abstract class BaseModel {

    protected final Map<String, Object> properties = new LinkedHashMap<>(2);
    private int modCount;
    private int hash = 0;

    protected BaseModel() {
    }

    protected void incrementModCount() {
        modCount++;
    }

    public int getModCount() {
        return modCount;
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
            return Objects.equals(properties, ((BaseModel) obj).properties);
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

    private static int hash(Map<Object, Object> stack, BaseModel model) {
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

        if (value instanceof Map) {
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

    @SuppressWarnings("unchecked")
    protected <T> T getProperty(String name) {
        return (T) properties.get(name);
    }

    protected <T> T getProperty(String name, Class<T> type) {
        Object value = properties.get(name);

        if (type.isInstance(value)) {
            return type.cast(value);
        } else {
            return null;
        }
    }

    protected <T> void setProperty(String name, T value) {
        if (value == null) {
            properties.remove(name);
        } else {
            properties.put(name, value);
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> Map<String, T> getMapProperty(String name) {
        Object result = getProperty(name);

        if (result instanceof Map) {
            return Collections.unmodifiableMap((Map<String, T>) result);
        } else {
            return null; // NOSONAR
        }
    }

    protected <T> void setMapProperty(String name, Map<String, T> value) {
        value = ModelUtil.replace(value, LinkedHashMap::new);

        if (value == null) {
            properties.remove(name);
        } else {
            properties.put(name, value);
        }
    }

    protected <V> void putMapPropertyEntry(String name, String key, V value) {
        if (value != null) {
            @SuppressWarnings("unchecked")
            Map<String, V> map = getProperty(name, Map.class);
            map = ModelUtil.add(key, value, map, LinkedHashMap<String, V>::new);
            setProperty(name, map);
        }
    }
}
