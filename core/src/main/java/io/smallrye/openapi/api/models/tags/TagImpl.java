package io.smallrye.openapi.api.models.tags;

import org.eclipse.microprofile.openapi.models.ExternalDocumentation;
import org.eclipse.microprofile.openapi.models.tags.Tag;

import io.smallrye.openapi.api.models.BaseExtensibleModel;
import io.smallrye.openapi.api.models.ModelImpl;
import io.smallrye.openapi.api.util.VersionUtil;

/**
 * An implementation of the {@link Tag} OpenAPI model interface.
 */
public class TagImpl extends BaseExtensibleModel<Tag> implements Tag, ModelImpl {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return getProperty("name");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setName(String name) {
        setProperty("name", name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return getProperty("description");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDescription(String description) {
        setProperty("description", description);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExternalDocumentation getExternalDocs() {
        return getProperty("externalDocs");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setExternalDocs(ExternalDocumentation externalDocs) {
        setProperty("externalDocs", externalDocs);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (VersionUtil.compareMicroProfileVersion("3.0") < 0) {
            // TCK versions before MP OpenAPI release 3.0 check Tag instances are not equal
            return false;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        if (VersionUtil.compareMicroProfileVersion("3.0") < 0) {
            // TCK versions before MP OpenAPI release 3.0 check Tag instances are not equal
            return System.identityHashCode(this);
        }

        return super.hashCode();
    }
}
