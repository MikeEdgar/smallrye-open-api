package io.smallrye.openapi.api.models.media;

import static io.smallrye.openapi.runtime.io.schema.SchemaConstant.PROP_EXCLUSIVE_MAXIMUM;
import static io.smallrye.openapi.runtime.io.schema.SchemaConstant.PROP_EXCLUSIVE_MINIMUM;
import static io.smallrye.openapi.runtime.io.schema.SchemaConstant.PROP_TYPE;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.eclipse.microprofile.openapi.models.media.Schema;

import io.smallrye.openapi.api.models.ModelImpl;

/**
 * An implementation of the {@link Schema} OpenAPI model interface.
 */
class Schema31Impl extends SchemaImpl implements SmallRyeSchema, ModelImpl {

    /**
     * Create an empty named schema
     *
     * @param name the name
     */
    protected Schema31Impl(String name) {
        super(name);
    }

    /**
     * Create an empty schema
     */
    protected Schema31Impl() {
        this((String) null);
    }

    /**
     * @see org.eclipse.microprofile.openapi.models.media.Schema#getExclusiveMaximum()
     */
    @Override
    public BigDecimal getExclusiveMaximum() {
        return getProperty(PROP_EXCLUSIVE_MAXIMUM, BigDecimal.class);
    }

    /**
     * @see org.eclipse.microprofile.openapi.models.media.Schema#setExclusiveMaximum(java.math.BigDecimal)
     */
    @Override
    public void setExclusiveMaximum(BigDecimal exclusiveMaximum) {
        setProperty(PROP_EXCLUSIVE_MAXIMUM, exclusiveMaximum);
    }

    /**
     * @see org.eclipse.microprofile.openapi.models.media.Schema#getExclusiveMinimum()
     */
    @Override
    public BigDecimal getExclusiveMinimum() {
        return getProperty(PROP_EXCLUSIVE_MINIMUM, BigDecimal.class);
    }

    /**
     * @see org.eclipse.microprofile.openapi.models.media.Schema#setExclusiveMinimum(java.math.BigDecimal)
     */
    @Override
    public void setExclusiveMinimum(BigDecimal exclusiveMinimum) {
        setProperty(PROP_EXCLUSIVE_MINIMUM, exclusiveMinimum);
    }

    @Override
    public List<SchemaType> getTypes() {
        return getType();
    }

    /**
     * @see org.eclipse.microprofile.openapi.models.media.Schema#getType()
     */
    @Override
    public List<SchemaType> getType() {
        List<SchemaType> resultList = getListProperty(PROP_TYPE);
        if (resultList != null) {
            return resultList;
        }

        SchemaType result = getProperty(PROP_TYPE, SchemaType.class);
        if (result != null) {
            return Collections.singletonList(result);
        }

        return null;
    }

    @Override
    public void setType(List<SchemaType> types) {
        nullable = null;
        setListProperty(PROP_TYPE, types);

        if (typeObservers != null) {
            typeObservers.forEach(o -> SmallRyeSchema.setTypeRetainingNull(o, types));
        }
    }

    @Override
    public SmallRyeSchema types(List<SchemaType> types) {
        setType(types);
        return this;
    }

    @Override
    public Boolean getAdditionalPropertiesBoolean() {
        Schema additionalPropertiesSchema = getAdditionalPropertiesSchema();
        return additionalPropertiesSchema == null ? null : additionalPropertiesSchema.getBooleanSchema();
    }

    /**
     * @see org.eclipse.microprofile.openapi.models.media.Schema#setAdditionalPropertiesBoolean(java.lang.Boolean)
     */
    @Override
    public void setAdditionalPropertiesBoolean(Boolean additionalProperties) {
        if (additionalProperties != null) {
            setAdditionalPropertiesSchema(new Schema31Impl().booleanSchema(additionalProperties));
        } else {
            setAdditionalPropertiesSchema(null);
        }
    }
}
