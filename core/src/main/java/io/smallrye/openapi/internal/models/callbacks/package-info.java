@OASModelType(name = "Callback", parent = io.smallrye.openapi.internal.models.BaseExtensibleModel.class, constructible = org.eclipse.microprofile.openapi.models.callbacks.Callback.class, properties = {
        @OASModelProperty(name = "ref", type = String.class),
        @OASModelProperty(name = "PathItems", unwrapped = true, type = Map.class, valueType = org.eclipse.microprofile.openapi.models.PathItem.class),
})
package io.smallrye.openapi.internal.models.callbacks;

import java.util.Map;

import io.smallrye.openapi.model.apt.OASModelProperty;
import io.smallrye.openapi.model.apt.OASModelType;
