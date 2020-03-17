package io.smallrye.openapi.runtime.io.callback;

import org.eclipse.microprofile.openapi.annotations.callbacks.Callback;
import org.eclipse.microprofile.openapi.annotations.callbacks.Callbacks;
import org.jboss.jandex.DotName;

import io.smallrye.openapi.runtime.io.Referenceable;

/**
 * Constants related to Callback.
 * 
 * @see <a href="https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.3.md#callbackObject">callbackObject</a>
 * 
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 * @author Eric Wittmann (eric.wittmann@gmail.com)
 */
public class CallbackConstant implements Referenceable {

    static final DotName DOTNAME_CALLBACKS = DotName.createSimple(Callbacks.class.getName());
    static final DotName DOTNAME_CALLBACK = DotName.createSimple(Callback.class.getName());

    static final String PROP_NAME = "name";
    static final String PROP_OPERATIONS = "operations";
    static final String PROP_CALLBACK_URL_EXPRESSION = "callbackUrlExpression";

    private CallbackConstant() {
    }
}
