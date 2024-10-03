@OASModelType(name = "Contact", parent = io.smallrye.openapi.internal.models.BaseExtensibleModel.class, constructible = org.eclipse.microprofile.openapi.models.info.Contact.class, properties = {
        @OASModelProperty(name = "name", type = String.class),
        @OASModelProperty(name = "url", type = String.class),
        @OASModelProperty(name = "email", type = String.class),
})
@OASModelType(name = "Info", parent = io.smallrye.openapi.internal.models.BaseExtensibleModel.class, constructible = org.eclipse.microprofile.openapi.models.info.Info.class, properties = {
        @OASModelProperty(name = "title", type = String.class),
        @OASModelProperty(name = "description", type = String.class),
        @OASModelProperty(name = "termsOfService", type = String.class),
        @OASModelProperty(name = "contact", type = org.eclipse.microprofile.openapi.models.info.Contact.class),
        @OASModelProperty(name = "license", type = org.eclipse.microprofile.openapi.models.info.License.class),
        @OASModelProperty(name = "version", type = String.class),
        @OASModelProperty(name = "summary", type = String.class),
})
@OASModelType(name = "License", parent = io.smallrye.openapi.internal.models.BaseExtensibleModel.class, constructible = org.eclipse.microprofile.openapi.models.info.License.class, properties = {
        @OASModelProperty(name = "name", type = String.class),
        @OASModelProperty(name = "url", type = String.class),
        @OASModelProperty(name = "identifier", type = String.class),
})
package io.smallrye.openapi.internal.models.info;

import io.smallrye.openapi.model.apt.OASModelProperty;
import io.smallrye.openapi.model.apt.OASModelType;
