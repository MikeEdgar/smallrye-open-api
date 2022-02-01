package test.io.smallrye.openapi.runtime.scanner;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.callbacks.Callback;
import org.eclipse.microprofile.openapi.annotations.callbacks.CallbackOperation;
import org.eclipse.microprofile.openapi.annotations.callbacks.Callbacks;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.extensions.Extension;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

/* Test models and resources below. */
@Path(value = "/ext-custom")
public class ExtensionParsingTestResource1 {

    @POST
    @Consumes(value = MediaType.TEXT_PLAIN)
    @Produces(value = MediaType.TEXT_PLAIN)
    @Callbacks(value = {
            @Callback(name = "extendedCallback", callbackUrlExpression = "http://localhost:8080/resources/ext-callback", operations = @CallbackOperation(summary = "Get results", extensions = {
                    @Extension(name = "x-object", value = "{ \"key\":\"value\" }", parseValue = true),
                    @Extension(name = "x-object-unparsed", value = "{ \"key\":\"value\" }"),
                    @Extension(name = "x-array", value = "[ \"val1\",\"val2\" ]", parseValue = true),
                    @Extension(name = "x-booltrue", value = "true", parseValue = false) }, method = "get", responses = @APIResponse(responseCode = "200", description = "successful operation", content = @Content(mediaType = "application/json", schema = @Schema(type = SchemaType.ARRAY, implementation = String.class))))) })
    public String get(String data) {
        return data;
    }

}
