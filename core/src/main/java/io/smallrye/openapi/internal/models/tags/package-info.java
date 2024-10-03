@OASModelType(name = "Tag", parent = io.smallrye.openapi.internal.models.BaseExtensibleModel.class, constructible = org.eclipse.microprofile.openapi.models.tags.Tag.class, properties = {
        @OASModelProperty(name = "name", type = String.class),
        @OASModelProperty(name = "description", type = String.class),
        @OASModelProperty(name = "externalDocs", type = ExternalDocumentation.class)
})
package io.smallrye.openapi.internal.models.tags;

import org.eclipse.microprofile.openapi.models.ExternalDocumentation;

import io.smallrye.openapi.model.apt.OASModelProperty;
import io.smallrye.openapi.model.apt.OASModelType;
