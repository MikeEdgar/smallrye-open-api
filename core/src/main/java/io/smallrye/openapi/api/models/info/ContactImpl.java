package io.smallrye.openapi.api.models.info;

import org.eclipse.microprofile.openapi.models.info.Contact;

import io.smallrye.openapi.api.models.BaseExtensibleModel;
import io.smallrye.openapi.api.models.ModelImpl;

/**
 * An implementation of the {@link Contact} OpenAPI model interface.
 */
public class ContactImpl extends BaseExtensibleModel<Contact> implements Contact, ModelImpl {

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
    public String getEmail() {
        return getProperty("email");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEmail(String email) {
        setProperty("email", email);
    }

}
