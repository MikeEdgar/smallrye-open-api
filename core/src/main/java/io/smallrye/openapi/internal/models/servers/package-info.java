@OASModelType(name = "Server", parent = io.smallrye.openapi.internal.models.BaseExtensibleModel.class, constructible = org.eclipse.microprofile.openapi.models.servers.Server.class, properties = {
        @OASModelProperty(name = "url", type = String.class),
        @OASModelProperty(name = "description", type = String.class),
        @OASModelProperty(name = "variables", type = Map.class, valueType = org.eclipse.microprofile.openapi.models.servers.ServerVariable.class),
})
@OASModelType(name = "ServerVariable", parent = io.smallrye.openapi.internal.models.BaseExtensibleModel.class, constructible = org.eclipse.microprofile.openapi.models.servers.ServerVariable.class, properties = {
        @OASModelProperty(name = "enumeration", singularName = "enumeration", type = List.class, valueType = String.class),
        @OASModelProperty(name = "defaultValue", type = String.class),
        @OASModelProperty(name = "description", type = String.class),
})
package io.smallrye.openapi.internal.models.servers;

import java.util.List;
import java.util.Map;

import io.smallrye.openapi.model.apt.OASModelProperty;
import io.smallrye.openapi.model.apt.OASModelType;
