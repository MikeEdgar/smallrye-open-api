package io.smallrye.openapi.api.models.media;

import static io.smallrye.openapi.runtime.io.schema.SchemaConstant.PROP_EXCLUSIVE_MAXIMUM;
import static io.smallrye.openapi.runtime.io.schema.SchemaConstant.PROP_EXCLUSIVE_MINIMUM;
import static io.smallrye.openapi.runtime.io.schema.SchemaConstant.PROP_TYPE;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.util.List;

import org.eclipse.microprofile.openapi.models.media.Schema;

import io.smallrye.openapi.runtime.OpenApiRuntimeException;

/**
 * An implementation of the {@link Schema} OpenAPI model interface.
 */
class Schema30Impl extends SchemaImpl implements InvocationHandler {

    static SmallRyeSchema newInstance(String name) {
        return (SmallRyeSchema) Proxy.newProxyInstance(
                Schema30Impl.class.getClassLoader(),
                new Class[] { SmallRyeSchema.class },
                new Schema30Impl(name));
    }

    private Schema30Impl(String name) {
        super(name);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws OpenApiRuntimeException {
        try {
            switch (method.getName()) {
                case "getExclusiveMaximum":
                    return getExclusiveMaximumBoolean();
                case "setExclusiveMaximum":
                    setExclusiveMaximumBoolean((Boolean) args[0]);
                    return null;
                case "getExclusiveMinimum":
                    return getExclusiveMinimumBoolean();
                case "setExclusiveMinimum":
                    setExclusiveMinimumBoolean((Boolean) args[0]);
                    return null;
                case "getType":
                    return getType1();
                case "setType":
                    setType1((SchemaType) args[0]);
                    return null;
                default:
                    return method.invoke(this, args);
            }
        } catch (InvocationTargetException e) {
            throw new OpenApiRuntimeException(e.getTargetException());
        } catch (Exception e) {
            throw new OpenApiRuntimeException(e);
        }
    }

    @Override
    public BigDecimal getExclusiveMaximum() {
        throw new UnsupportedOperationException();
    }

    public Boolean getExclusiveMaximumBoolean() {
        return getProperty(PROP_EXCLUSIVE_MAXIMUM, Boolean.class);
    }

    @Override
    public void setExclusiveMaximum(BigDecimal exclusiveMaximum) {
        throw new UnsupportedOperationException();
    }

    public void setExclusiveMaximumBoolean(Boolean exclusiveMaximum) {
        setProperty(PROP_EXCLUSIVE_MAXIMUM, exclusiveMaximum);
    }

    @Override
    public BigDecimal getExclusiveMinimum() {
        throw new UnsupportedOperationException();
    }

    public Boolean getExclusiveMinimumBoolean() {
        return getProperty(PROP_EXCLUSIVE_MINIMUM, Boolean.class);
    }

    @Override
    public void setExclusiveMinimum(BigDecimal exclusiveMinimum) {
        throw new UnsupportedOperationException();
    }

    public void setExclusiveMinimumBoolean(Boolean exclusiveMinimum) {
        setProperty(PROP_EXCLUSIVE_MINIMUM, exclusiveMinimum);
    }

    @Override
    public List<SchemaType> getType() {
        throw new UnsupportedOperationException();
    }

    public SchemaType getType1() {
        return getProperty(PROP_TYPE, SchemaType.class);
    }

    @Override
    public void setType(List<SchemaType> types) {
        throw new UnsupportedOperationException();
    }

    public void setType1(SchemaType type) {
        setProperty(PROP_TYPE, type);
    }

    @Override
    public SmallRyeSchema type(SchemaType type) {
        this.setType1(type);
        return this;
    }

    @Override
    public Schema addType(SchemaType type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeType(SchemaType type) {
        throw new UnsupportedOperationException();
    }

    public Boolean getNullable() {
        return SmallRyeSchema.getNullable(this);
    }

    public void setNullable(Boolean nullable) {
        SmallRyeSchema.setNullable(this, nullable);
    }
}
