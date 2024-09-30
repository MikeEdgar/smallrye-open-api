package io.smallrye.openapi.api.models;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.eclipse.microprofile.openapi.models.Extensible;

import io.smallrye.openapi.runtime.util.ModelUtil;

public abstract class BaseExtensibleModel<T extends Extensible<T>> extends BaseModel implements Extensible<T> {

    private static final Set<String> EMPTY = Collections.emptySet();
    private Set<String> extensionNames = EMPTY;

    protected BaseExtensibleModel() {
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        if (obj instanceof BaseExtensibleModel) {
            return Objects.equals(extensionNames, ((BaseExtensibleModel<?>) obj).extensionNames);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode() * 31 + Objects.hash(extensionNames);
    }

    @Override
    protected <P> P getProperty(String name) {
        if (extensionNames.contains(name)) {
            // Do not return extension when accessed as a property
            return null;
        }
        return super.getProperty(name);
    }

    @Override
    protected <P> P getProperty(String name, Class<P> type) {
        if (extensionNames.contains(name)) {
            // Do not return extension when accessed as a property
            return null;
        }
        return super.getProperty(name, type);
    }

    @Override
    protected <P> void setProperty(String name, P value) {
        if (extensionNames.contains(name)) {
            // Property replaces the extension entry.
            extensionNames.remove(name);
        }
        super.setProperty(name, value);
    }

    @Override
    protected <V> Map<String, V> getMapProperty(String name) {
        if (extensionNames.contains(name)) {
            // Do not return extension when accessed as a property
            return null; // NOSONAR
        }
        return super.getMapProperty(name);
    }

    @Override
    protected <V> void setMapProperty(String name, Map<String, V> value) {
        if (extensionNames.contains(name)) {
            // Property replaces the extension entry.
            extensionNames.remove(name);
        }
        super.setMapProperty(name, value);
    }

    @Override
    protected <V> void putMapPropertyEntry(String name, String key, V value) {
        if (extensionNames.contains(name)) {
            // Property replaces the extension entry.
            extensionNames.remove(name);
        }
        super.putMapPropertyEntry(name, key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getExtensions() {
        if (extensionNames.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, Object> ext = new LinkedHashMap<>(extensionNames.size());

        for (Map.Entry<String, Object> property : properties.entrySet()) {
            if (extensionNames.contains(property.getKey())) {
                ext.put(property.getKey(), property.getValue());
            }
        }

        return ModelUtil.unmodifiableMap(ext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public T addExtension(String name, Object value) {
        if (value != null) {
            if (extensionNames == EMPTY) {
                extensionNames = new HashSet<>(1);
            }
            extensionNames.add(name);
        }
        // Extension replaces the property entry (if present)
        super.setProperty(name, value);
        return (T) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeExtension(String name) {
        if (extensionNames.remove(name)) {
            // Only remove the property if it was one of the existing extensions
            super.setProperty(name, null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setExtensions(Map<String, Object> extensions) {
        for (String name : extensionNames) {
            removeExtension(name);
        }

        if (extensions == null || extensions.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> extension : extensions.entrySet()) {
            addExtension(extension.getKey(), extension.getValue());
        }
    }
}
