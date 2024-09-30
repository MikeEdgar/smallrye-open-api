package io.smallrye.openapi.api.models.info;

import org.eclipse.microprofile.openapi.models.info.License;

import io.smallrye.openapi.api.models.BaseExtensibleModel;
import io.smallrye.openapi.api.models.ModelImpl;

/**
 * An implementation of the {@link License} OpenAPI model interface.
 */
public class LicenseImpl extends BaseExtensibleModel<License> implements License, ModelImpl {

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
    public String getUrl() {
        return getProperty("url");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUrl(String url) {
        setProperty("url", url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIdentifier() {
        return getProperty("identifier");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setIdentifier(String identifier) {
        setProperty("identifier", identifier);
    }

}
