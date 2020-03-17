package io.smallrye.openapi.runtime.io.requestbody;

import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.jboss.jandex.DotName;

import io.smallrye.openapi.runtime.io.Referenceable;

/**
 * Constants related to RequestBody
 * 
 * @see <a href="https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.3.md#requestBodyObject">requestBodyObject</a>
 * 
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 * @author Eric Wittmann (eric.wittmann@gmail.com)
 */
public class RequestBodyConstant implements Referenceable {

    static final DotName DOTNAME_REQUESTBODY = DotName.createSimple(RequestBody.class.getName());

    static final String PROP_NAME = "name";
    static final String PROP_REQUIRED = "required";

    static final String PROP_DESCRIPTION = "description";
    static final String PROP_REQUEST_BODY = "requestBody";
    static final String PROP_CONTENT = "content";

    private RequestBodyConstant() {
    }
}
