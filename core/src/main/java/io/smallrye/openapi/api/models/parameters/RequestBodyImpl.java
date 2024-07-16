package io.smallrye.openapi.api.models.parameters;

import org.eclipse.microprofile.openapi.models.media.Content;
import org.eclipse.microprofile.openapi.models.parameters.RequestBody;

import io.smallrye.openapi.api.models.ExtensibleImpl;
import io.smallrye.openapi.api.models.ModelImpl;
import io.smallrye.openapi.api.util.VersionUtil;
import io.smallrye.openapi.runtime.io.ReferenceType;

/**
 * An implementation of the {@link RequestBody} OpenAPI model interface.
 */
public class RequestBodyImpl extends ExtensibleImpl<RequestBody> implements RequestBody, ModelImpl {

    private String ref;
    private String description;
    private Content content;
    private Boolean required;
    private boolean isRequiredSet = false;

    /**
     * @see org.eclipse.microprofile.openapi.models.Reference#getRef()
     */
    @Override
    public String getRef() {
        return this.ref;
    }

    /**
     * @see org.eclipse.microprofile.openapi.models.Reference#setRef(java.lang.String)
     */
    @Override
    public void setRef(String ref) {
        if (ref != null && !ref.contains("/")) {
            ref = ReferenceType.REQUEST_BODY.referenceOf(ref);
        }
        this.ref = ref;
    }

    /**
     * @see org.eclipse.microprofile.openapi.models.parameters.RequestBody#getDescription()
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * @see org.eclipse.microprofile.openapi.models.parameters.RequestBody#setDescription(java.lang.String)
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @see org.eclipse.microprofile.openapi.models.parameters.RequestBody#getContent()
     */
    @Override
    public Content getContent() {
        return this.content;
    }

    /**
     * @see org.eclipse.microprofile.openapi.models.parameters.RequestBody#setContent(org.eclipse.microprofile.openapi.models.media.Content)
     */
    @Override
    public void setContent(Content content) {
        this.content = content;
    }

    /**
     * @see org.eclipse.microprofile.openapi.models.parameters.RequestBody#getRequired()
     */
    @Override
    public Boolean getRequired() {
        if (isRequiredSet) {
            return this.required;
        } else {
            return VersionUtil.VER4 ? Boolean.TRUE : null;
        }
    }

    /**
     * @see org.eclipse.microprofile.openapi.models.parameters.RequestBody#setRequired(java.lang.Boolean)
     */
    @Override
    public void setRequired(Boolean required) {
        this.required = required;
        isRequiredSet = true;
    }

    /**
     * Returns whether {@link #setRequired(Boolean)} has been called on a request body.
     * <p>
     * Always returns {@code true} if the request body is not a {@code RequestBodyImpl}
     *
     * @param requestBody the request body
     * @return {@code true} if {@code setRequired} has been called
     */
    public static boolean isRequiredSet(RequestBody requestBody) {
        if (requestBody instanceof RequestBodyImpl) {
            return ((RequestBodyImpl) requestBody).isRequiredSet;
        } else {
            return true;
        }
    }
}
